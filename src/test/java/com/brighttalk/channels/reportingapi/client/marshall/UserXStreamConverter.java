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
package com.brighttalk.channels.reportingapi.client.marshall;

import com.brighttalk.channels.reportingapi.client.resource.User;
import com.brighttalk.channels.reportingapi.client.resource.UserRealm;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of {@link User}.
 * 
 * @author Neil Brown
 */
public class UserXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(User.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    int id = Integer.valueOf(reader.getAttribute("id"));
    String email = null;
    UserRealm userRealm = null;
    String realmUserId = null;
    String firstName = null;
    String lastName = null;
    String timeZone = null;
    String phone = null;
    String jobTitle = null;
    String level = null;
    String companyName = null;
    String companySize = null;
    String industry = null;
    String country = null;
    String stateProvince = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if ("email".equals(nodeName)) {
        email = reader.getValue();
      } else if ("realm".equals(nodeName)) {
        userRealm = new UserRealm(Integer.valueOf(reader.getAttribute("id")));
      } else if ("realmUserId".equals(nodeName)) {
        realmUserId = reader.getValue();
      } else if ("firstName".equals(nodeName)) {
        firstName = reader.getValue();
      } else if ("lastName".equals(nodeName)) {
        lastName = reader.getValue();
      } else if ("timeZone".equals(nodeName)) {
        timeZone = reader.getValue();
      } else if ("phone".equals(nodeName)) {
        phone = reader.getValue();
      } else if ("jobTitle".equals(nodeName)) {
        jobTitle = reader.getValue();
      } else if ("level".equals(nodeName)) {
        level = reader.getValue();
      } else if ("companyName".equals(nodeName)) {
        companyName = reader.getValue();
      } else if ("companySize".equals(nodeName)) {
        companySize = reader.getValue();
      } else if ("industry".equals(nodeName)) {
        industry = reader.getValue();
      } else if ("country".equals(nodeName)) {
        country = reader.getValue();
      } else if ("stateProvince".equals(nodeName)) {
        stateProvince = reader.getValue();
      }
      reader.moveUp();
    }
    return new User(id, email, userRealm, realmUserId, firstName, lastName, timeZone, phone, jobTitle, level,
        companyName, companySize, industry, country, stateProvince);
  }
}
