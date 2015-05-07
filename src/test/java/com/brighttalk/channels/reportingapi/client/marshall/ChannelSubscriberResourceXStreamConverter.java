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
import com.brighttalk.channels.reportingapi.client.resource.ChannelSubscriberResource;
import com.brighttalk.channels.reportingapi.client.resource.Embed;
import com.brighttalk.channels.reportingapi.client.resource.Link;
import com.brighttalk.channels.reportingapi.client.resource.User;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of a
 * {@link ChannelSubscriberResource}.
 * 
 * @author Neil Brown
 */
public class ChannelSubscriberResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(ChannelSubscriberResource.class);
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
    String lastSubscribed = null;
    String unsubscribed = null;
    Embed embed = null;
    User user = null;
    List<Link> links = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if ("lastSubscribed".equals(nodeName)) {
        lastSubscribed = reader.getValue();
      } else if ("unsubscribed".equals(nodeName)) {
        unsubscribed = reader.getValue();
      } else if ("embed".equals(nodeName)) {
        embed = (Embed) context.convertAnother(null, Embed.class);        
      } else if ("user".equals(nodeName)) {
        user = (User) context.convertAnother(null, User.class);
      } else if ("link".equals(nodeName)) {
        if (links == null) {
          links = new ArrayList<>();
        }
        Link resourceLink = (Link) context.convertAnother(null, Link.class);
        links.add(resourceLink);
      }
      reader.moveUp();
    }
    ApiDateTimeFormatter dateTimeFormatter = new ApiDateTimeFormatter();
    Date lastSubscribedDate = lastSubscribed != null ? dateTimeFormatter.parse(lastSubscribed) : null;
    Date unsubscribedDate = unsubscribed != null ? dateTimeFormatter.parse(unsubscribed) : null;
    return new ChannelSubscriberResource(id, lastSubscribedDate, unsubscribedDate, embed, user, links);
  }
}
