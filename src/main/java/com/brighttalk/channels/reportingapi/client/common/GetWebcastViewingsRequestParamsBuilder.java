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

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.brighttalk.channels.reportingapi.client.PageCriteria;
import com.brighttalk.channels.reportingapi.client.resource.WebcastStatus;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimaps;

/**
 * Builds a map representation of the request parameters supported by the Get Webcast Viewings APIs from supplied
 * values. Also defines the set of named request parameters supported by those APIs.
 * 
 * @author Neil Brown
 */
public class GetWebcastViewingsRequestParamsBuilder {

  /* CHECKSTYLE:OFF */
  enum ParamName {
    SINCE("since"),

    WEBCAST_STATUS("webcastStatus");

    private String name;

    ParamName(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

  /** The supported subset of {@link WebcastStatus} which viewings can be filtered by. */
  private static EnumSet<WebcastStatus> VIEWING_WEBCAST_STATUS = EnumSet.of(WebcastStatus.LIVE, WebcastStatus.RECORDED);

  /* CHECKSTYLE:ON */

  // Multimap API uses flattened collection of key/value pairs, with multiple entries for multiple values with same key
  // Multimap.asMap() is subsequently used to convert this to a Map<String, Collection<String>> representation.
  // Use LinkedListMultimap to get reliable (insert) order for keys as well as values
  private LinkedListMultimap<String, String> params = LinkedListMultimap.create();

  private ApiDateTimeFormatter dateFormat = new ApiDateTimeFormatter();

  /**
   * Builds the request parameters using the data from the supplied parameters.
   * 
   * @param since Optionally filters the results to include only those viewings (created or) updated after (exclusive)
   * the specified UTC date / time.
   * @param webcastStatus Optionally filters the results according to the status of the webcast at the time the viewing
   * took place. Must be one of supported status {@link WebcastStatus#LIVE} or {@link WebcastStatus#RECORDED}.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @throws IllegalArgumentException If {@code webcastStatus} is not one of the status supported by this API.
   */
  public GetWebcastViewingsRequestParamsBuilder(Date since, WebcastStatus webcastStatus, PageCriteria pageCriteria) {
    if (since != null) {
      this.params.put(ParamName.SINCE.getName(), this.dateFormat.formatAsDateTime(since));
    }
    if (webcastStatus != null) {
      if (VIEWING_WEBCAST_STATUS.contains(webcastStatus)) {
        this.params.put(ParamName.WEBCAST_STATUS.getName(), webcastStatus.toString());
      } else {
        throw new IllegalArgumentException("Invalid webcast status [" + webcastStatus
            + "]. Webcast status must be one of " + VIEWING_WEBCAST_STATUS + ".");
      }
    }
    if (pageCriteria != null) {
      Map<String, List<String>> pagingParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
      for (String paramName : pagingParams.keySet()) {
        this.params.putAll(paramName, pagingParams.get(paramName));
      }
    }
  }

  /**
   * @return A {@code Map<String, List<String>>} representation of the request parameter names and their values.
   */
  public Map<String, List<String>> asMap() {
    // Multimap's asMap() methods convert
    return Multimaps.asMap(this.params);
  }
}