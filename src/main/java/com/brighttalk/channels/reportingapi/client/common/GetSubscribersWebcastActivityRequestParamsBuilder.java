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
import java.util.List;
import java.util.Map;

import com.brighttalk.channels.reportingapi.client.PageCriteria;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimaps;

/**
 * Builds a map representation of the request parameters supported by the Get Subscribers Webcast Activity APIs from 
 * supplied values. Also defines the set of named request parameters supported by the API.
 * 
 * @author Neil Brown
 */
public class GetSubscribersWebcastActivityRequestParamsBuilder {

  /* CHECKSTYLE:OFF */
  enum ParamName {
    SINCE("since"), EXPAND("expand");

    private String name;

    ParamName(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }
  
  private static final String EXPAND_CHANNEL_SURVEY_RESPONSE = "channelSurveyResponse";

  /* CHECKSTYLE:ON */

  // Multimap API uses flattened collection of key/value pairs, with multiple entries for multiple values with same key
  // Multimap.asMap() is subsequently used to convert this to a Map<String, Collection<String>> representation.
  // Use LinkedListMultimap to get reliable (insert) order for keys as well as values
  private LinkedListMultimap<String, String> params = LinkedListMultimap.create();

  private ApiDateTimeFormatter dateFormat = new ApiDateTimeFormatter();

  /**
   * Builds the request parameters using the data from the supplied parameters.
   * 
   * @param since Optionally filters the results to include only those Subscriber Webcast Activity (created or) updated
   * after (exclusive) the specified UTC date / time.
   * @param expandChannelSurveyResponse Optionally expands every child Subscriber Webcast Activity to include the 
   * subscriber’s response to the channel’s survey, if there is one. Defaults to {@code false}.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   */
  public GetSubscribersWebcastActivityRequestParamsBuilder(Date since, Boolean expandChannelSurveyResponse,
      PageCriteria pageCriteria) {
    if (since != null) {
      this.params.put(ParamName.SINCE.getName(), this.dateFormat.formatAsDateTime(since));
    }
    if (expandChannelSurveyResponse != null) {
      this.params.put(ParamName.EXPAND.getName(), EXPAND_CHANNEL_SURVEY_RESPONSE);
    }    
    if (pageCriteria != null) {
      Map<String, List<String>> pagingParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
      for (String paramName : pagingParams.keySet()) {
        this.params.putAll(paramName, pagingParams.get(paramName));
      }
    }
  }

  /**
   * @return A {@code Map<String, List<String>>} representation of thee request parameter names and their values.
   */
  public Map<String, List<String>> asMap() {
    // Multimap's asMap() methods convert
    return Multimaps.asMap(this.params);
  }
}