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
package com.brighttalk.channels.reportingapi.client;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.brighttalk.channels.reportingapi.client.resource.ApiError;

/**
 * Class of non-checked (runtime) exception thrown to report an error response for a failed API request.
 * <p>
 * Subclass' {@link HttpErrorResponseException}, reflecting the fact that the API uses HTTP and a RESTful style of error
 * reporting based on using a combination of standard HTTP status codes and an API specific {@link ApiError} body.
 * <p>
 * The HTTP status code can be used to classify the error as either a client (4xx series) or server (5xx series)
 * originating error. The presence of an {@link Error} in the response body distinguishes an error response returned by
 * the API service from an intermediate web server/proxy.
 * <p>
 * Inspired by org.springframework.web.client.HttpClientErrorException
 * 
 * @author Neil Brown
 */
public class ApiErrorResponseException extends HttpErrorResponseException {

  private final ApiError apiError;

  /**
   * @param statusCode The HTTP response status code.
   * @param statusText The HTTP response status text.
   * @param responseHeaders The HTTP response headers.
   * @param responseCharset The character set of the HTTP response body.
   * @param responseBody The HTTP response body.
   * @param apiError The {@link ApiError API error} returned in the response, or null if not present because the error
   * response was returned by an intermediate web server/proxy.
   */
  public ApiErrorResponseException(int statusCode, String statusText, Map<String, List<String>> responseHeaders,
      Charset responseCharset, byte[] responseBody, ApiError apiError) {
    super(statusCode, statusText, responseHeaders, responseCharset, responseBody);
    this.apiError = apiError;
  }

  /**
   * @return The {@link ApiError} returned in the response, or null if not present e.g. because the error response was
   * returned by an intermediate web server/proxy.
   */
  public final ApiError getApiError() {
    return this.apiError;
  }
}