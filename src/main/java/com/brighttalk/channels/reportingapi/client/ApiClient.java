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
package com.brighttalk.channels.reportingapi.client;

import java.util.Date;

import com.brighttalk.channels.reportingapi.client.resource.ChannelSubscribersResource;
import com.brighttalk.channels.reportingapi.client.resource.ChannelsResource;
import com.brighttalk.channels.reportingapi.client.resource.SubscribersWebcastActivityResource;
import com.brighttalk.channels.reportingapi.client.resource.SurveyResource;
import com.brighttalk.channels.reportingapi.client.resource.SurveyResponsesResource;
import com.brighttalk.channels.reportingapi.client.resource.SurveysResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastRegistrationsResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastStatus;
import com.brighttalk.channels.reportingapi.client.resource.WebcastViewingsResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastsResource;

/**
 * A client for accessing the BrightTALK channel owner reporting API (version 1.x).
 * <p>
 * TODO @see <a href="...">BrightTALK Channel Owner Reporting APIs V1 - Technical specification</a>
 * 
 * @author Neil Brown
 */
public interface ApiClient {

  /**
   * Retrieves the channels owned by the current API user.
   * 
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link ChannelsResource} containing a collection of channels owned by the current user.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  ChannelsResource getMyChannels(PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves the channels owned by an identified BrightTALK user.
   * <p>
   * The API credentials used must be authorised to access the identified user's channels.
   * 
   * @param userId The ID of the BrightTALK user.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link ChannelsResource} containing a collection of channels owned by the identified user.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  ChannelsResource getUserChannels(int userId, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves the list of current and/or past subscribers to a channel owned by an API user.
   * 
   * @param channelId The ID of the channel.
   * @param subscribed Optionally filters the results according to the subscriber’s current subscription status.
   * {@code true} to to include only those that are subscribed. {@code false} to include only those that are
   * unsubscribed. If not supplied all subscribers are returned, irrespective of their current subscription status.
   * @param subscribedSince Optionally filters the results to include only those subscribers who subscribed after
   * (exclusive) the specified UTC date / time. Supports incremental reporting of new and updated susbcribers. (May,
   * depending on the ‘subscribed’ param, include Subscribers who have since unsubscribed).
   * @param unsubscribedSince Optionally filters the results to include only those subscribers who <em>un</em>subscribed
   * after (exclusive) the specified UTC date / time. Implies setting the {@code subscribed} param to “false”.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link ChannelSubscribersResource} containing a collection of zero or more subscribers to the channel.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  ChannelSubscribersResource getChannelSubscribers(int channelId, Boolean subscribed, Date subscribedSince,
      Date unsubscribedSince, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves a summary (aggregation) of per subscriber activity for webcasts in one of the channel owner's channels.
   * 
   * @param channelId The ID of the channel.
   * @param since Optionally filters the results to include only those Subscriber Webcast Activity (created or) updated
   * after (exclusive) the specified UTC date / time.
   * @param expandChannelSurveyResponse Optionally expands every child Subscriber Webcast Activity to include the
   * subscriber’s response to the channel’s survey, if there is one. Defaults to {@code false}.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link SubscribersWebcastActivityResource} containing a collection of zero or more activities.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SubscribersWebcastActivityResource getSubscribersWebcastActivityForChannel(int channelId, Date since,
      Boolean expandChannelSurveyResponse, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves a summary (aggregation) of per subscriber activity for a specific webcast in one of the channel owner's
   * channels.
   * 
   * @param channelId The ID of the channel.
   * @param webcastId The ID of the webcast.
   * @param since Optionally filters the results to include only those Subscriber Webcast Activity (created or) updated
   * after (exclusive) the specified UTC date / time.
   * @param expandChannelSurveyResponse Optionally expands every child Subscriber Webcast Activity to include the
   * subscriber’s response to the channel’s survey, if there is one. Defaults to {@code false}.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link SubscribersWebcastActivityResource} containing a collection of zero or more activities.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SubscribersWebcastActivityResource getSubscribersWebcastActivityForWebcast(int channelId, int webcastId, Date since,
      Boolean expandChannelSurveyResponse, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves the survey (definitions) for a channel owned by the current user.
   * <p>
   * Supports a Channel owner obtaining a link for subsequently retrieving the responses to a channel survey.
   * 
   * @param channelId The ID of the channel.
   * @return A {@link SurveysResource} containing a collection of zero or more {@link SurveyResource surveys}.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SurveysResource getSurveysForChannel(int channelId) throws ApiClientException;

  /**
   * Retrieves an identified survey (definition) owned by the current user.
   * 
   * @param surveyId The ID of the survey.
   * @return The {@link SurveyResource}.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SurveyResource getSurvey(int surveyId) throws ApiClientException;

  /**
   * Retrieves all of the responses to an identified survey owned by the current user.
   * 
   * @param surveyId The ID of the survey.
   * @param since Optionally filters the results to include only those survey responses (created or) updated after
   * (exclusive) the specified UTC date / time.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link SurveyResponsesResource} containing a collection of zero or more
   * {@link com.brighttalk.channels.reportingapi.client.resource.SurveyResponseResource survey response}.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SurveyResponsesResource getSurveyResponses(int surveyId, Date since, PageCriteria pageCriteria)
      throws ApiClientException;

/**
   * Retrieves the webcasts in an identified channel owned by the current user.
   *  
   * @param channelId The ID of the channel.
   * @param since Optionally filters the results to include only those webcasts (created or) updated after (exclusive) 
   * the specified UTC date / time.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link WebcastsResource} containing a collection of zero or more {@link 
   * com.brighttalk.channels.reportingapi.client.resource.WebcastResource webcasts}.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  WebcastsResource getWebcastsForChannel(int channelId, Date since, PageCriteria pageCriteria)
      throws ApiClientException;

  /**
   * Retrieves the full details of an identified webcast, in a channel owned by the current user.
   * 
   * @param channelId The ID of the channel.
   * @param webcastId The ID of the webcast.
   * @return The {@link WebcastResource webcast}.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  WebcastResource getWebcast(int channelId, int webcastId) throws ApiClientException;

  /**
   * Retrieves all of the registrations for an identified webcast in a channel owned by the current user.
   * 
   * @param channelId The ID of the channel.
   * @param webcastId The ID of the webcast.
   * @param since Optionally filters the results to include only those registrations (created or) updated after
   * (exclusive) the specified UTC date / time.
   * @param viewed Optionally filters the results according to whether the registrant has subsequently viewed the 
   * webcast in any status (live or recorded). Supports identification of “no shows” - subscribers that registered but 
   * have not yet viewed.
   * @param pageCriteria Optional {@link PageCriteria page criteria}. 
   * @return A {@link WebcastRegistrationsResource} containing a collection of zero or more {@link 
   * com.brighttalk.channels.reportingapi.client.resource.WebcastRegistrationResource webcast registration}.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  WebcastRegistrationsResource getWebcastRegistrationsForWebcast(int channelId, int webcastId, Date since,
      Boolean viewed, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves the viewings that have taken place for all webcasts in a channel owned by the current user.
   * 
   * @param channelId The ID of the channel.
   * @param since Optionally filters the results to include only those viewings (created or) updated after
   * (exclusive) the specified UTC date / time.
   * @param webcastStatus Optionally filters the results according to the status of the webcast at the time the viewing 
   * took place. Must be one of supported status {@link WebcastStatus#LIVE} or {@link WebcastStatus#RECORDED}.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.    
   * @return A {@link WebcastViewingsResource} containing a collection of zero or more {@link 
   * com.brighttalk.channels.reportingapi.client.resource.WebcastViewingResource webcast viewing}.
   * @throws IllegalArgumentException If {@code webcastStatus} is not one of the status supported by this API.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  WebcastViewingsResource getWebcastViewingsForChannel(int channelId, Date since, WebcastStatus webcastStatus,
      PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves the viewings that have taken place for an identified webcast in a channel owned by the current user.
   * 
   * @param channelId The ID of the channel.
   * @param webcastId The ID of the webcast. 
   * @param since Optionally filters the results to include only those vieiwngs (created or) updated after
   * (exclusive) the specified UTC date / time.
   * @param webcastStatus Optionally filters the results according to the status of the webcast at the time the viewing 
   * took place. Must be one of supported status {@link WebcastStatus#LIVE} or {@link WebcastStatus#RECORDED}.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.    
   * @return A {@link WebcastViewingsResource} containing a collection of zero or more {@link 
   * com.brighttalk.channels.reportingapi.client.resource.WebcastViewingResource webcast viewing}.
   * @throws IllegalArgumentException If {@code webcastStatus} is not one of the status supported by this API.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  WebcastViewingsResource getWebcastViewingsForWebcast(int channelId, int webcastId, Date since,
      WebcastStatus webcastStatus, PageCriteria pageCriteria) throws ApiClientException;
}