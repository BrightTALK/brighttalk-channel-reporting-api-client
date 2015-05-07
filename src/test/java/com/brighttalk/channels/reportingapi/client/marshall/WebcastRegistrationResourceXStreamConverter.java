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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.brighttalk.channels.reportingapi.client.common.ApiDateTimeFormatter;
import com.brighttalk.channels.reportingapi.client.resource.Embed;
import com.brighttalk.channels.reportingapi.client.resource.Link;
import com.brighttalk.channels.reportingapi.client.resource.User;
import com.brighttalk.channels.reportingapi.client.resource.WebcastRegistrationResource;
import com.brighttalk.channels.reportingapi.client.resource.WebcastResource;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of a
 * {@link WebcastRegistrationResource}.
 * 
 * @author Neil Brown
 */
public class WebcastRegistrationResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(WebcastRegistrationResource.class);
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
    WebcastResource webcast = null;
    User user = null;
    Embed embed = null;
    String created = null;
    String lastUpdated = null;
    List<Link> links = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if ("webcast".equals(nodeName)) {
        webcast = (WebcastResource) context.convertAnother(null, WebcastResource.class);        
      } else if ("user".equals(nodeName)) {
        user = (User) context.convertAnother(null, User.class);
      } else if ("embed".equals(nodeName)) {
        embed = (Embed) context.convertAnother(null, Embed.class); 
      } else if ("created".equals(nodeName)) {
        created = reader.getValue();
      } else if ("lastUpdated".equals(nodeName)) {
        lastUpdated = reader.getValue();
      } else if ("link".equals(nodeName)) {
        if (links == null) {
          links = new ArrayList<>();
        }
        Link link = (Link) context.convertAnother(null, Link.class);
        links.add(link);
      }
      reader.moveUp();
    }
    ApiDateTimeFormatter dateTimeFormatter = new ApiDateTimeFormatter();
    Date createdDate = created != null ? dateTimeFormatter.parse(created) : null;
    Date lastUpdatedDate = lastUpdated != null ? dateTimeFormatter.parse(lastUpdated) : null;
    return new WebcastRegistrationResource(id, webcast, user, embed, createdDate, lastUpdatedDate, links);
  }
}
