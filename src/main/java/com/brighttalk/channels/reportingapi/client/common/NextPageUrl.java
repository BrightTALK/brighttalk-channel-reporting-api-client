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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * URL for a next page of resources, as returned by the API in the 'href' attribute of a link relation of type 'next'.
 * <p>
 * Provides support for parsing components of the URL for use in subsequent API requests.
 * 
 * @author Neil Brown
 */
public final class NextPageUrl {

  /** Compiled regex used to validate supplied URL strings start with the protocol prefix expected for HTTP URLs. */
  private static final Pattern HTTP_URL_PROTOCOL_PATTERN = Pattern.compile("^(?i)http[s]?.*");

  /**
   * Compiled regex for parsing the value of the mandatory next page cursor request param, irrespective of no. of
   * request params in the URL, and its relative position. For example, matches all the following possible patterns:
   * "...?cursor=x", "...?cursor=x&...", "...&cursor=x", "...&cursor=x&...".
   */
  private static final Pattern CURSOR_PARAM_PATTERN = Pattern.compile(".*[\\?&]" + "(?i)"
      + PagingRequestParamsBuilder.ParamName.CURSOR.getName() + "=([^&]+).*");

  private URL url;
  private String cursor;

  /**
   * Create an instance of this class by parsing a supplied string representation of a next page URL.
   * 
   * @param nextPageUrl The URL to parse, in its string form.
   * @return The created instance of {@link NextPageUrl}.
   * @throws IllegalArgumentException If the supplied URL is not a valid next page URL - a HTTP URL with a next page
   * cursor.
   */
  public static NextPageUrl parse(String nextPageUrl) {
    Preconditions.checkNotNull(nextPageUrl, "nextPageUrl must be non-null.");
    URL url = parseHttpUrl(nextPageUrl);
    String cursor = parseCursor(nextPageUrl);
    return new NextPageUrl(url, cursor);
  }

  /**
   * Private constructor. Creates an instance from a previously parsed and validated URL.
   * 
   * @param nextPageUrl The previously validated next page URL.
   * @param cursor The parsed component cursor of the next page URL.
   */
  private NextPageUrl(URL nextPageUrl, String cursor) {
    this.url = nextPageUrl;
    this.cursor = cursor;
  }

  /**
   * @return The page cursor in this next page link. Guaranteed to return a non-null string as the presence of the
   * cursor in a next page link is mandatory, and validated on construction.
   */
  public String getCursor() {
    return this.cursor;
  }
  
  @Override
  public String toString() {
    /* @formatter:off */
    return Objects.toStringHelper(this).omitNullValues()
      .add("url", this.url)
      .add("cursor", this.cursor)
      .toString();
    /* @formatter:on */
  }  

  /**
   * Parses the string representation of a next page URL as an HTTP URL.
   * 
   * @param nextPageUrl The URL to parse.
   * @return The parsed URL in its {@link URL} form.
   * @throws IllegalArgumentException If the supplied URL is not a valid HTTP URL.
   */
  private static URL parseHttpUrl(String nextPageUrl) {
    try {
      URL url = new URL(nextPageUrl);
      if (HTTP_URL_PROTOCOL_PATTERN.matcher(nextPageUrl).matches()) {
        return url;
      }
      throw new IllegalArgumentException("Next page URL must be an HTTP URL [" + nextPageUrl + "].");
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Invalid format next page URL [" + nextPageUrl + "].", e);
    }
  }

  /**
   * Parses the string representation of a next page URL for its mandatory page cursor, as encoded in a request param.
   * 
   * @param nextPageUrl The URL to parse.
   * @return The next page cursor.
   * @throws IllegalArgumentException If the supplied URL doesn't contain a page cursor.
   */
  private static String parseCursor(String nextPageUrl) {
    // To do - Always URL decode? If the next page link is returned URL encoded, convert %3D to '=' before matching
    Matcher m = CURSOR_PARAM_PATTERN.matcher(nextPageUrl);
    if (m.matches()) {
      String cursor = m.group(1);
      return cursor;
    }
    throw new IllegalArgumentException("Next page URL must contain a cursor request param [" + nextPageUrl + "].");
  }    
}