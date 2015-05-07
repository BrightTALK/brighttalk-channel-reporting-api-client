/*
 * Copyright 2014-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brighttalk.channels.reportingapi.client.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.mock.http.client.MockClientHttpResponse;

import com.brighttalk.channels.reportingapi.client.ApiErrorResponseException;
import com.brighttalk.channels.reportingapi.client.resource.ApiError;
import com.brighttalk.channels.reportingapi.client.spring.ApiResponseErrorHandler;

/**
 * Unit tests for {@link ApiResponseErrorHandler}.
 * 
 * @author Neil Brown
 */
public class ApiResponseErrorHandlerTest {

  private ApiResponseErrorHandler errorHandler;

  /**
   * Set-up test fixtures used by all test methods.
   */
  @Before
  public void setUp() {
    this.errorHandler = new ApiResponseErrorHandler(
        Arrays.asList(new HttpMessageConverter<?>[] { new Jaxb2RootElementHttpMessageConverter() }));
  }

  /**
   * Tests constructor {@link ApiResponseErrorHandler#ApiResponseErrorHandler(java.util.List)} in the error case where
   * one of the {@link HttpMessageConverter} isn't capable of reading an {@link ApiError} object.
   */
  @Test
  public void testApiResponseErrorHandlerForUnsupportedConverter() {
    HttpMessageConverter<?> supportedConverter = new Jaxb2RootElementHttpMessageConverter();
    HttpMessageConverter<?> unsupportedConveter = new StringHttpMessageConverter();
    try {
      this.errorHandler = new ApiResponseErrorHandler(Arrays.asList(new HttpMessageConverter<?>[] { supportedConverter,
          unsupportedConveter }));
      fail("Expected IllegalArgumentException to be thrown for HttpMessageConverter that doesn't support reading an ApiError.");
    } catch (IllegalArgumentException e) {
      assertTrue("Unexpected exception [" + e.toString() + "].",
          e.getMessage().matches("HttpMessageConverter.*must support reading.*"));
    }
  }

  /**
   * Tests {@link ApiResponseErrorHandler#hasError} in the case where the response has an HTTP status code in the client
   * error (4xx) series, specifically HTTP 400 Bad Request.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testHasErrorWhenHttpStatusBadRequest() throws Exception {
    byte[] body = null;
    ClientHttpResponse response = new MockClientHttpResponse(body, HttpStatus.BAD_REQUEST);
    assertThat(this.errorHandler.hasError(response), CoreMatchers.is(true));
  }

  /**
   * Tests {@link ApiResponseErrorHandler#hasError} in the case where the response has an HTTP status code in the server
   * error (5xx) series, specifically HTTP 500 Internal Server Error.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testHasErrorWhenHttpStatusInternalServerError() throws Exception {
    byte[] body = null;
    ClientHttpResponse response = new MockClientHttpResponse(body, HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(this.errorHandler.hasError(response), CoreMatchers.is(true));
  }

  /**
   * Tests {@link ApiResponseErrorHandler#hasError} in the case where the response has an HTTP status code in the
   * success (2xx) series, specifically HTTP 200 OK.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testHasErrorWhenHttpStatusOK() throws Exception {
    byte[] body = null;
    ClientHttpResponse response = new MockClientHttpResponse(body, HttpStatus.OK);
    assertThat(this.errorHandler.hasError(response), CoreMatchers.is(false));
  }

  /**
   * Tests {@link ApiResponseErrorHandler#handleError} in the case where the error response is returned by the API
   * service and contains an API error entity.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testHandleErrorWhenResponseBodyContainsApiError() throws Exception {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_XML);
    String apiErrorCode = "InvalidChannelId";
    String apiErrorMessage = "Invalid channel id [foo].";
    String apiErrorXml = "<?xml version='1.0' encoding='UTF-8'?><error><code>" + apiErrorCode
        + "</code><message>" + apiErrorMessage + "</message></error>";    
    ClientHttpResponse response = this.createMockClientHttpResponse(HttpStatus.BAD_REQUEST, httpHeaders, apiErrorXml);
    EasyMock.replay(response);

    try {
      this.errorHandler.handleError(response);
      fail("Expected exception to be thrown for error response.");
    } catch (ApiErrorResponseException e) {
      ApiErrorResponseException expectedException = new ApiErrorResponseException(response.getRawStatusCode(), 
        null, httpHeaders, null, apiErrorXml.getBytes(), new ApiError(apiErrorCode, apiErrorMessage));
      assertApiErrorResponseException(expectedException, e);
    }
  }
    
  /**
   * Tests {@link ApiResponseErrorHandler#handleError} in the case where the error response body is in an unsupported
   * media type, e.g. an HTML error page returned by an intermediate web proxy.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testHandleErrorWhenResponseBodyInUnsupportedMediaType() throws Exception {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.TEXT_HTML);    
    String body = "<html><head>Error</head><body>Woz error</body>";
    ClientHttpResponse response = this.createMockClientHttpResponse(HttpStatus.GATEWAY_TIMEOUT, httpHeaders, body);
    EasyMock.replay(response);

    try {
      this.errorHandler.handleError(response);
      fail("Expected exception to be thrown for error response.");
    } catch (ApiErrorResponseException e) {
      ApiError apiError = null;
      ApiErrorResponseException expectedException = new ApiErrorResponseException(response.getRawStatusCode(), 
          null, httpHeaders, null, body.getBytes(), apiError);
      assertApiErrorResponseException(expectedException, e);      
    }
  }
  
  /**
   * Tests {@link ApiResponseErrorHandler#handleError} in the case where the error response body contains an 
   * unexpected entity in a supported media type.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testHandleErrorWhenResponseBodyContainsUnexpectedEntity() throws Exception {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_XML);    
    String body = "<?xml version='1.0' encoding='UTF-8'?><notAnApiError><foo>1</foo></notAnApiError>";
    ClientHttpResponse response = this.createMockClientHttpResponse(HttpStatus.GATEWAY_TIMEOUT, httpHeaders, body);
    EasyMock.replay(response);

    try {
      this.errorHandler.handleError(response);
      fail("Expected exception to be thrown for error response.");
    } catch (ApiErrorResponseException e) {
      ApiError apiError = null;
      ApiErrorResponseException expectedException = new ApiErrorResponseException(response.getRawStatusCode(), 
          null, httpHeaders, null, body.getBytes(), apiError);
      assertApiErrorResponseException(expectedException, e);      
    }
  }  

  /**
   * Tests {@link ApiResponseErrorHandler#handleError} in the case where the error response body is empty, e.g. because
   * it is returned by an intermediate web proxy.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testHandleErrorWhenResponseBodyEmpty() throws Exception {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentLength(0);
    String body = null;
    ClientHttpResponse response = this.createMockClientHttpResponse(HttpStatus.GATEWAY_TIMEOUT, httpHeaders, body);
    EasyMock.replay(response);

    try {
      this.errorHandler.handleError(response);
      fail("Expected exception to be thrown for error response.");
    } catch (ApiErrorResponseException e) {
      ApiError apiError = null;
      ApiErrorResponseException expectedException = new ApiErrorResponseException(response.getRawStatusCode(), 
          null, httpHeaders, null, new byte[0], apiError);
      assertApiErrorResponseException(expectedException, e);      
    }
  }  
  
  private void assertApiErrorResponseException(ApiErrorResponseException expected, ApiErrorResponseException actual) {
    assertThat(actual.getStatusCode(), is(expected.getStatusCode()));
    assertThat(actual.getStatusText(), is(expected.getStatusText()));
    assertThat(actual.getResponseHeaders(), is(expected.getResponseHeaders()));
    assertThat(actual.getResponseCharset(), is(expected.getResponseCharset()));
    assertThat(actual.getResponseBody(), is(expected.getResponseBody()));
    assertThat(actual.getApiError(), is(expected.getApiError()));
  }
  
  /**
   * Creates a mock implementation of {@link ClientHttpResponse} and configures it to return supplied data.
   * <p>
   * ClientHttpResponse needs mocking for test fixtures which rely on setting response headers as for some reason Spring
   * 4.0's MockClientHttpResponse doesn't support setting response headers.
   * 
   * @param httpStatus The {@link HttpStatus} which the mock should be configured to return for invocations of
   * {@link ClientHttpResponse#getStatusCode()}.
   * @param headers The {@link HttpHeaders} which the mock should be configured to return for invocations of
   * {@link ClientHttpResponse#getHeaders()}.
   * @param body A string representation of the data which the mock should be configured to return for invocations of
   * {@link ClientHttpResponse#getBody()}.
   * @return The created {@link ClientHttpResponse}.
   */
  private ClientHttpResponse createMockClientHttpResponse(HttpStatus httpStatus, HttpHeaders headers, final String body) {
    ClientHttpResponse mockClientHttpResponse = EasyMock.createNiceMock(ClientHttpResponse.class);
    try {
      EasyMock.expect(mockClientHttpResponse.getStatusCode()).andReturn(httpStatus).anyTimes();
      EasyMock.expect(mockClientHttpResponse.getRawStatusCode()).andReturn(httpStatus.value()).anyTimes();
      EasyMock.expect(mockClientHttpResponse.getHeaders()).andReturn(headers).anyTimes();
      // Return a new InputStream for each call to ClientHttpResponse.getBody() rather than an exhausted one  
      IAnswer<InputStream> iAnswer = new IAnswer<InputStream>() {
        public InputStream answer() {
          return body != null ? new ByteArrayInputStream(body.getBytes()) : null;
        }
      };
      EasyMock.expect(mockClientHttpResponse.getBody()).andAnswer(iAnswer).anyTimes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return mockClientHttpResponse;
  }
}