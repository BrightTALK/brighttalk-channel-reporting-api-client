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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.common.base.Preconditions;

/**
 * Formats (prints) and parses date/times to and from the format expected by the reporting APIs.
 * <p>
 * Dates and times exchanged with the reporting APIs are always in the UTC time zone and are formatted using a
 * simplified subset of the ISO 8601 standard. See patterns {@link #API_DATE_TIME_PATTERN} and
 * {@link #API_DATE_ONLY_PATTERN}.
 * <p>
 * Thread-safe.
 * 
 * @author Neil Brown
 */
public final class ApiDateTimeFormatter {

  /**
   * String pattern used to format and parse full, combined date and times exchanged with the reporting APIs
   * <p>
   * The pattern is a subset ('profile') of the ISO 8601 standard date/time format, comprising a combined date and time,
   * with per second resolution (excludes milliseconds) using the 'extended' format (with hyphens and colon delimiters),
   * with all component fields being mandatory, and a time zone format string of 'Z' rather than an offset to indicate
   * UTC.
   * <p>
   * This format is compatible with the profile of ISO 8601 supported by the XML Schema (XSD) dateTime type, namely the
   * extended format with all fields being mandatory
   * <p>
   * An example date printed using this format: {@code 2014-04-29T23:00:30Z}.
   */
  protected static final String API_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  private static final int API_DATE_TIME_PATTERN_LITERAL_QUOTE_COUNT = 4;

  /**
   * String pattern used to format and parse dates only (no times) exchanged with from the reporting APIs.
   * <p>
   * The pattern is a subset ('profile') of the ISO 8601 standard date/time format, comprising a date (only), in the
   * 'extended' format (with hyphens).
   * <p>
   * An example date printed using this format: {@code 2014-04-29}.
   */
  protected static final String API_DATE_ONLY_PATTERN = "yyyy-MM-dd";

  /**
   * Thread-safe {@link DateFormat} for efficient formatting and parsing combined date and times using the
   * {@link API_DATE_TIME_PATTERN}, implemented as a ThreadLocal variable.
   */
  private static ThreadLocal<DateFormat> threadLocalDateTimeFormat = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
      DateFormat df = new SimpleDateFormat(API_DATE_TIME_PATTERN);
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      return df;
    }
  };

  /**
   * Thread-safe {@link DateFormat} for efficient formatting and parsing dates (only) using the
   * {@link API_DATE_ONLY_PATTERN}, implemented as a ThreadLocal variable.
   */
  private static ThreadLocal<DateFormat> threadLocalDateOnlyFormat = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
      DateFormat df = new SimpleDateFormat(API_DATE_ONLY_PATTERN);
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      return df;
    }
  };

  /**
   * Formats a supplied Java {@link java.util.Date}, assumed to be in the UTC time zone, in the ISO 8601 date/time
   * format expected by the reporting APIs.
   * 
   * @param date The {@link Date} to format.
   * @return The formatted date/time string.
   */
  public String formatAsDateTime(Date date) {
    Preconditions.checkNotNull(date);
    return threadLocalDateTimeFormat.get().format(date);
  }
  
  /**
   * Formats a supplied Java {@link java.util.Date}, assumed to be in the UTC time zone, in the ISO 8601 date only 
   * format expected by the reporting APIs.
   *  
   * @param date The {@link Date} to format.
   * @return The formatted date string.
   */
  public String formatAsDateOnly(Date date) {
    Preconditions.checkNotNull(date);
    return threadLocalDateOnlyFormat.get().format(date);    
  }

  /**
   * Parses a supplied string in the ISO 8601 date/time or date only format used by the reporting APIs and returns the
   * resulting {@link java.util.Date}.
   * 
   * @param value The date/time string to parse.
   * @return The {@link Date} parsed from the string.
   */
  public Date parse(String value) {
    Preconditions.checkNotNull(value);
    if (value.length() == API_DATE_TIME_PATTERN.length() - API_DATE_TIME_PATTERN_LITERAL_QUOTE_COUNT) {
      try {             
        return threadLocalDateTimeFormat.get().parse(value);
      } catch (ParseException e) {
        throw new IllegalArgumentException("Failed to parse value as API date/time [" + value + "].", e);
      }        
    } else if (value.length() == API_DATE_ONLY_PATTERN.length()) {
      try {
        return threadLocalDateOnlyFormat.get().parse(value);
      } catch (ParseException e) {
        throw new IllegalArgumentException("Failed to parse value as API date [" + value + "].", e);
      }
    } else {
      throw new IllegalArgumentException("Failed to parse value as API date/time string [" + value + "].");
    }
  }
}