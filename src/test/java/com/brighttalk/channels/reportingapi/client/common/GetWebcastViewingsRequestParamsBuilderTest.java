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

import static org.junit.Assert.*;

import org.junit.Test;

import com.brighttalk.channels.reportingapi.client.common.GetWebcastViewingsRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.client.resource.WebcastStatus;

/**
 * Unit tests for {@link GetWebcastViewingsRequestParamsBuilder}.
 * 
 * @author Neil Brown
 */
public class GetWebcastViewingsRequestParamsBuilderTest {

  /**
   * Tests {@link GetWebcastViewingsRequestParamsBuilder#GetWebcastViewingsRequestParamsBuilder} in the case where the
   * supplied webcast status is one of the set of status supported by the API - neither 'live' or 'recorded'.
   */
  @Test
  public void testGetWebcastViewingsRequestParamsBuilderUnsupportedWebcastStatus() {
    WebcastStatus unsupportedWebcastStatus = WebcastStatus.UPCOMING;
    try {
      new GetWebcastViewingsRequestParamsBuilder(null, unsupportedWebcastStatus, null);
      fail("Exepected IllegalArgumentException to be thrown.");
    } catch (IllegalArgumentException e) {
      assertTrue("Unexpected exception [" + e.toString() + "].",
          e.getMessage().matches(".*" + unsupportedWebcastStatus + ".*"));
    }
  }
}