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

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Custom class of JAXB {@link XmlAdapter} for converting an XML string value to/from an Integer which doesn't cause a
 * fatal error on umarshalling a non-integer string.
 * 
 * <h2>Motivation</h2>
 * The default behaviour of most XmlAdapter in the JAXB reference implementation (RI) is to report a
 * non-fatal validation error if a type mismatch / conversion error occurs on unmarshalling, leaving the target field
 * set to its default value and continuing with unmarshalling the rest of the XML document. However, there is a bug in
 * the RI which results in it throwing a fatal NumberFormatException if it unmarshalls a non-integer string. This custom
 * XmlAdapter has been created to work around this bug and ensure consistent error handling with the XmlAdapter for
 * other types.
 * 
 * @author Neil Brown
 */
public class IntegerXmlAdapter extends XmlAdapter<String, Integer> {

  /**
   * {@inheritDoc}
   * 
   * @throws NumberFormatException if the string to be unmarshalled is a non-integer string. The JAXB API states that
   * callers of this adapter are responsible for catching this exception and reporting it to any configured
   * ValdiationEventHandler.
   */
  @Override
  public Integer unmarshal(String v) throws Exception {
    return Integer.parseInt(v);
  }

  /**
   * {@inheritDoc}
   * <p>
   * This implementation replicates the marshalling behaviour of the standard XmlAdapter provided in the JAXB RI - null
   * Integers result in a null String.
   */
  @Override
  public String marshal(Integer v) {
    return v != null ? String.valueOf(v) : null;
  }
}