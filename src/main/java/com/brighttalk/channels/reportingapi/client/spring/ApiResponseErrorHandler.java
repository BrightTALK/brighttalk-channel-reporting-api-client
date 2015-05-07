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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import com.brighttalk.channels.reportingapi.client.ApiClientException;
import com.brighttalk.channels.reportingapi.client.ApiErrorResponseException;
import com.brighttalk.channels.reportingapi.client.resource.ApiError;
import com.google.common.base.Preconditions;

/**
 * Implementation of {@link org.springframework.web.client.ResponseErrorHandler} for responses returned by the
 * BrightTALK Channel owner reporting API. Replaces Spring's
 * {@link org.springframework.web.client.DefaultResponseErrorHandler} to translate HTTP error responses (as indicated by
 * status code) to {@link ApiErrorResponseException} rather than exposing Spring's
 * {@link org.springframework.web.client.HttpStatusCodeException}. Also supports unmarshalling the supplementary, custom
 * error information returned by the API in the response body to an {@link ApiError} object, and making it available in
 * the thrown Exception.
 * 
 * @see org.springframework.web.client.DefaultResponseErrorHandler
 * @see com.brighttalk.channels.reportingapi.client.ApiErrorResponseException
 * @author Neil Brown
 */
public class ApiResponseErrorHandler implements ResponseErrorHandler {

  private static final Logger logger = LoggerFactory.getLogger(ApiResponseErrorHandler.class);

  /**
   * Instance of {@link org.springframework.web.client.ResponseExtractor} used to extract and unmarshall the optional
   * {@link Error} in the body of an HTTP error response returned by the API service.
   */
  private HttpMessageConverterExtractor<ApiError> errorResponseExtractor;

  /**
   * @param messageConverters The list of {@link HttpMessageConverter} that should be used to convert the body of an
   * error response returned by the API service. All of the supplied converters must be capable of reading an
   * {@link ApiError} in a media type of application/xml.
   */
  public ApiResponseErrorHandler(List<HttpMessageConverter<?>> messageConverters) {
    Preconditions.checkNotNull(messageConverters, "messageConverters must be non-null.");
    for (HttpMessageConverter<?> messageConverter : messageConverters) {
      if (!messageConverter.canRead(ApiError.class, MediaType.APPLICATION_XML)) {
        throw new IllegalArgumentException("HttpMessageConverter [" + messageConverter + "] must support reading "
            + "an object of class [" + ApiError.class.toString() + "].");
      }
    }
    this.errorResponseExtractor = new HttpMessageConverterExtractor<ApiError>(ApiError.class, messageConverters);
  }

  /**
   * {@inheritDoc}
   * <p>
   * This implementation returns {@code true} if the response status code is in the client error range (4xx) or server
   * error range (5xx).
   */
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return hasError(getHttpStatusCode(response));
  }

  /**
   * {@inheritDoc}
   * <p>
   * This implementation handles the HTTP error response by throwing an {@link ApiErrorResponseException} which includes
   * an {@link ApiError error} if the response body contained an API error entity.
   */
  @Override
  public void handleError(ClientHttpResponse response) throws ApiErrorResponseException, IOException {
    HttpStatus statusCode = getHttpStatusCode(response);
    ApiError apiError = this.extractErrorResponse(response);
    throw new ApiErrorResponseException(statusCode.value(), response.getStatusText(), response.getHeaders(),
        getCharset(response), getResponseBody(response), apiError);
  }

  /**
   * Template method called from {@link #hasError(ClientHttpResponse)}.
   * <p>
   * The default implementation checks if the given status code is
   * {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR CLIENT_ERROR} or
   * {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR SERVER_ERROR}. Can be overridden in subclasses.
   * 
   * @param statusCode the HTTP status code
   * @return {@code true} if the response has an error; {@code false} otherwise
   */
  protected boolean hasError(HttpStatus statusCode) {
    return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR || statusCode.series() == HttpStatus.Series.SERVER_ERROR);
  }

  private HttpStatus getHttpStatusCode(ClientHttpResponse response) throws IOException {
    HttpStatus statusCode;
    try {
      statusCode = response.getStatusCode();
    } catch (IllegalArgumentException ex) {
      throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(),
          response.getHeaders(), getResponseBody(response), getCharset(response));
    }
    return statusCode;
  }

  private byte[] getResponseBody(ClientHttpResponse response) {
    try {
      InputStream responseBody = response.getBody();
      if (responseBody != null) {
        return FileCopyUtils.copyToByteArray(responseBody);
      }
    } catch (IOException ex) {
      // ignore
    }
    return new byte[0];
  }

  private Charset getCharset(ClientHttpResponse response) {
    HttpHeaders headers = response.getHeaders();
    MediaType contentType = headers.getContentType();
    return contentType != null ? contentType.getCharSet() : null;
  }

  /**
   * Extracts API error data from the body of a supplied {@link ClientHttpResponse HTTP error response} if it exists,
   * using one of this object's configured HTTP message converter.
   * 
   * @param errorResponse The {@link ClientHttpResponse HTTP error response}.
   * @return An {@link ApiError} populated with the API error data, or null if the response body was empty (as can occur
   * for some types of severe API errors, e.g. HTTP 500 Internal Server Error) or didn't contain error data.
   */
  private ApiError extractErrorResponse(ClientHttpResponse errorResponse) {
    ApiError apiError = null;
    try {
      apiError = this.errorResponseExtractor.extractData(errorResponse);
    } catch (RestClientException e) {
      // The response body was in a media-type which couldn't be unmarshalled by the configured HttpMessageConverter
      // This can happen when a web proxy returns the error response and includes e.g. an HTML error page
      // Suppress this error - treat it like an empty respnonse.
      logger.debug("Error extracting API error from response body. Ignoring.", e);
    } catch (HttpMessageNotReadableException e) {
      // The response body was in a supported media type but contained an unexpected entity which couldn't be
      // unmarshalled. This could happen if a web proxy returned the error response.
      // Suppress this error - treat like an empty respnonse.
      logger.debug("Error extracting API error from response body. Unknown entity. Ignoring.", e);
    } catch (IOException e) {
      throw new ApiClientException("I/O error on extracting API error from response body.", e);
    }
    return apiError;
  }
}