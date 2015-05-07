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
package com.brighttalk.channels.reportingapi.client.support;

/**
 * The enumeration of standard ('registered') and custom (aka 'extended') link relation types that the API uses in 
 * resource links.
 * <p>
 * Use {@link #name()} on enum members as the source of the value of the 'rel' parameter/attribute of links e.g. 
 * {@code RelationType.next.name()} for "next" in {@code <link rel="next" href="..."/>}.
 */
public enum LinkRelationType {
  /** Designates a link to the next resource or collection of resources. Supports paging resources. */
  next,

  /** Designates a link to the current resource. */
  self,

  /** Designates a link to a Channel Subscriber. */
  channel_subscriber,

  /** Designates a link to a collection of Channel Subscribers. */
  channel_subscribers,

  /** Designates a link to a collection of Subscriber Webcast Activity for one or more subscriber. */
  subscribers_webcast_activity,

  /** Designates a link to a Survey. */
  survey,

  /** Designates a link to a collection of Surveys. */
  surveys,

  /** Designates a link to a collection of Survey Responses. */
  survey_responses,

  /** Designates a link to a webcast resource. */
  webcast,

  /** Designates a link to a collection of webcast resources. */
  webcasts,

  /** Designates a link to a collection of Viewings. */
  webcast_viewings,

  /** Designates a link to a collection of Registrations. */
  webcast_registrations;
}