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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.brighttalk.channels.reportingapi.client.ApiErrorResponseException;
import com.brighttalk.channels.reportingapi.client.PageCriteria;
import com.brighttalk.channels.reportingapi.client.common.ApiDateTimeFormatter;
import com.brighttalk.channels.reportingapi.client.marshall.ChannelResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.ChannelSubscriberResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.ChannelSubscribersResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.ChannelsResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.EmbedXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.LinkXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.QuestionXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.SubscriberWebcastActivityResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.SubscribersWebcastActivityResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.SurveyResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.SurveyResponseResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.SurveyResponsesResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.SurveysResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.UserXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.WebcastRegistrationResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.WebcastRegistrationsResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.WebcastResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.WebcastViewingResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.WebcastViewingsResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.marshall.WebcastsResourceXStreamConverter;
import com.brighttalk.channels.reportingapi.client.resource.ApiError;
import com.brighttalk.channels.reportingapi.client.resource.ChannelResource;
import com.brighttalk.channels.reportingapi.client.resource.ChannelSubscriberResource;
import com.brighttalk.channels.reportingapi.client.resource.ChannelSubscribersResource;
import com.brighttalk.channels.reportingapi.client.resource.ChannelsResource;
import com.brighttalk.channels.reportingapi.client.resource.Embed;
import com.brighttalk.channels.reportingapi.client.resource.Link;
import com.brighttalk.channels.reportingapi.client.resource.Question;
import com.brighttalk.channels.reportingapi.client.resource.SubscriberWebcastActivityResource;
import com.brighttalk.channels.reportingapi.client.resource.SubscribersWebcastActivityResource;
import com.brighttalk.channels.reportingapi.client.resource.SurveyResource;
import com.brighttalk.channels.reportingapi.client.resource.SurveyResponseResource;
import com.brighttalk.channels.reportingapi.client.resource.SurveyResponsesResource;
import com.brighttalk.channels.reportingapi.client.resource.SurveysResource;
import com.brighttalk.channels.reportingapi.client.resource.User;
import com.brighttalk.channels.reportingapi.client.resource.WebcastRegistrationResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastRegistrationsResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastStatus;
import com.brighttalk.channels.reportingapi.client.resource.WebcastViewingResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastViewingsResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastsResource;
import com.brighttalk.channels.reportingapi.client.spring.AppConfig;
import com.brighttalk.channels.reportingapi.client.spring.SpringApiClientImpl;
import com.brighttalk.channels.reportingapi.client.support.LinkRelationType;
import com.brighttalk.channels.reportingapi.client.support.Links;
import com.thoughtworks.xstream.XStream;

/**
 * Integration tests for {@link SpringApiClientImpl}.
 * <p>
 * These tests load the application's Spring application context using the {@link SpringJUnit4ClassRunner}. In addition
 * to testing the Spring bean configuration, this tests the use of the production configured instance of the
 * RestTemplate.
 * <p>
 * The test case uses the Spring MVC Test framework’s {@link MockRestServiceServer} to provide a dynamically mocked
 * implementation of the BrightTALK Reporting API service, assert the expected API requests, and specify stubbed
 * responses. When using the MockRestServiceServer, the RestTemplate is exercised exactly as it would be if it were
 * making HTTP requests to the real API service. The test coverage provided by these tests therefore include marshalling
 * and unmarshalling the HTTP request and response bodies, exercising the API resource objects (DTOs) and their
 * configured marshallers and marshalling annotations in the process.
 * <p>
 * The MockRestServiceServer works by substituting the Spring {@link ClientHttpRequestFactory} for a mock
 * implementation. As a consequence, the coverage of these tests does NOT extend as far as testing the production HTTP
 * client and its configuration (e.g. support for basic auth and compression). This requires a separate set of,
 * (end-to-end integration or functional) tests.
 * 
 * @author Neil Brown
 * @see SpringApiClientImplStubbedApiServiceIntegrationTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
// Avoid reuse of injected RestTemplate in later tests as it's configured to use a MockClientHttpRequestFactory
@DirtiesContext
public class SpringApiClientImplIntegrationTest {

  private static final Logger logger = LoggerFactory.getLogger(SpringApiClientImplIntegrationTest.class);

  /** Instance of class under test */
  @Autowired
  private SpringApiClientImpl apiClient;

  /**
   * Instance of {@link RestTemplate} that both the instance of the {@link SpringApiClientImpl} class under test, and
   * the {@link #mockReportingApiService} are configured to use.
   */
  @Autowired
  @Qualifier("apiClientRestTemplate")
  private RestTemplate restTemplate;

  /**
   * Dynamic mock implementation of the BrightTALK Reporting API service, implemented using the Spring MVC Test
   * framework’s {@link MockRestServiceServer}.
   */
  private MockRestServiceServer mockReportingApiService;

  /** Instance of {@link XStream} used to unmarshall (deserialise) canned API response payloads used by tests. */
  private XStream xstream;

  /**
   * @throws Exception If an unexpected exception occurs.
   */
  @Before
  public void setUp() throws Exception {
    this.mockReportingApiService = MockRestServiceServer.createServer(this.restTemplate);
    this.initXStream();
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} in the case where HTTP response code 401 Unauthorized is returned.
   * This can occur if the API credentials that the API client is configured to use are deemed invalid by the API
   * service.
   */
  @Test
  public void getMyChannelsWhenAuthenticationFails() {
    String expectedRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withUnauthorizedRequest());

    try {
      this.apiClient.getMyChannels(null);
      fail("Expected exception to be thrown.");
    } catch (ApiErrorResponseException e) {
      assertThat(e.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    this.mockReportingApiService.verify();
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when the API user has zero channels.
   */
  @Test
  public void getMyChannelsWhenZeroChannels() {
    String expectedRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<channels/>", MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getMyChannels(null);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(0));
    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests an API call, such as {@link SpringApiClientImpl#getMyChannels}, when page criteria is supplied without a next
   * page link, i.e. it is criteria for the first page of resources which is only used to specificy a non-default page
   * size.
   */
  @Test
  public void getMyChannelsWhenPageCriteriaSuppliedForFirstPage() {
    int pageSize = 100;
    PageCriteria firstPageCriteria = new PageCriteria(pageSize);
    String expectedRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE + "?pageSize=" + pageSize;

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<channels/>", MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getMyChannels(firstPageCriteria);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(0));
    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when the first page of returned resources contains multiple
   * channels, and the total no. of channels is greater than the API page size, resulting in a 'next' page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getMyChannelsWhenMultipleChannelsWithNextPage() throws Exception {
    String expectedRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getMyChannelsMultipleChannelsWithNextPage-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getMyChannels(null);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    ChannelsResource expectedChannelsResource = (ChannelsResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelResource expectedChannel = expectedChannelsResource.getChannels().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(channelsResource.getChannels().get(0), is(expectedChannel));

    // For the second of the two returned Resource just check its not the same as the first
    assertThat(channelsResource.getChannels().get(0).getId(), not(channelsResource.getChannels().get(1).getId()));

    assertThat(channelsResource.getLinks(), hasSize(1));
    assertThat(channelsResource.getLinks().get(0), is(expectedChannelsResource.getLinks().get(0)));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} with paging criteria that includes a next page link and a
   * non-default page size. The API responds with the final (last) page containing a single Channel.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getMyChannelsWhenNextPageWithNonDefaultPageSizeReturnsLastPage() throws Exception {
    int pageSize = 50;
    String expectedRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE + "?cursor=1234&pageSize=" + pageSize;
    PageCriteria pageCriteria = createPageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getMyChannelsNextPageWithNonDefaultPageSizeReturnsLastPageSingleChannel-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getMyChannels(pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    ChannelsResource expectedChannelsResource = (ChannelsResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelResource expectedChannel = expectedChannelsResource.getChannels().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(channelsResource.getChannels().get(0), is(expectedChannel));

    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} in the case where the API service returns a response containing a
   * field value which cannot be converted to the expected type as defined in the API resource class. In this case,
   * return of a {@link ChannelResource} with a non-numeric identifier is tested.
   */
  @Test
  public void getMyChannelsInvalidResponseTypeMismatchChannelId() {
    String expectedRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getMyChannelsInvalidResponseTypeMismatchChannelId-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Default JAXB behaviour is to treat this as a non-fatal unmarshalling error, resulting in a default value field
    ChannelsResource channelsResource = this.apiClient.getMyChannels(null);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(1));
    assertThat(channelsResource.getChannels().get(0).getId(), is(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} in the case where the API service returns a response containing a
   * field value which cannot be converted to the expected type as defined in the API resource class. In this case,
   * return of a {@link ChannelResource} with created date/time string which can't be converted to date is tested.
   */
  @Test
  public void getMyChannelsInvalidResponseTypeMismatchChannelCreatedDate() {
    String expectedRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getMyChannelsInvalidResponseTypeMismatchChannelCreatedDate-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getMyChannels(null);

    // Default JAXB behaviour is to treat this as a non-fatal unmarshalling error, resulting in a null field
    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels().get(0), notNullValue());
    assertThat(channelsResource.getChannels().get(0).getCreated(), nullValue());
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} in the case where the API service returns an HTTP 500 Internal
   * Server error, as a result of an unexpected system error.
   */
  @Test
  public void getMyChannelsWhenInternalServerError() {
    String expectedRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withServerError());

    try {
      this.apiClient.getMyChannels(null);
      fail("Expected exception to be thrown.");
    } catch (ApiErrorResponseException e) {
      assertThat(e.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    this.mockReportingApiService.verify();
  }

  /**
   * Test {@link SpringApiClientImpl#getUserChannels(int, PageCriteria)} in the case where the identified user has zero
   * channels.
   */
  @Test
  public void getUserChanelsWhenZeroChannels() {
    int userId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelsResource.USER_CHANNELS_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(userId).toString();

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<channels/>", MediaType.APPLICATION_XML));

    // Perform the test
    ChannelsResource channelsResource = this.apiClient.getUserChannels(userId, null);

    this.mockReportingApiService.verify();
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(0));
    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} in the case where HTTP response code 403 Forbidden is returned.
   * This can occur if the requesting API user is not permitted to access the requested BrightTALK Channel.
   */
  @Test
  public void getChannelSubscribersWhenAuthorisationFails() {
    int channelId = 1;
    String expectedTemplateRequestUrl =
        this.apiClient.getApiServiceBaseUri() + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    ApiError apiError = new ApiError("NotAuthorisedForChannel", "Not authorised for channel [" + channelId + "].");
    String responseBody = apiErrorToXml(apiError);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withStatus(HttpStatus.FORBIDDEN).body(responseBody).contentType(MediaType.APPLICATION_XML));

    try {
      this.apiClient.getChannelSubscribers(channelId, null, null, null, null);
      fail("Expected exception to be thrown.");
    } catch (ApiErrorResponseException e) {
      assertThat(e.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
      assertThat(e.getApiError(), is(apiError));
    }

    this.mockReportingApiService.verify();
  }

  /**
   * Test {@link SpringApiClientImpl#getChannelSubscribers} in the case where there identified channel has zero
   * subscribers.
   */
  @Test
  public void getChannelSubscribersWhenZeroSubscribers() {
    int channelId = 1;
    Boolean subscribed = false;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE + "?subscribed=" + subscribed;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<channelSubscribers/>", MediaType.APPLICATION_XML));

    // Perform the test
    ChannelSubscribersResource subscribersResource = this.apiClient.getChannelSubscribers(channelId, subscribed, null,
        null, null);

    this.mockReportingApiService.verify();
    assertThat(subscribersResource, notNullValue());
    assertThat(subscribersResource.getChannelSubscribers(), hasSize(0));
    assertThat(subscribersResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when retrieving all current and past subscribers and the first page
   * of returned resources contains multiple subscribers, and the total no. of subscribers is greater than the API page
   * size, resulting in a 'next' page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getChannelSubscribersWhenMultipleCurrentAndPastSusbcribersWithNextPage() throws Exception {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getChannelSubscribersWhenMultipleCurrentAndPastSusbcribersWithNextPage-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelSubscribersResource subscribersResource = this.apiClient.getChannelSubscribers(channelId, null, null, null,
        null);

    this.mockReportingApiService.verify();
    assertThat(subscribersResource, notNullValue());
    assertThat(subscribersResource.getChannelSubscribers(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    ChannelSubscribersResource expectedSubscribersResource =
        (ChannelSubscribersResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelSubscriberResource expectedSubscriber = expectedSubscribersResource.getChannelSubscribers().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(subscribersResource.getChannelSubscribers().get(0), is(expectedSubscriber));

    // For the second of the two returned Resource just check its not the same as the first
    assertThat(subscribersResource.getChannelSubscribers().get(0).getId(),
        not(subscribersResource.getChannelSubscribers().get(1).getId()));

    assertThat(subscribersResource.getLinks(), hasSize(1));
    assertThat(subscribersResource.getLinks().get(0), is(expectedSubscribersResource.getLinks().get(0)));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when a request is made for the next page of all of the current
   * subscribers that subscribed since a specified date/time.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getChannelSubscribersWhenSubscribedSinceNextPage() throws Exception {
    int channelId = 1;
    Boolean subscribed = true;
    String subscribedSinceAsString = "2014-06-14T19:26:10Z";
    Date subscribedSinceDate = new ApiDateTimeFormatter().parse(subscribedSinceAsString);
    int pageSize = 200;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE + "?subscribed=" + subscribed + "&subscribedSince="
        + subscribedSinceAsString + "&cursor=1234&pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();
    PageCriteria pageCriteria = createPageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getChannelSubscribersWhenSubscribedSinceNextPage-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelSubscribersResource subscribersResource = this.apiClient.getChannelSubscribers(channelId, subscribed,
        subscribedSinceDate, null, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(subscribersResource, notNullValue());
    assertThat(subscribersResource.getChannelSubscribers(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    ChannelSubscribersResource expectedSubscribersResource =
        (ChannelSubscribersResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelSubscriberResource expectedSubscriberResource = expectedSubscribersResource.getChannelSubscribers().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(subscribersResource.getChannelSubscribers().get(0), is(expectedSubscriberResource));

    assertThat(subscribersResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when a request is made for the next page of all of the past
   * subscribers that have unsubscribed since a specified date/time.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getChannelSubscribersWhenUnsubscribedSinceNextPage() throws Exception {
    int channelId = 1;
    String unsubscribedSinceAsString = "2014-06-16T22:06:40Z";
    Date unsubscribedSinceDate = new ApiDateTimeFormatter().parse(unsubscribedSinceAsString);
    int pageSize = 200;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE + "?unsubscribedSince=" + unsubscribedSinceAsString
        + "&cursor=1234&pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();
    PageCriteria pageCriteria = createPageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getChannelSubscribersWhenSubscribedSinceNextPage-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    ChannelSubscribersResource subscribersResource = this.apiClient.getChannelSubscribers(channelId, null, null,
        unsubscribedSinceDate, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(subscribersResource, notNullValue());
    assertThat(subscribersResource.getChannelSubscribers(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    ChannelSubscribersResource expectedSubscribersResource =
        (ChannelSubscribersResource) this.xstream.fromXML(responseBody.getInputStream());
    ChannelSubscriberResource expectedSubscriberResource = expectedSubscribersResource.getChannelSubscribers().get(0);
    // Relies on overridden ChannelResource.equals() to test for equality by value
    assertThat(subscribersResource.getChannelSubscribers().get(0), is(expectedSubscriberResource));

    assertThat(subscribersResource.getLinks(), hasSize(0));
  }

  /**
   * Test {@link SpringApiClientImpl#getChannelSubscribers} in the case when the requested channel does not exist.
   */
  @Test
  public void getChannelSubscribersWhenChannelNotFound() {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + ChannelSubscribersResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call with a canned collection of Channel resources read from file
    ApiError apiError = new ApiError("ChannelNotFound", "Channel [" + channelId + "] not found");
    String responseBody = apiErrorToXml(apiError);
    MediaType mediaType = MediaType.APPLICATION_XML;
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withBadRequest().body(responseBody).contentType(mediaType));

    // Perform the test
    try {
      this.apiClient.getChannelSubscribers(channelId, null, null, null, null);
      fail("Expected exception to be thrown.");
    } catch (ApiErrorResponseException e) {
      assertThat(e.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
      assertThat(e.getApiError(), is(apiError));
    }

    this.mockReportingApiService.verify();
  }

  /**
   * Test {@link SpringApiClientImpl#getSubscribersWebcastActivityForWebcast} in the case where there is no activity for
   * the identified webcast.
   */
  @Test
  public void getSubscribersWebcastActivityForWebcastWhenZeroActivity() {
    int channelId = 1;
    int webcastId = 2;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SubscribersWebcastActivityResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<subscribersWebcastActivity/>", MediaType.APPLICATION_XML));

    // Perform the test
    SubscribersWebcastActivityResource subscribersWebcastActivityResource =
        this.apiClient.getSubscribersWebcastActivityForWebcast(
            channelId, webcastId, null, null, null);

    this.mockReportingApiService.verify();
    assertThat(subscribersWebcastActivityResource, notNullValue());
    assertThat(subscribersWebcastActivityResource.getSubscriberWebcastActivities(), hasSize(0));
    assertThat(subscribersWebcastActivityResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getSubscribersWebcastActivityForWebcast} when the first page of returned resources
   * contains multiple Subscriber Webcast Activity, and the total no. of activities is greater than the API page size,
   * resulting in a 'next' page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getSubscribersWebcastActivityForWebcastWhenMultipleActivitiesWithNextPage() throws Exception {
    int channelId = 1;
    int webcastId = 2;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SubscribersWebcastActivityResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody =
        new ClassPathResource(
            "SpringApiClientImplTest.getChannelSubscribersWebcastActivityForWebcastWhenMultipleActivitiesWithNextPage-response.xml",
            this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SubscribersWebcastActivityResource subscribersWebcastActivityResource =
        this.apiClient.getSubscribersWebcastActivityForWebcast(
            channelId, webcastId, null, null, null);

    this.mockReportingApiService.verify();
    assertThat(subscribersWebcastActivityResource, notNullValue());
    assertThat(subscribersWebcastActivityResource.getSubscriberWebcastActivities(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    SubscribersWebcastActivityResource expectedsubscribersWebcastActivityResource =
        (SubscribersWebcastActivityResource) this.xstream.fromXML(responseBody.getInputStream());
    SubscriberWebcastActivityResource expectedSWA =
        expectedsubscribersWebcastActivityResource.getSubscriberWebcastActivities().get(
            0);
    // Relies on overridden SubscriberWebcastActivityResource.equals() to test for equality by value
    assertThat(subscribersWebcastActivityResource.getSubscriberWebcastActivities().get(0), is(expectedSWA));

    // For the second of the two returned Resource just check its not the same as the first
    assertThat(subscribersWebcastActivityResource.getSubscriberWebcastActivities().get(0).getId(),
        not(subscribersWebcastActivityResource.getSubscriberWebcastActivities().get(1).getId()));

    assertThat(subscribersWebcastActivityResource.getLinks(), hasSize(1));
    assertThat(subscribersWebcastActivityResource.getLinks().get(0),
        is(expectedsubscribersWebcastActivityResource.getLinks().get(0)));
  }

  /**
   * Tests {@link SpringApiClientImpl#getSubscribersWebcastActivityForWebcast} with paging criteria that includes a next
   * page link and a non-default page size. The API responds with the final (last) page containing a single Subscriber
   * Webcast Activity.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getSubscribersWebcastActivityForWebcastWhenNextPageWithNonDefaultPageSizeReturnsLastPage()
      throws Exception {
    int channelId = 1;
    int webcastId = 2;
    int pageSize = 200;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SubscribersWebcastActivityResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE + "?cursor=1234&pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();
    PageCriteria pageCriteria = createPageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody =
        new ClassPathResource(
            "SpringApiClientImplTest.getChannelSubscribersWebcastActivityForWebcastWhenNextPageWithNonDefaultPageSizeReturnsLastPage-response.xml",
            this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SubscribersWebcastActivityResource subscribersWebcastActivityResource =
        this.apiClient.getSubscribersWebcastActivityForWebcast(
            channelId, webcastId, null, null, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(subscribersWebcastActivityResource, notNullValue());
    assertThat(subscribersWebcastActivityResource.getSubscriberWebcastActivities(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    SubscribersWebcastActivityResource expectedsubscribersWebcastActivityResource =
        (SubscribersWebcastActivityResource) this.xstream.fromXML(responseBody.getInputStream());
    SubscriberWebcastActivityResource expectedSWA =
        expectedsubscribersWebcastActivityResource.getSubscriberWebcastActivities().get(
            0);
    // Relies on overridden SubscriberWebcastActivityResource.equals() to test for equality by value
    assertThat(subscribersWebcastActivityResource.getSubscriberWebcastActivities().get(0), is(expectedSWA));

    assertThat(subscribersWebcastActivityResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getSubscribersWebcastActivityForWebcast} when a request is made for the next page
   * of Subscriber Webcast Activity which have been created or updated since a specified date/time, and the Activity
   * should be expanded to include the subscriber's channel survey response, if they have one.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getSubscribersWebcastActivityForWebcastWhenSinceNextPageAndExpandChannelSurveyResponse() throws Exception {
    int channelId = 1;
    int webcastId = 2;
    String sinceString = "2014-06-28T21:24:59Z";
    boolean expandChannelSurveyResponse = true;
    Date sinceDate = new ApiDateTimeFormatter().parse(sinceString);
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SubscribersWebcastActivityResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE + "?since=" + sinceString
        + "&expand=channelSurveyResponse&cursor=1234";
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();
    PageCriteria pageCriteria = createPageCriteria(null, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody =
        new ClassPathResource(
            "SpringApiClientImplTest.getChannelSubscribersWebcastActivityForWebcastWhenSinceNextPageAndExpandChannelSurveyResponse-response.xml",
            this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SubscribersWebcastActivityResource subscribersWebcastActivityResource =
        this.apiClient.getSubscribersWebcastActivityForWebcast(
            channelId, webcastId, sinceDate, expandChannelSurveyResponse, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(subscribersWebcastActivityResource, notNullValue());

    // Assert all fields in the first of the returned Resource
    SubscribersWebcastActivityResource expectedsubscribersWebcastActivityResource =
        (SubscribersWebcastActivityResource) this.xstream.fromXML(responseBody.getInputStream());
    SubscriberWebcastActivityResource expectedSWA =
        expectedsubscribersWebcastActivityResource.getSubscriberWebcastActivities().get(
            0);
    // Relies on overridden SubscriberWebcastActivityResource.equals() to test for equality by value
    assertThat(subscribersWebcastActivityResource.getSubscriberWebcastActivities().get(0), is(expectedSWA));

    assertThat(subscribersWebcastActivityResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getSubscribersWebcastActivityForChannel} when a request is made for the next page
   * of Subscriber Webcast Activity which have been created or updated since a specified date/time, and the Activity
   * should be expanded to include the subscriber's channel survey response, if they have one.
   * 
   * @throws Exception If an unexpected error occurs.
   * @see #getChannelSubscribersWebcastActivityForWebcastWhenSinceNextPageAndExpandChannelSurveyResponse
   */
  @Test
  public void getSubscribersWebcastActivityForChannelWhenSinceNextPageAndExpandChannelSurveyResponse() throws Exception {
    int channelId = 1;
    String sinceString = "2014-06-28T21:24:59Z";
    boolean expandChannelSurveyResponse = true;
    Date sinceDate = new ApiDateTimeFormatter().parse(sinceString);
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SubscribersWebcastActivityResource.FOR_CHANNEL_RELATIVE_URI_TEMPLATE + "?since=" + sinceString
        + "&expand=channelSurveyResponse&cursor=1234";
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();
    PageCriteria pageCriteria = createPageCriteria(null, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody =
        new ClassPathResource(
            "SpringApiClientImplTest.getChannelSubscribersWebcastActivityForChannelWhenSinceNextPageAndExpandChannelSurveyResponse-response.xml",
            this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SubscribersWebcastActivityResource subscribersWebcastActivityResource =
        this.apiClient.getSubscribersWebcastActivityForChannel(
            channelId, sinceDate, expandChannelSurveyResponse, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(subscribersWebcastActivityResource, notNullValue());

    // Assert all fields in the first of the returned Resource
    SubscribersWebcastActivityResource expectedsubscribersWebcastActivityResource =
        (SubscribersWebcastActivityResource) this.xstream.fromXML(responseBody.getInputStream());
    SubscriberWebcastActivityResource expectedSWA =
        expectedsubscribersWebcastActivityResource.getSubscriberWebcastActivities().get(
            0);
    // Relies on overridden SubscriberWebcastActivityResource.equals() to test for equality by value
    assertThat(subscribersWebcastActivityResource.getSubscriberWebcastActivities().get(0), is(expectedSWA));

    assertThat(subscribersWebcastActivityResource.getLinks(), hasSize(0));
  }

  /**
   * Test {@link SpringApiClientImpl#getSurveysForChannel} when the identified channel does not have a survey.
   */
  @Test
  public void getSurveysForChannelWhenNoSurveyFound() {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SurveysResource.FOR_CHANNELS_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<surveys/>", MediaType.APPLICATION_XML));

    // Perform the test
    SurveysResource surveysResource = this.apiClient.getSurveysForChannel(channelId);

    this.mockReportingApiService.verify();
    assertThat(surveysResource, notNullValue());
    assertThat(surveysResource.getSurveys(), hasSize(0));
  }

  /**
   * Test {@link SpringApiClientImpl#getSurveysForChannel} when the identified channel has a single survey which
   * comprises multiple questions of every supported type, some with multiple options.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getSurveysForChannelWhenSingleSurveyMultipleQuestionsOfEveryType() throws Exception {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SurveysResource.FOR_CHANNELS_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getSurveysForChannelWhenSingleSurveyMultipleQuestionsOfEveryType-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SurveysResource surveysResource = this.apiClient.getSurveysForChannel(channelId);

    this.mockReportingApiService.verify();
    assertThat(surveysResource, notNullValue());
    assertThat(surveysResource.getSurveys(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    SurveysResource expectedSurveysResource = (SurveysResource) this.xstream.fromXML(responseBody.getInputStream());
    SurveyResource expectedSurveyResource = expectedSurveysResource.getSurveys().get(0);
    // Relies on overridden SurveyResource.equals() to test for equality by value
    assertThat(surveysResource.getSurveys().get(0), is(expectedSurveyResource));
  }

  /**
   * Test {@link SpringApiClientImpl#getSurveys} when the identified survey exists, is inactive, and comprises multiple
   * questions of every supported type, some with multiple options.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getSurveyWhenInactiveSurveyMultipleQuestionsOfEveryType() throws Exception {
    int surveyId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri() + SurveyResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(surveyId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody =
        new ClassPathResource(
            "SpringApiClientImplTest.getSurveyWhenInactiveSurveyMultipleQuestionsOfEveryType-response.xml",
            this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SurveyResource surveyResource = this.apiClient.getSurvey(surveyId);

    this.mockReportingApiService.verify();
    assertThat(surveyResource, notNullValue());

    // Assert all fields in the first of the returned Resource
    SurveyResource expectedSurveyResource = (SurveyResource) this.xstream.fromXML(responseBody.getInputStream());
    // Relies on overridden SurveyResource.equals() to test for equality by value
    assertThat(surveyResource, is(expectedSurveyResource));
  }

  /**
   * Tests {@link SpringApiClientImpl#getSurvey} in the case where the API service returns a response containing a field
   * value which cannot be converted to the expected type as defined in the API resource class. In this case, return of
   * a {@link SurveyResource} with an active field which can't be converted to a boolean is tested.
   */
  @Test
  public void getSurveyWhenInvalidResponseTypeMismatchActive() {
    int surveyId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri() + SurveyResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(surveyId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getSurveyWhenInvalidResponseTypeMismatchActive-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SurveyResource surveyResource = this.apiClient.getSurvey(surveyId);

    // Current behaviour, as exhibited by the JAXB RI, is to suppress the error, resulting in default field value
    this.mockReportingApiService.verify();
    assertThat(surveyResource, notNullValue());
    assertThat(surveyResource.isActive(), is(false));
  }

  /**
   * Tests {@link SpringApiClientImpl#getSurveyResponses} when the request is for the first page of all the survey
   * responses (no filter criteria is specified in the request), and no responses are found.
   */
  @Test
  public void getSurveyResponsesWhenZeroResponses() {
    int surveyId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SurveyResponsesResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(surveyId).toString();

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<surveyResponses/>", MediaType.APPLICATION_XML));

    // Perform the test
    SurveyResponsesResource surveyResponsesResource = this.apiClient.getSurveyResponses(surveyId, null, null);

    this.mockReportingApiService.verify();
    assertThat(surveyResponsesResource, notNullValue());
    assertThat(surveyResponsesResource.getSurveyResponses(), hasSize(0));
    assertThat(surveyResponsesResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getSurveyResponses} for a survey with multiple questions, when there are responses
   * for more than one user/subscriber with, in some cases, multiple answers to a question, and a next page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getSurveyResponsesWhenMultipleResponsesWithMultipleQuestionsAndAnswersAndNextPage() throws Exception {
    int surveyId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SurveyResponsesResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(surveyId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody =
        new ClassPathResource(
            "SpringApiClientImplTest.getSurveyResponsesWhenMultipleResponsesWithMultipleQuestionsAndAnswersAndNextPage-response.xml",
            this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SurveyResponsesResource surveyResponsesResource = this.apiClient.getSurveyResponses(surveyId, null, null);

    this.mockReportingApiService.verify();
    assertThat(surveyResponsesResource, notNullValue());
    assertThat(surveyResponsesResource.getSurveyResponses(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    SurveyResponsesResource expectedSurveyResponsesResource =
        (SurveyResponsesResource) this.xstream.fromXML(responseBody.getInputStream());
    SurveyResponseResource expectedSurveyResponseResource = expectedSurveyResponsesResource.getSurveyResponses().get(0);
    // Relies on overridden SurveyResponseResource.equals() to test for equality by value
    assertThat(surveyResponsesResource.getSurveyResponses().get(0), is(expectedSurveyResponseResource));

    assertThat(surveyResponsesResource.getLinks(), hasSize(1));
  }

  /**
   * Tests {@link SpringApiClientImpl#getSurveyResponses} when the request is for the next page of responses since a
   * specified date/time, and the response is the last page containing a single response.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getSurveyResponsesWhenSinceAndNextPageWithLastPageSingleResponse() throws Exception {
    int surveyId = 1;
    String sinceString = "2014-06-28T21:24:59Z";
    Date sinceDate = new ApiDateTimeFormatter().parse(sinceString);
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + SurveyResponsesResource.RELATIVE_URI_TEMPLATE + "?since=" + sinceString + "&cursor=1234";
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(surveyId).toString();
    PageCriteria pageCriteria = createPageCriteria(null, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getSurveyResponsesWhenSinceAndNextPageWithLastPageSingleResponse-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    SurveyResponsesResource surveyResponsesResource = this.apiClient.getSurveyResponses(surveyId, sinceDate,
        pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(surveyResponsesResource, notNullValue());
    assertThat(surveyResponsesResource.getSurveyResponses(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    SurveyResponsesResource expectedSurveyResponsesResource =
        (SurveyResponsesResource) this.xstream.fromXML(responseBody.getInputStream());
    SurveyResponseResource expectedSurveyResponseResource = expectedSurveyResponsesResource.getSurveyResponses().get(0);
    // Relies on overridden SurveyResponseResource.equals() to test for equality by value
    assertThat(surveyResponsesResource.getSurveyResponses().get(0), is(expectedSurveyResponseResource));

    assertThat(surveyResponsesResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastsForChannel} when the request is for the first page of all the webcasts
   * in the identified channel (no filter criteria is specified in the request), and none exist.
   */
  @Test
  public void getWebcastsForChannelWhenZeroResponses() {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri() + WebcastsResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<webcasts/>", MediaType.APPLICATION_XML));

    // Perform the test
    WebcastsResource webcastsResource = this.apiClient.getWebcastsForChannel(channelId, null, null);

    this.mockReportingApiService.verify();
    assertThat(webcastsResource, notNullValue());
    assertThat(webcastsResource.getWebcasts(), hasSize(0));
    assertThat(webcastsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastsForChannel} when the request is for the first page of all the webcasts
   * in the identified channel (no filter criteria is specified in the request), and there are more than a pageful of
   * webcasts, and hence a next page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getWebcastsForChannelWhenMultipleWebastsAndNextPage() throws Exception {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri() + WebcastsResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getWebcastsForChannelWhenMultipleWebastsAndNextPage-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    WebcastsResource webcastsResource = this.apiClient.getWebcastsForChannel(channelId, null, null);

    this.mockReportingApiService.verify();
    assertThat(webcastsResource, notNullValue());
    assertThat(webcastsResource.getWebcasts(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    WebcastsResource expectedWebcastsResource = (WebcastsResource) this.xstream.fromXML(responseBody.getInputStream());
    WebcastResource expectedWebcastResource = expectedWebcastsResource.getWebcasts().get(0);
    // Relies on overridden WebcastResource.equals() to test for equality by value
    assertThat(webcastsResource.getWebcasts().get(0), is(expectedWebcastResource));

    assertThat(webcastsResource.getLinks(), hasSize(1));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastsForChannel} when the request is for the next page of webcasts since a
   * specified date/time, and the response is the last page containing a single webcast.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getWebcastsForChannelWhenSinceAndNextPageWithLastPageSingleWebcast() throws Exception {
    int channelId = 1;
    String sinceString = "2014-06-28T21:24:59Z";
    Date sinceDate = new ApiDateTimeFormatter().parse(sinceString);
    int pageSize = 200;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri() + WebcastsResource.RELATIVE_URI_TEMPLATE
        + "?since=" + sinceString + "&cursor=1234&pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();
    PageCriteria pageCriteria = createPageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getWebcastsForChannelWhenSinceAndNextPageWithLastPageSingleWebcast-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    WebcastsResource webcastsResource = this.apiClient.getWebcastsForChannel(channelId, sinceDate, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(webcastsResource, notNullValue());
    assertThat(webcastsResource.getWebcasts(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    WebcastsResource expectedWebcastsResource = (WebcastsResource) this.xstream.fromXML(responseBody.getInputStream());
    WebcastResource expectedWebcastResource = expectedWebcastsResource.getWebcasts().get(0);
    // Relies on overridden WebcastResource.equals() to test for equality by value
    assertThat(webcastsResource.getWebcasts().get(0), is(expectedWebcastResource));

    assertThat(webcastsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastsForChannel} when there are multiple {@link WebcastsResource webcasts}
   * for the channel to be reported, and these are requested and returned across separate pages.
   * <p>
   * Primarily written to provide an example of how to use the API client to page through a collection of resources,
   * using {@link PageCriteria} and the supporting {@link Links} class.
   */
  @Test
  public void getWebcastsForChannelWhenMultiplePagesToRetrieve() {
    final int channelId = 1;
    final int pageSize = 1;
    final Date since = null;

    // Configure mock API service to respond to API call for first page of resources
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri() + WebcastsResource.RELATIVE_URI_TEMPLATE +
        "?pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();
    Resource responseBodyPage1 = new ClassPathResource(
        "SpringApiClientImplTest.getWebcastsForChannelWhenMultiplePages-page1-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBodyPage1, MediaType.APPLICATION_XML));

    // Configure mock API service to respond to API call for second (and final) page of resources
    expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri() + WebcastsResource.RELATIVE_URI_TEMPLATE +
        "?cursor=5-1376595689&pageSize=" + pageSize;
    expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();
    Resource responseBodyPage2 = new ClassPathResource(
        "SpringApiClientImplTest.getWebcastsForChannelWhenMultiplePages-page2-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBodyPage2, MediaType.APPLICATION_XML));

    // Retrieve all pages of resources (until next page link is not present)
    List<WebcastResource> webcastResources = new ArrayList<>();
    Link nextPageLink = null;
    do {
      if (nextPageLink == null) {
        logger.debug("Retrieving first page of resources.");
      } else {
        logger.debug("Retrieving next page of resources using link [{}].", nextPageLink);
      }

      WebcastsResource webcastsResource =
          this.apiClient.getWebcastsForChannel(channelId, since, new PageCriteria(pageSize, nextPageLink));

      if (webcastsResource.getWebcasts().size() > 0) {
        // One or more resources returned

        // Unwrap returned page of resource collection 'webcasts' to get to member 'webcast'
        webcastResources.addAll(webcastsResource.getWebcasts());

        // Find optional next page link in last retrieved collection of resources
        nextPageLink = Links.findNextPageLink(webcastsResource.getLinks());
      } else if (nextPageLink != null) {
        logger.warn("Zero resources returned for previously returned next page link [{}]", nextPageLink);
        nextPageLink = null;
      }
    } while (nextPageLink != null);

    this.mockReportingApiService.verify();
    assertThat(webcastResources, notNullValue());
    assertThat(webcastResources, hasSize(2));    
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcast} in the case where the requested webcast exists, and contains all
   * optional fields.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getWebcastExistsFullyPopulated() throws Exception {
    int channelId = 1;
    int webcastId = 2;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri() + WebcastResource.RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();

    // Configure mock API service to respond to API call with a canned API resource read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getWebcastExistsFullyPopulated-response.xml", this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    WebcastResource webcastResource = this.apiClient.getWebcast(channelId, webcastId);

    this.mockReportingApiService.verify();
    assertThat(webcastResource, notNullValue());

    // Assert all fields in the returned Resource
    WebcastResource expectedWebcastResource = (WebcastResource) this.xstream.fromXML(responseBody.getInputStream());
    // Relies on overridden WebcastResource.equals() to test for equality by value
    assertThat(webcastResource, is(expectedWebcastResource));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastRegistrationsForWebcast} when the request is for the first page of all
   * the registrations for an identified webcast (no filter criteria is specified in the request), and none exist.
   */
  @Test
  public void getWebcastRegistrationsForWebcastWhenZeroFound() {
    int channelId = 1;
    int webcastId = 2;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + WebcastRegistrationsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<webcastRegistrations/>", MediaType.APPLICATION_XML));

    // Perform the test
    WebcastRegistrationsResource preregistrationsResource = this.apiClient.getWebcastRegistrationsForWebcast(channelId,
        webcastId, null, null, null);

    this.mockReportingApiService.verify();
    assertThat(preregistrationsResource, notNullValue());
    assertThat(preregistrationsResource.getWebcastRegistrations(), hasSize(0));
    assertThat(preregistrationsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastRegistrationsForWebcast} when the request is for the first page of all
   * the pre-registrations for the identified webcast (no filter criteria is specified in the request), and there are
   * more than a pageful of pre-registrations, and hence a next page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getWebcastRegistrationsForWebcastWhenMultipleRegistrationsAndNextPage() throws Exception {
    int channelId = 1;
    int webcastId = 2;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + WebcastRegistrationsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getWebcastRegistrationsForWebcastWhenMultipleRegistrationsAndNextPage-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    WebcastRegistrationsResource webcastRegistrationsResource = this.apiClient.getWebcastRegistrationsForWebcast(
        channelId, webcastId, null, null, null);

    this.mockReportingApiService.verify();
    assertThat(webcastRegistrationsResource, notNullValue());
    assertThat(webcastRegistrationsResource.getWebcastRegistrations(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    WebcastRegistrationsResource expectedWebcastRegistrationsResource =
        (WebcastRegistrationsResource) this.xstream.fromXML(responseBody.getInputStream());
    WebcastRegistrationResource expectedWebcastRegistrationResource =
        expectedWebcastRegistrationsResource.getWebcastRegistrations().get(
            0);
    // Relies on overridden WebcastResource.equals() to test for equality by value
    assertThat(webcastRegistrationsResource.getWebcastRegistrations().get(0), is(expectedWebcastRegistrationResource));

    assertThat(webcastRegistrationsResource.getLinks(), hasSize(1));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastRegistrationsForWebcast} when the request is for the next page of
   * pre-registrations, for which the registrant has also subsequently viewed, since a specified date/time, and the
   * response is the last page containing a single pre-registration.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getWebcastRegistrationsForWebcastWhenViewedAndSinceAndNextPageWithLastPage() throws Exception {
    int channelId = 1;
    int webcastId = 2;
    Boolean viewed = true;
    String sinceString = "2013-07-10T12:23:22Z";
    Date sinceDate = new ApiDateTimeFormatter().parse(sinceString);
    int pageSize = 200;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + WebcastRegistrationsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE + "?since=" + sinceString + "&viewed="
        + viewed + "&cursor=1234&pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();
    PageCriteria pageCriteria = createPageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody =
        new ClassPathResource(
            "SpringApiClientImplTest.getWebcastRegistrationsForWebcastWhenViewedOnlySinceAndNextPageWithLastPage-response.xml",
            this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    WebcastRegistrationsResource webcastRegistrationsResource = this.apiClient.getWebcastRegistrationsForWebcast(
        channelId, webcastId, sinceDate, viewed, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(webcastRegistrationsResource, notNullValue());
    assertThat(webcastRegistrationsResource.getWebcastRegistrations(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    WebcastRegistrationsResource expectedWebcastRegistrationsResource =
        (WebcastRegistrationsResource) this.xstream.fromXML(responseBody.getInputStream());
    WebcastRegistrationResource expectedWebcastRegistrationResource =
        expectedWebcastRegistrationsResource.getWebcastRegistrations().get(
            0);
    // Relies on overridden WebcastRegistrationResource.equals() to test for equality by value
    assertThat(webcastRegistrationsResource.getWebcastRegistrations().get(0), is(expectedWebcastRegistrationResource));

    assertThat(webcastRegistrationsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastViewingsForChannel} when the request is for the first page of all the
   * viewings in an identified channel (no filter criteria is specified in the request), and none exist.
   */
  @Test
  public void getWebcastViewingsForChannelWhenZeroFound() {
    int channelId = 1;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + WebcastViewingsResource.FOR_CHANNEL_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId).toString();

    // Configure mock API service to respond to API call
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess("<webcastViewings/>", MediaType.APPLICATION_XML));

    // Perform the test
    WebcastViewingsResource webcastViewings = this.apiClient.getWebcastViewingsForChannel(channelId, null, null, null);

    this.mockReportingApiService.verify();
    assertThat(webcastViewings, notNullValue());
    assertThat(webcastViewings.getWebcastViewings(), hasSize(0));
    assertThat(webcastViewings.getLinks(), hasSize(0));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastViewingsForChannel} in the error case where the requested webcast status
   * is not one of the status that the API supports.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getWebcastViewingsForChannelWhenViewingStatusNotSupported() throws Exception {
    int channelId = 1;
    WebcastStatus unsupportedWebcastStatus = WebcastStatus.PROCESSING;

    try {
      this.apiClient.getWebcastViewingsForChannel(channelId, null, unsupportedWebcastStatus, null);
      fail("Expected an exception to be thrown for an unsupported webcast status.");
    } catch (IllegalArgumentException e) {
      assertTrue("Unexpected exception message [" + e.getMessage() + "].",
          e.getMessage().matches(".*webcast status.*" + unsupportedWebcastStatus.toString() + ".*"));
    }
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastViewingsForWebcast} when the request is for the first page of all the
   * viewings for the identified webcast (no filter criteria is specified in the request), and there are more than a
   * pageful of viewings, and hence a next page link.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getWebcastViewingsForWebcastWhenMultipleRegistrationsAndNextPage() throws Exception {
    int channelId = 1;
    int webcastId = 2;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + WebcastViewingsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody = new ClassPathResource(
        "SpringApiClientImplTest.getWebcastViewingsForWebcastWhenMultipleViewingsAndNextPage-response.xml",
        this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    WebcastViewingsResource webcastViewingsResource = this.apiClient.getWebcastViewingsForWebcast(channelId, webcastId,
        null, null, null);

    this.mockReportingApiService.verify();
    assertThat(webcastViewingsResource, notNullValue());
    assertThat(webcastViewingsResource.getWebcastViewings(), hasSize(2));

    // Assert all fields in the first of the returned Resource
    WebcastViewingsResource expectedWebcastViewingsResource =
        (WebcastViewingsResource) this.xstream.fromXML(responseBody.getInputStream());
    WebcastViewingResource expectedWebcastViewingResource = expectedWebcastViewingsResource.getWebcastViewings().get(0);
    // Relies on overridden WebcastViewingResource.equals() to test for equality by value
    assertThat(webcastViewingsResource.getWebcastViewings().get(0), is(expectedWebcastViewingResource));

    assertThat(webcastViewingsResource.getLinks(), hasSize(1));
  }

  /**
   * Tests {@link SpringApiClientImpl#getWebcastViewingsForWebcast} when the request is for the next page of viewings,
   * matching a specified webcast status, since a specified date/time, and the response is the last page containing a
   * single viewing.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void getWebcastViewingsForWebcastWhenViewingStatusRecordedAndSinceAndNextPageWithLastPage() throws Exception {
    int channelId = 1;
    int webcastId = 2;
    String sinceString = "2013-07-11T19:16:36Z";
    Date sinceDate = new ApiDateTimeFormatter().parse(sinceString);
    WebcastStatus webcastStatus = WebcastStatus.RECORDED;
    int pageSize = 200;
    String expectedTemplateRequestUrl = this.apiClient.getApiServiceBaseUri()
        + WebcastViewingsResource.FOR_WEBCAST_RELATIVE_URI_TEMPLATE + "?since=" + sinceString + "&webcastStatus="
        + webcastStatus.toString() + "&cursor=1234&pageSize=" + pageSize;
    String expectedRequestUrl = new UriTemplate(expectedTemplateRequestUrl).expand(channelId, webcastId).toString();
    PageCriteria pageCriteria = createPageCriteria(pageSize, expectedRequestUrl);

    // Configure mock API service to respond to API call with a canned collection of API resources read from file
    Resource responseBody =
        new ClassPathResource(
            "SpringApiClientImplTest.getWebcastViewingsForWebcastViewingStatusRecordedAndSinceAndNextPageWithLastPage-response.xml",
            this.getClass());
    this.mockReportingApiService.expect(method(HttpMethod.GET)).andExpect(requestTo(expectedRequestUrl)).andRespond(
        withSuccess(responseBody, MediaType.APPLICATION_XML));

    // Perform the test
    WebcastViewingsResource webcastViewingsResource = this.apiClient.getWebcastViewingsForWebcast(channelId, webcastId,
        sinceDate, webcastStatus, pageCriteria);

    this.mockReportingApiService.verify();
    assertThat(webcastViewingsResource, notNullValue());
    assertThat(webcastViewingsResource.getWebcastViewings(), hasSize(1));

    // Assert all fields in the first of the returned Resource
    WebcastViewingsResource expectedWebcastViewingsResource =
        (WebcastViewingsResource) this.xstream.fromXML(responseBody.getInputStream());
    WebcastViewingResource expectedWebcastViewingResource = expectedWebcastViewingsResource.getWebcastViewings().get(0);
    // Relies on overridden WebcastRegistrationResource.equals() to test for equality by value
    assertThat(webcastViewingsResource.getWebcastViewings().get(0), is(expectedWebcastViewingResource));

    assertThat(webcastViewingsResource.getLinks(), hasSize(0));
  }

  private static PageCriteria createPageCriteria(Integer pageSize, String nextPageUrl) {
    return new PageCriteria(pageSize, new Link(nextPageUrl, LinkRelationType.next.name()));
  }

  private static String apiErrorToXml(ApiError apiError) {
    return "<?xml version='1.0' encoding='UTF-8'?><error><code>" + apiError.getCode()
        + "</code><message>" + apiError.getMessage() + "</message></error>";
  }

  /**
   * Configures the {@link XStream} instance the test uses to unamrshall (deserialise) canned API response payloads.
   */
  private void initXStream() {
    this.xstream = new XStream();
    this.xstream.alias("channels", ChannelsResource.class);
    this.xstream.registerConverter(new ChannelsResourceXStreamConverter());
    this.xstream.alias("channel", ChannelResource.class);
    this.xstream.registerConverter(new ChannelResourceXStreamConverter());
    this.xstream.alias("link", Link.class);
    this.xstream.registerConverter(new LinkXStreamConverter());
    this.xstream.alias("channelSubscribers", ChannelSubscribersResource.class);
    this.xstream.registerConverter(new ChannelSubscribersResourceXStreamConverter());
    this.xstream.alias("channelSubscriber", ChannelSubscriberResource.class);
    this.xstream.registerConverter(new ChannelSubscriberResourceXStreamConverter());
    this.xstream.alias("embed", Embed.class);
    this.xstream.registerConverter(new EmbedXStreamConverter());
    this.xstream.alias("user", User.class);
    this.xstream.registerConverter(new UserXStreamConverter());
    this.xstream.alias("subscribersWebcastActivity", SubscribersWebcastActivityResource.class);
    this.xstream.registerConverter(new SubscribersWebcastActivityResourceXStreamConverter());
    this.xstream.alias("subscriberWebcastActivity", SubscriberWebcastActivityResource.class);
    this.xstream.registerConverter(new SubscriberWebcastActivityResourceXStreamConverter());
    this.xstream.alias("surveyResponse", SurveyResponseResource.class);
    this.xstream.registerConverter(new SurveyResponseResourceXStreamConverter());
    this.xstream.alias("question", Question.class);
    this.xstream.registerConverter(new QuestionXStreamConverter());
    this.xstream.alias("surveys", SurveysResource.class);
    this.xstream.registerConverter(new SurveysResourceXStreamConverter());
    this.xstream.alias("survey", SurveyResource.class);
    this.xstream.registerConverter(new SurveyResourceXStreamConverter());
    this.xstream.alias("surveyResponses", SurveyResponsesResource.class);
    this.xstream.registerConverter(new SurveyResponsesResourceXStreamConverter());
    this.xstream.alias("webcasts", WebcastsResource.class);
    this.xstream.registerConverter(new WebcastsResourceXStreamConverter());
    this.xstream.alias("webcast", WebcastResource.class);
    this.xstream.registerConverter(new WebcastResourceXStreamConverter());
    this.xstream.alias("webcastRegistrations", WebcastRegistrationsResource.class);
    this.xstream.registerConverter(new WebcastRegistrationsResourceXStreamConverter());
    this.xstream.alias("webcastRegistration", WebcastRegistrationResource.class);
    this.xstream.registerConverter(new WebcastRegistrationResourceXStreamConverter());
    this.xstream.alias("webcastViewings", WebcastViewingsResource.class);
    this.xstream.registerConverter(new WebcastViewingsResourceXStreamConverter());
    this.xstream.alias("webcastViewing", WebcastViewingResource.class);
    this.xstream.registerConverter(new WebcastViewingResourceXStreamConverter());
  }
}