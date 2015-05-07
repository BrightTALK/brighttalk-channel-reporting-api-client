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
 * The list of Channels owned by the current API user.
 * 
 * @author Neil Brown
 */
public final class MyChannelsResource {

  /** URI template string for the relative URL of this API resource. @see http://tools.ietf.org/html/rfc6570 */
  public static final String RELATIVE_URI_TEMPLATE = "/v1/my/channels";
  
  private MyChannelsResource() {};
}