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
package com.brighttalk.channels.reportingapi.client.resource;

/**
 * The status of a webcast.
 * 
 * @author Neil Brown
 */
public enum WebcastStatus {
  
  /** The webcast is scheduled to go live at a future start date/time. */
  UPCOMING, 
  
  CANCELLED, 
  
  /** The webcast is currently live. */
  LIVE,
  
  /** The webcast has ended and is being prepared for on-demand viewing. */
  PROCESSING,
  
  /** The webcast is available for on-demand viewing. */
  RECORDED,
  
  ERROR;

  /** 
   * {@inheritDoc}
   * <p>
   * This implementation returns the enum constant name in lowercase for consistency with the string representation 
   * used in the resource representations returned and accepted by the API service.  
   */
  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }           
}