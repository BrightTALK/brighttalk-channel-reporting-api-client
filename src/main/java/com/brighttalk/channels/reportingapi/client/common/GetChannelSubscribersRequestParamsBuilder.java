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
 * Builds a map representation of the request parameters supported by the Get Channel Subscribers API from supplied
 * values. Also defines the set of named request parameters supported by the API.
 * 
 * @author Neil Brown
 */
public class GetChannelSubscribersRequestParamsBuilder {

  /* CHECKSTYLE:OFF */
  enum ParamName {
    SUBSCRIBED("subscribed"), SUBSCRIBED_SINCE("subscribedSince"), UNSUBSCRIBED_SINCE("unsubscribedSince");

    private String name;

    ParamName(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }
  /* CHECKSTYLE:ON */

  // Multimap API uses flattened collection of key/value pairs, with multiple entries for multiple values with same key
  // Multimap.asMap() is subsequently used to convert this to a Map<String, Collection<String>> representation.
  // Use LinkedListMultimap to get reliable (insert) order for keys as well as values  
  private LinkedListMultimap<String, String> params = LinkedListMultimap.create();

  private ApiDateTimeFormatter dateFormat = new ApiDateTimeFormatter();

  /**
   * Builds the request parameters using the data from the supplied parameters.
   * 
   * @param subscribed Optionally filters the results according to the Subscriber’s current subscription status.
   * {@code true} to to include only those that are subscribed. {@code false} to include only those that are
   * unsubscribed. If not supplied all Subscribers are returned, irrespective of their current subscription status.
   * @param subscribedSince Optionally filters the results to include only those Subscribers who subscribed after
   * (exclusive) the specified UTC date / time. (May, depending on the ‘subscribed’ param, include Subscribers who have
   * since unsubscribed).
   * @param unsubscribedSince Optionally filters the results to include only those Subscribers who <em>un</em>subscribed
   * after (exclusive) the specified UTC date / time. Implies setting the 'subscribed' param to “false”.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   */
  public GetChannelSubscribersRequestParamsBuilder(Boolean subscribed, Date subscribedSince, Date unsubscribedSince,
      PageCriteria pageCriteria) {
    if (subscribed != null) {
      this.params.put(ParamName.SUBSCRIBED.getName(), subscribed.toString());
    }
    if (subscribedSince != null) {
      this.params.put(ParamName.SUBSCRIBED_SINCE.getName(), this.dateFormat.formatAsDateTime(subscribedSince));
    }
    if (unsubscribedSince != null) {
      this.params.put(ParamName.UNSUBSCRIBED_SINCE.getName(), this.dateFormat.formatAsDateTime(unsubscribedSince));
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