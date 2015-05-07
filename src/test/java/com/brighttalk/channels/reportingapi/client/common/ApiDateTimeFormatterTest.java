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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.brighttalk.channels.reportingapi.client.common.ApiDateTimeFormatter;

/**
 * Unit tests for {@link ApiDateTimeFormatter}.
 * 
 * @author Neil Brown
 */
public class ApiDateTimeFormatterTest {
  
  private ApiDateTimeFormatter uut;

  @Before
  public void setUp() {
    this.uut = new ApiDateTimeFormatter();
  }

  /**
   * Tests {@link ApiDateTimeFormatter#formatAsDateTime} for a non-null date.
   */
  @Test
  public void testFormatAsDateTime() {
    Date date = new Date();
    String expectedFormatted = createSimpleDateFormat(ApiDateTimeFormatter.API_DATE_TIME_PATTERN).format(date);
    String actualFormatted = this.uut.formatAsDateTime(date);
    assertThat(actualFormatted, is(expectedFormatted));
  }
  
  /**
   * Tests {@link ApiDateTimeFormatter#formatAsDateOnly} for a non-null date.
   */
  @Test
  public void testFormatAsDateOnly() {
    Date date = new Date();
    String expectedFormatted = createSimpleDateFormat(ApiDateTimeFormatter.API_DATE_ONLY_PATTERN).format(date);
    String actualFormatted = this.uut.formatAsDateOnly(date);
    assertThat(actualFormatted, is(expectedFormatted));
  }
    
  /**
   * Tests {@link ApiDateTimeFormatter#parse} for a date/time string which conforms to the format expected by the 
   * reporting APIs and comprises all component fields - date, time (full resolution) and time zone indicator.
   * 
   * @throws Exception If an unexpected exception occurs.
   */
  @Test
  public void testParseValidFormatDateTimeString() throws Exception {
    String dateTimeString = "2014-04-30T21:32:21Z";
    Date expectedDate = createSimpleDateFormat(ApiDateTimeFormatter.API_DATE_TIME_PATTERN).parse(dateTimeString);
    Date actualDate = this.uut.parse(dateTimeString);
    assertThat(actualDate, is(expectedDate));
  }
  
  /**
   * Tests {@link ApiDateTimeFormatter#parse} for a date only string which conforms to the format expected by the 
   * reporting APIs.
   * 
   * @throws Exception If an unexpected exception occurs.
   */
  @Test
  public void testParseValidFormatDateOnlyString() throws Exception {
    String dateTimeString = "2014-04-30";
    Date expectedDate = createSimpleDateFormat(ApiDateTimeFormatter.API_DATE_ONLY_PATTERN).parse(dateTimeString);
    Date actualDate = this.uut.parse(dateTimeString);
    assertThat(actualDate, is(expectedDate));
  }
  
  private static SimpleDateFormat createSimpleDateFormat(String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    return sdf;
  } 
}