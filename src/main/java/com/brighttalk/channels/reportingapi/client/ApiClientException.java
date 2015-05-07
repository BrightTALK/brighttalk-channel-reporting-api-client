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

/**
 * Base class for non-checked (runtime) exception thrown by the {@link ApiClient}.
 * 
 * @author Neil Brown
 */
public class ApiClientException extends RuntimeException {
  
  /**
   * @param message The internal, technical diagnostic message providing supplementary information about the context in 
   * which the exception occurred.  
   */
  public ApiClientException(final String message) {
    super(message);
  }
  
  /** 
   * @param cause The causal exception.
   */
  public ApiClientException(final Throwable cause) {
    super(cause);
  }  

  /** 
   * @param message The internal, technical diagnostic message providing supplementary information about the context in 
   * which the exception occurred.  
   * @param cause The causal exception.
   */
  public ApiClientException(final String message, final Throwable cause) {
    super(message, cause);
  }   
}