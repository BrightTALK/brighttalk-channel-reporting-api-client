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
package com.brighttalk.channels.reportingapi.client.common;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * A set of credentials used to authenticate an API client as an owner of one or more BrightTALK Channels.
 * 
 * @author Neil Brown
 */
public class ApiCredentials {
  
  private final int key;
  private final String secret;
  
  /**
   * @param key The unique identifier for the BrightTALK API user.
   * @param secret A secret value used to authenticate the API user identified by the {@code key}.  
   */
  public ApiCredentials(int key, String secret) {
    Preconditions.checkArgument(key > 0, "API key must be a positive integer.");
    Preconditions.checkArgument(Strings.isNullOrEmpty(secret), "API secret must be a non-empty string.");
    this.key = key;
    this.secret = secret;
  }

  public final int getKey() {
    return this.key;
  }

  public final String getSecret() {
    return this.secret;
  }       
}