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
package com.brighttalk.channels.reportingapi.client.jaxb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;

import com.brighttalk.channels.reportingapi.client.jaxb.IntegerXmlAdapter;

/**
 * Unit tests for {@link IntegerXmlAdapter}.
 * 
 * @author Neil Brown
 */
public class IntegerXmlAdapterTest {

  private IntegerXmlAdapter xmlAdapter = new IntegerXmlAdapter();

  /**
   * Tests {@link IntegerXmlAdapter#unmarshal} for a valid integer string.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testUnmarshalIntegerString() throws Exception {
    int i = 1;
    assertThat(this.xmlAdapter.unmarshal(String.valueOf(i)), is(i));
  }

  /**
   * Tests {@link IntegerXmlAdapter#unmarshal} for a non-integer string.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test(expected = NumberFormatException.class)
  public void testUnmarshalNonIntegerString() throws Exception {
    this.xmlAdapter.unmarshal(String.valueOf("foo"));
  }
  
  /**
   * Tests {@link IntegerXmlAdapter#marshal} for a null string.
   * 
   * @throws Exception If an unexpected error occurs.
   */
  @Test
  public void testMarshalNull() throws Exception {
    assertThat(this.xmlAdapter.marshal(null), is(nullValue()));
  }  
}