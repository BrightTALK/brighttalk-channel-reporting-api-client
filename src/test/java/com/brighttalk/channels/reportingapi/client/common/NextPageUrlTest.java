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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.brighttalk.channels.reportingapi.client.common.NextPageUrl;
import com.brighttalk.channels.reportingapi.client.common.PagingRequestParamsBuilder;

/**
 * Unit tests for {@link NextPageUrl}.
 * 
 * @author Neil Brown
 */
public class NextPageUrlTest {

  private static final String RESOURCE_URL = "https://api.test.brighttalk.net/v1/channel/1234/subscribers";

  private static final String CURSOR_REQUEST_PARAM_NAME = PagingRequestParamsBuilder.ParamName.CURSOR.getName();
  
  /**
   * Tests {@link NextPageUrl#parse} in the error case where an invalid format URL is supplied.
   */
  @Test(expected = IllegalArgumentException.class)
  public void parseInvalidUrlFormat() {
    NextPageUrl.parse("foo");
  }

  /**
   * Tests {@link NextPageUrl#parse} in the error case where a valid format, non-HTTP URL is supplied.
   */
  @Test(expected = IllegalArgumentException.class)
  public void parseInvalidUrlNonHttpUrl() {
    NextPageUrl.parse("file://api.test.brighttalk.net/v1/channel/1234/subscribers");
  }

  /**
   * Tests {@link NextPageUrl#parse} in the error case where the supplied URL does not contain a cursor request param
   * which is mandatory for a next page URL.
   */
  @Test(expected = IllegalArgumentException.class)
  public void parseInvalidUrlMissingCursorRequestParam() {
    NextPageUrl.parse(RESOURCE_URL);
  }

  /**
   * Tests {@link NextPageUrl#parse} in the case where the cursor request param is the first and only request param in
   * the URL.
   */
  @Test
  public void parseForCursorWhenCursorFirstAndOnlyRequestParam() {
    String expectedCursor = "1234-1";
    String url = RESOURCE_URL + "?" + CURSOR_REQUEST_PARAM_NAME + "=" + expectedCursor;
    String actualCursor = NextPageUrl.parse(url).getCursor();
    assertThat(actualCursor, is(expectedCursor));
  }

  /**
   * Tests {@link NextPageUrl#parse} in the case where the cursor request param is the first but not the only (and hence
   * not last) request param in the URL.
   */
  @Test
  public void parseForCursorWhenCursorFirstButNotOnlyRequestParam() {
    String expectedCursor = "1234-1";
    String url = RESOURCE_URL + "?" + CURSOR_REQUEST_PARAM_NAME + "=" + expectedCursor + "&foo=x";
    String actualCursor = NextPageUrl.parse(url).getCursor();
    assertThat(actualCursor, is(expectedCursor));
  }

  /**
   * Tests {@link NextPageUrl#parse} in the case where the cursor request param is the last of more than one request
   * param.
   */
  @Test
  public void parseForCursorWhenCursorLastOfSeveralRequestParams() {
    String expectedCursor = "1234-1";
    String url = RESOURCE_URL + "?foo=x&" + CURSOR_REQUEST_PARAM_NAME + "=" + expectedCursor;
    String actualCursor = NextPageUrl.parse(url).getCursor();
    assertThat(actualCursor, is(expectedCursor));
  }

  /**
   * Tests {@link NextPageUrl#parse} in the case where the cursor request param is the last of more than one request
   * param.
   */
  @Test
  public void parseForCursorWhenCursorNeitherFirstOrLastRequestParam() {
    String expectedCursor = "1234-1";
    String url = RESOURCE_URL + "?foo=x&" + CURSOR_REQUEST_PARAM_NAME + "=" + expectedCursor + "&bar=y";
    String actualCursor = NextPageUrl.parse(url).getCursor();
    assertThat(actualCursor, is(expectedCursor));
  }
}