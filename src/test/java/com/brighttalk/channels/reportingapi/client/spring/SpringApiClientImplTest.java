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
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.brighttalk.channels.reportingapi.client.spring.SpringApiClientImpl;

/**
 * Unit tests for {@link SpringApiClientImpl}.
 */
public class SpringApiClientImplTest {

  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, String, Integer, RestTemplate)} in the
   * case where the supplied protocol and port are for valid, encrypted communications.
   */
  @Test
  public final void testConstructValidEncryptedProtocolAndPort() {
    new SpringApiClientImpl("https", "api.test.brighttalk.net", 443, new RestTemplate());
  }

  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, String, Integer, RestTemplate)} in the
   * case where the supplied protocol and port are for valid, unencrypted communications.
   */
  @Test
  public final void testConstructValidUnencryptedProtocolAndPort() {
    new SpringApiClientImpl("http", "api.test.brighttalk.net", 80, new RestTemplate());
  }
  
  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, String, Integer, RestTemplate)} in the
   * case where the optional protocol is not supplied. A default should be used.
   */
  @Test
  public final void testConstructOptionlProtocolNotSupplied() {
    SpringApiClientImpl apiClient = new SpringApiClientImpl(null, "api.test.brighttalk.net", 443, new RestTemplate());
    assertThat(apiClient.getApiServiceProtocol(), is("https"));    
  } 
  
  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, String, Integer, RestTemplate)} in the
   * case where the optional port is not supplied. A default should be used, which varies based upon the protocol.
   */
  @Test
  public final void testConstructOptionlPortNotSupplied() {
    SpringApiClientImpl apiClient = new SpringApiClientImpl("http", "api.test.brighttalk.net", null, new RestTemplate());
    assertThat(apiClient.getApiServicePort(), is(80));    
  }   

  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, RestTemplate)} in the case where the
   * supplied host name is valid. Serves to test the default protocol and port.
   */
  @Test
  public final void testConstructValidHostNameOnly() {
    SpringApiClientImpl apiClient = new SpringApiClientImpl("api.test.brighttalk.net", new RestTemplate());
    assertThat(apiClient.getApiServiceProtocol(), is("https"));
    assertThat(apiClient.getApiServicePort(), is(443));
  }
    
  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, String, Integer, RestTemplate)} in the
   * case where the supplied protocol is invalid, because it's an unknown string.
   */
  @Test
  public final void testConstructInvalidProtocolUnknown() {
    final String protocol = "foo";
    try {
      new SpringApiClientImpl(protocol, "api.test.brighttalk.net", 80, new RestTemplate());
      fail("Expected an exception to be thrown.");
    } catch (IllegalArgumentException e) {
      assertTrue("Unexepected exception message [" + e.toString() + "].",
          e.getMessage().matches(".*protocol.*" + protocol + ".*"));
    }
  }
  
  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, String, Integer, RestTemplate)} in the
   * case where the supplied port is invalid, because it's a negative number.
   */
  @Test
  public final void testConstructInvalidPortNegative() {
    int port = -1;
    try {
      new SpringApiClientImpl("https", "api.test.brighttalk.net", port, new RestTemplate());
      fail("Expected an exception to be thrown.");
    } catch (IllegalArgumentException e) {
      assertTrue("Unexepected exception message [" + e.toString() + "].",
          e.getMessage().matches(".*port.*" + port + ".*"));
    }
  }    

  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, RestTemplate)} in the case where the
   * supplied host name is invalid, because it contains an unsupported character e.g a space.
   */
  @Test
  public final void testConstructInvalidHostNameUnsupportedCharacter() {
    final String hostName = "<invalid host name>";
    try {
      new SpringApiClientImpl(hostName, new RestTemplate());
      fail("Expected an exception to be thrown.");
    } catch (IllegalArgumentException e) {
      assertTrue("Unexepected exception message [" + e.toString() + "].",
          e.getMessage().matches(".*host name.*" + hostName + ".*"));
    }
  }
  
  /**
   * Tests constructor {@link SpringApiClientImpl#SpringApiClientImpl(String, RestTemplate)} in the case where the
   * supplied host name is invalid, because it contains an unsupported character e.g a space.
   */
  @Test
  public final void testConstructInvalidHostNameNull() {
    try {
      new SpringApiClientImpl(null, new RestTemplate());
      fail("Expected an exception to be thrown.");
    } catch (NullPointerException e) {
      assertTrue("Unexepected exception message [" + e.toString() + "].",
          e.getMessage().matches(".*host name.*null.*"));
    }
  }  
}