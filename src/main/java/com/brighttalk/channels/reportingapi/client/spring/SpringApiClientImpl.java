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
package com.brighttalk.channels.reportingapi.client.spring;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.brighttalk.channels.reportingapi.client.ApiClient;
import com.brighttalk.channels.reportingapi.client.ApiClientException;
import com.brighttalk.channels.reportingapi.client.PageCriteria;
import com.brighttalk.channels.reportingapi.client.common.GetChannelSubscribersRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.client.common.GetSubscribersWebcastActivityRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.client.common.GetSurveyResponsesRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.client.common.GetWebcastRegistrationsRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.client.common.GetWebcastViewingsRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.client.common.GetWebcastsRequestParamsBuilder;
import com.brighttalk.channels.reportingapi.client.common.PagingRequestParamsBuilder;
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
import com.google.common.base.Preconditions;

/**
 * An {@link ApiClient} implementation that delegates to a pre-configured instance of the Spring framework's
 * {@link RestTemplate} to make the authenticated, synchronous API calls.
 * <p>
 * The {@link RestTemplate} provides management of the underlying HTTP connection, binding of URL variables, marshalling
 * / unmarshalling HTTP request and response bodies, and error handling (adapting HTTP status codes to exceptions).
 * <p>
 * Diagnostic logging of request and responses is provided by the RestTemplate and its configured
 * {@link org.springframework.http.client.ClientHttpRequest} implementation.
 * <p>
 * Thread safe.
 * 
 * @author Neil Brown
 */
public class SpringApiClientImpl implements ApiClient {

  private static final Logger logger = LoggerFactory.getLogger(SpringApiClientImpl.class);

  private static final String PROTOCOL_HTTP = "http";
  private static final String PROTOCOL_HTTPS = "https";
  private static final int HTTP_DEFAULT_PORT = 80;
  private static final int HTTPS_DEFAULT_PORT = 443;
  /** Compiled regex for a valid host name. See http://en.wikipedia.org/wiki/Hostname#Restrictions_on_valid_host_names */
  private static final Pattern VALID_HOST_NAME_PATTERN =
      Pattern.compile("[a-zA-Z0-9\\.\\-]{4,253}", Pattern.CASE_INSENSITIVE);

  private final String apiServiceProtocol;
  private final String apiServiceHostName;
  private final int apiServicePort;
  private final URI apiServiceBaseUri;
  private final RestTemplate restTemplate;

  /**
   * Creates an instance of the API client that communicates with an identified API service, using the default protocol
   * and port number.
   * 
   * @param apiServiceHostName The host name of the BrightTALK API service. A fully qualified domain name.
   * @param restTemplate The Spring {@link RestTemplate} this API client should use to make HTTP requests and process
   * the resulting HTTP response. The object must be fully configured with a connection factory supporting the required
   * API authentication and marshalling of all supported API resources to/from HTTP request and response bodies.
   * @see #SpringApiClientImpl(String, String, Integer, RestTemplate)
   */
  public SpringApiClientImpl(String apiServiceHostName, RestTemplate restTemplate) {
    this(null, apiServiceHostName, null, restTemplate);
  }

  /**
   * Creates an instance of the API client that communicates with a specified API service host.
   * 
   * @param apiServiceProtocol The protocol used to communicate with the BrightTALK API service. One of "http" or
   * "https". Optional. If null defaults to "https".
   * @param apiServiceHostName The host name of the BrightTALK API service. A fully qualified domain name.
   * @param apiServicePort The port of the BrightTALK API service. Optional. If null defaults to 80 or 443 depending on
   * {@code apiServiceProtocol}.
   * @param restTemplate The Spring {@link RestTemplate} this API client should use to make HTTP requests and process
   * the resulting HTTP response. The object must be fully configured with a connection factory supporting the required
   * API authentication and marshalling of all supported API resources to/from HTTP request and response bodies.
   */
  public SpringApiClientImpl(String apiServiceProtocol, String apiServiceHostName, Integer apiServicePort,
      RestTemplate restTemplate) {
    if (apiServiceProtocol == null) {
      apiServiceProtocol = PROTOCOL_HTTPS;
    }
    Preconditions.checkArgument(
        PROTOCOL_HTTP.equalsIgnoreCase(apiServiceProtocol) || PROTOCOL_HTTPS.equalsIgnoreCase(apiServiceProtocol),
        "API service protocol must be one or '%s' or '%s', not [%s]", PROTOCOL_HTTP, PROTOCOL_HTTPS, apiServiceProtocol);
    this.apiServiceProtocol = apiServiceProtocol.toLowerCase();

    Preconditions.checkNotNull(apiServiceHostName, "API service host name must not be null.");
    Preconditions.checkArgument(VALID_HOST_NAME_PATTERN.matcher(apiServiceHostName).matches(),
        "Invalid API service host name [%s].", apiServiceHostName);
    this.apiServiceHostName = apiServiceHostName;

    if (apiServicePort == null) {
      apiServicePort = PROTOCOL_HTTP.equalsIgnoreCase(apiServiceProtocol) ? HTTP_DEFAULT_PORT : HTTPS_DEFAULT_PORT;
    }
    Preconditions.checkArgument(apiServicePort > 0, "API service port must be a positive number, not [%s]",
        apiServicePort);
    this.apiServicePort = apiServicePort;

    this.restTemplate = Preconditions.checkNotNull(restTemplate, "RestTemplate must not be null.");

    this.apiServiceBaseUri =
        initApiServiceBaseUri(this.apiServiceProtocol, this.apiServiceHostName, this.apiServicePort);
  }

  /** {@inheritDoc} */
  @Override
  public ChannelsResource getMyChannels(PageCriteria pageCriteria) throws ApiClientException {
    logger.debug("Requesting My Channels with page criteria [{}].", pageCriteria);
    Map<String, List<String>> requestParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE, requestParams);
    ChannelsResource channels = this.restTemplate.getForObject(absResourceUrlTemplate, ChannelsResource.class);
    logger.debug("Got My Channels [{}].", channels);
    return channels;
  }

  /** {@inheritDoc} */
  @Override
  public ChannelsResource getUserChannels(int userId, PageCriteria pageCriteria) throws ApiClientException {
    logger.debug("Requesting User Channels for user [{}] with page criteria [{}].", userId, pageCriteria);
    Map<String, List<String>> requestParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        ChannelsResource.USER_CHANNELS_RELATIVE_URI_TEMPLATE, requestParams);
    ChannelsResource channels = this.restTemplate.getForObject(absResourceUrlTemplate, ChannelsResource.class, userId);
    logger.debug("Got User Channels [{}].", channels);
    return channels;
  }

  /** {@inheritDoc} */
  @Override
  public ChannelSubscribersResource getChannelSubscribers(int channelId, Boolean subscribed, Date subscribedSince,
      Date unsubscribedSince, PageCriteria pageCriteria) throws ApiClientException {
    logger.debug("Requesting Channel Subscribers for channel [{}] with page criteria [{}].", channelId, pageCriteria);
    Map<String, List<String>> requestParams = new GetChannelSubscribersRequestParamsBuilder(subscribed,
        subscribedSince, unsubscribedSince, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        ChannelSubscribersResource.RELATIVE_URI_TEMPLATE, requestParams);
    ChannelSubscribersResource subscribers =
        this.restTemplate.getForObject(absResourceUrlTemplate, ChannelSubscribersResource.class, channelId);
    logger.debug("Got Channel Subscribers [{}].", subscribers);
    return subscribers;
  }

  /** {@inheritDoc} */
  @Override
  public SubscribersWebcastActivityResource getSubscribersWebcastActivityForChannel(int channelId, Date since,
      Boolean expandChannelSurveyResponse, PageCriteria pageCriteria) throws ApiClientException {
    logger.debug("Requesting Subscribers Webcast Activity for channel [{}] with page criteria [{}].", channelId,
        pageCriteria);
    Map<String, List<String>> requestParams = new GetSubscribersWebcastActivityRequestParamsBuilder(since,
        expandChannelSurveyResponse, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        SubscribersWebcastActivityResource.FOR_CHANNEL_RELATIVE_URI_TEMPLATE, requestParams);
    SubscribersWebcastActivityResource subscribersWebcastActivity =
        this.restTemplate.getForObject(absResourceUrlTemplate, SubscribersWebcastActivityResource.class, channelId);
    logger.debug("Got Subscribers Webcast Activity [{}].", subscribersWebcastActivity);
    return subscribersWebcastActivity;
  }

  /** {@inheritDoc} */
  @Override
  public SubscribersWebcastActivityResource getSubscribersWebcastActivityForWebcast(int channelId, int webcastId,
      Date since, Boolean expandChannelSurveyResponse, PageCriteria pageCriteria) throws ApiClientException {
    logger.debug("Requesting Subscribers Webcast Activity for channel [{}], webcast [{}] with page criteria [{}].",
        channelId, webcastId, pageCriteria);
    Map<String, List<String>> requestParams = new GetSubscribersWebcastActivityRequestParamsBuilder(since,
        expandChannelSurveyResponse, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        SubscribersWebcastActivityResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE, requestParams);
    SubscribersWebcastActivityResource subscribersWebcastActivity =
        this.restTemplate.getForObject(absResourceUrlTemplate, SubscribersWebcastActivityResource.class, channelId,
            webcastId);
    logger.debug("Got Subscribers Webcast Activity [{}].", subscribersWebcastActivity);
    return subscribersWebcastActivity;
  }

  /** {@inheritDoc} */
  @Override
  public SurveysResource getSurveysForChannel(int channelId) throws ApiClientException {
    logger.debug("Requesting Surveys for channel [{}].", channelId);
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        SurveysResource.FOR_CHANNELS_RELATIVE_URI_TEMPLATE, null);
    SurveysResource surveys = this.restTemplate.getForObject(absResourceUrlTemplate, SurveysResource.class, channelId);
    logger.debug("Got Surveys [{}].", surveys);
    return surveys;
  }

  /** {@inheritDoc} */
  @Override
  public SurveyResource getSurvey(int surveyId) throws ApiClientException {
    logger.debug("Requesting Survey [{}].", surveyId);
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri, SurveyResource.RELATIVE_URI_TEMPLATE,
        null);
    SurveyResource survey = this.restTemplate.getForObject(absResourceUrlTemplate, SurveyResource.class, surveyId);
    logger.debug("Got Survey [{}].", survey);
    return survey;
  }

  /** {@inheritDoc} */
  @Override
  public SurveyResponsesResource getSurveyResponses(int surveyId, Date since, PageCriteria pageCriteria)
      throws ApiClientException {
    logger.debug("Requesting Survey Responses for survey [{}] with page criteria [{}].", surveyId, pageCriteria);
    Map<String, List<String>> requestParams = new GetSurveyResponsesRequestParamsBuilder(since, pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        SurveyResponsesResource.RELATIVE_URI_TEMPLATE, requestParams);
    SurveyResponsesResource surveyResponses =
        this.restTemplate.getForObject(absResourceUrlTemplate, SurveyResponsesResource.class, surveyId);
    logger.debug("Got Survey Responses [{}].", surveyResponses);
    return surveyResponses;
  }

  /** {@inheritDoc} */
  @Override
  public WebcastsResource getWebcastsForChannel(int channelId, Date since, PageCriteria pageCriteria)
      throws ApiClientException {
    logger.debug("Requesting Webcasts for channel [{}] with page criteria [{}].", channelId, pageCriteria);
    Map<String, List<String>> requestParams = new GetWebcastsRequestParamsBuilder(since, pageCriteria).asMap();
    String absResourceUrlTemplate =
        buildAbsoluteHttpUrl(this.apiServiceBaseUri, WebcastsResource.RELATIVE_URI_TEMPLATE, requestParams);
    WebcastsResource webcasts =
        this.restTemplate.getForObject(absResourceUrlTemplate, WebcastsResource.class, channelId);
    logger.debug("Got Webcasts [{}].", webcasts);
    return webcasts;
  }

  /** {@inheritDoc} */
  @Override
  public WebcastResource getWebcast(int channelId, int webcastId) throws ApiClientException {
    logger.debug("Requesting Webcast [{}] for channel [{}].", webcastId, channelId);
    String absResourceUrlTemplate =
        buildAbsoluteHttpUrl(this.apiServiceBaseUri, WebcastResource.RELATIVE_URI_TEMPLATE, null);
    WebcastResource webcast =
        this.restTemplate.getForObject(absResourceUrlTemplate, WebcastResource.class, channelId, webcastId);
    logger.debug("Got Webcast [{}].", webcast);
    return webcast;
  }

  /** {@inheritDoc} */
  @Override
  public WebcastRegistrationsResource getWebcastRegistrationsForWebcast(int channelId, int webcastId, Date since,
      Boolean viewed, PageCriteria pageCriteria) throws ApiClientException {
    logger.debug("Requesting Webcast Registrations for channel [{}], webcast [{}] with page criteria [{}].", channelId,
        webcastId, pageCriteria);
    Map<String, List<String>> requestParams = new GetWebcastRegistrationsRequestParamsBuilder(since, viewed,
        pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        WebcastRegistrationsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE, requestParams);
    WebcastRegistrationsResource webcastRegistrations =
        this.restTemplate.getForObject(absResourceUrlTemplate, WebcastRegistrationsResource.class, channelId, webcastId);
    logger.debug("Got Webcast Registrations [{}].", webcastRegistrations);
    return webcastRegistrations;
  }

  /** {@inheritDoc} */
  @Override
  public WebcastViewingsResource getWebcastViewingsForChannel(int channelId, Date since, WebcastStatus webcastStatus,
      PageCriteria pageCriteria) throws ApiClientException {
    logger.debug("Requesting Webcast Viewings for channel [{}] with page criteria [{}].", channelId, pageCriteria);
    Map<String, List<String>> requestParams = new GetWebcastViewingsRequestParamsBuilder(since, webcastStatus,
        pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        WebcastViewingsResource.FOR_CHANNEL_RELATIVE_URI_TEMPLATE, requestParams);
    WebcastViewingsResource webcastViewings =
        this.restTemplate.getForObject(absResourceUrlTemplate, WebcastViewingsResource.class, channelId);
    logger.debug("Got Webcast Viewings [{}].", webcastViewings);
    return webcastViewings;
  }

  /** {@inheritDoc} */
  @Override
  public WebcastViewingsResource getWebcastViewingsForWebcast(int channelId, int webcastId, Date since,
      WebcastStatus webcastStatus, PageCriteria pageCriteria) throws ApiClientException {
    logger.debug("Requesting Webcast Viewings for channel [{}], webcast [{}] with page criteria [{}].", channelId,
        webcastId, pageCriteria);
    Map<String, List<String>> requestParams = new GetWebcastViewingsRequestParamsBuilder(since, webcastStatus,
        pageCriteria).asMap();
    String absResourceUrlTemplate = buildAbsoluteHttpUrl(this.apiServiceBaseUri,
        WebcastViewingsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE, requestParams);
    WebcastViewingsResource webcastViewings =
        this.restTemplate.getForObject(absResourceUrlTemplate, WebcastViewingsResource.class, channelId, webcastId);
    logger.debug("Got Webcast Viewings [{}].", webcastViewings);
    return webcastViewings;
  }

  /**
   * @return the apiServiceProtocol
   */
  public final String getApiServiceProtocol() {
    return this.apiServiceProtocol;
  }

  /**
   * @return the apiServiceHostName
   */
  public final String getApiServiceHostName() {
    return this.apiServiceHostName;
  }

  /**
   * @return the apiServicePort
   */
  public final int getApiServicePort() {
    return this.apiServicePort;
  }

  /**
   * @return The base (protocol, host name and optional port) {@link URI} of the API service which this client is
   * currently configured to use. This is an environment specific value.
   */
  public final URI getApiServiceBaseUri() {
    try {
      // Return a new URI to preserve immutability
      return new URI(this.apiServiceBaseUri.toString());
    } catch (URISyntaxException e) {
      throw new ApiClientException(e);
    }
  }

  private static URI initApiServiceBaseUri(String protocol, String hostName, int port) {
    try {
      return new URI(protocol + "://" + hostName + ":" + port);
    } catch (URISyntaxException e) {
      throw new ApiClientException(e);
    }
  }

  /**
   * Builds an absolute HTTP URL from a supplied base URI, a relative path an an optional map of request parameters.
   * <p>
   * Supports building URLs before URL template variables have been expanded - template variable placeholders ({...})
   * will _not_ be encoded.
   * 
   * @param baseUri The {@link URL base URI}.
   * @param relativeUrlPath The relative path to be appended to the base URI.
   * @param requestParams An optional, map representation of request parameters to be appended to the URL. Can be null.
   * @return A String representation of the absolute URL. A return type of String rather than {@link java.net.URI} is
   * used to avoid encoding the URL before any template variables have been replaced.
   */
  private static String buildAbsoluteHttpUrl(URI baseUri, String relativeUrlPath,
      Map<String, List<String>> requestParams) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(baseUri);
    uriBuilder.path(relativeUrlPath);
    if (requestParams != null) {
      for (String paramName : requestParams.keySet()) {
        for (String paramValue : requestParams.get(paramName)) {
          uriBuilder.queryParam(paramName, paramValue);
        }
      }
    }
    UriComponents uriComponents = uriBuilder.build();
    return uriComponents.toUriString();
  }
}