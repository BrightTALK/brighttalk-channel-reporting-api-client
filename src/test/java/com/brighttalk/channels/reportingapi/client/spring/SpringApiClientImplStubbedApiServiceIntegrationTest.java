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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.brighttalk.channels.reportingapi.client.resource.ChannelsResource;
import com.brighttalk.channels.reportingapi.client.spring.AppConfig;
import com.brighttalk.channels.reportingapi.client.spring.SpringApiClientImpl;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Log4jNotifier;

/**
 * Additional integration tests for {@link SpringApiClientImpl}, which stub-out the API service.
 * <p>
 * The {@link SpringApiClientImplIntegrationTest} contains the bulk of the integration tests for
 * {@link SpringApiClientImpl}. However, as noted in that test case, those tests don't exercise the underlying HTTP
 * client as they're implemented using the Spring MVC Test framework, which substitutes the
 * {@link org.springframework.http.client.ClientHttpRequestFactory} to support stubbing and verification. These
 * additional integration tests are instead implemented with a stubbed API service (using WireMock) which additionally
 * test that the class' configured HTTP client behaves as expected.
 * 
 * @author Neil Brown
 * @see SpringApiClientImplIntegrationTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class SpringApiClientImplStubbedApiServiceIntegrationTest {

  /** Instance of class under test, configured with production instance of {@link RestTemplate} */
  @Autowired
  private SpringApiClientImpl apiClient;

  // Enviroment specific API service props used by injected SpringApiClientImpl instance for reuse when initialising
  // the mock server
  @Value("${apiService.protocol}")
  private String apiServiceProtocol;
  @Value("${apiService.hostName}")
  private String apiServiceHostName;
  @Value("${apiService.port}")
  private int apiServicePort;

  private WireMockServer wireMockServer;

  /** Automate the startup and shutdown of the mock HTTP server before and after the execution of each test */
  // Can't use WireMock's JUnit Rule as it runs before properties are injected by SpringJUnit4ClassRunner
  // @Rule
  // public WireMockRule wireMockRule =
  // new WireMockRule(wireMockConfig().bindAddress(this.apiServiceHostName).port(this.apiServicePort).notifier(
  // new Log4jNotifier()));

  /**
   * @throws Exception If an unexpected exception occurs.
   */
  @Before
  public void setUp() throws Exception {
    if (this.wireMockServer == null) {
      initWireMock();
    }
    this.wireMockServer.start();
  }

  @After
  public void teardown() {
    this.wireMockServer.stop();
  }

  /**
   * Tests {@link SpringApiClientImpl#getMyChannels} when the API user has zero channels.
   */
  @Test
  public void getMyChannelsWhenZeroChannels() {
    String expectedRequestUrl = ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    WireMock.stubFor(get(urlEqualTo(expectedRequestUrl)).willReturn(
        aResponse().withStatus(200).withHeader("Content-Type", MediaType.APPLICATION_XML.toString()).withBody(
            "<channels/>")));

    ChannelsResource channelsResource = this.apiClient.getMyChannels(null);
    assertThat(channelsResource, notNullValue());
    assertThat(channelsResource.getChannels(), hasSize(0));
    assertThat(channelsResource.getLinks(), hasSize(0));
  }

  /**
   * Tests that the API client performs preemptive basic authentication - includes authentication credentials in all its
   * requests, for the configured target host and post, rather than waiting to be challenged by the API service, thereby
   * avoiding the overhead of an extra request/response cycle.
   */
  @Test
  public void preemptiveBasicAuthentication() {
    String expectedRequestUrl = ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    WireMock.stubFor(get(urlEqualTo(expectedRequestUrl)).willReturn(
        aResponse().withStatus(200).withHeader("Content-Type", MediaType.APPLICATION_XML.toString()).withBody(
            "<channels/>")));

    this.apiClient.getMyChannels(null);

    WireMock.verify(getRequestedFor(urlEqualTo(expectedRequestUrl)).withHeader("Authorization", matching("Basic.*")));
  }

  /**
   * Tests that the API client takes advantage of the API service's support for HTTP (response) compression by including
   * HTTP header 'Accept-Encoding: gzip' in its requests.
   */
  @Test
  public void httpCompression() {
    String expectedRequestUrl = ChannelsResource.MY_CHANNELS_RELATIVE_URI_TEMPLATE;

    WireMock.stubFor(get(urlEqualTo(expectedRequestUrl)).willReturn(
        aResponse().withStatus(200).withHeader("Content-Type", MediaType.APPLICATION_XML.toString()).withBody(
            "<channels/>")));

    this.apiClient.getMyChannels(null);

    WireMock.verify(getRequestedFor(urlEqualTo(expectedRequestUrl)).withHeader("Accept-Encoding", matching(".*gzip.*")));
  }

  /**
   * Creates a WireMockServer that listens on the configured host and port and initialises the corresponding property.
   */
  private void initWireMock() {
    this.wireMockServer =
        new WireMockServer(wireMockConfig().bindAddress(this.apiServiceHostName).port(this.apiServicePort).notifier(
            new Log4jNotifier()));
    // If you change the default host and port, you also need to tell the WireMock facade about it
    WireMock.configureFor(this.apiServiceHostName, this.apiServicePort);
  }

}