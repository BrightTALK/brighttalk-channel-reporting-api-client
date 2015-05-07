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
import com.brighttalk.channels.reportingapi.client.resource.Link;
import com.brighttalk.channels.reportingapi.client.resource.WebcastResource;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of a {@link WebcastResource}.
 * 
 * @author Neil Brown
 */
public class WebcastResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(WebcastResource.class);
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
    String title = null;
    String description = null;
    String presenter = null;
    int duration = 0;
    String start = null;
    String keywords = null;
    boolean published = false;
    String visibility = null;
    String clientBookingRef = null;
    String url = null;
    List<String> categories = null;
    String status = null;
    String syndicationType = null;
    String created = null;
    String lastUpdated = null;
    List<Link> links = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if ("title".equals(nodeName)) {
        title = reader.getValue();
      } else if ("description".equals(nodeName)) {
        description = reader.getValue();        
      } else if ("presenter".equals(nodeName)) {
        presenter = reader.getValue();        
      } else if ("duration".equals(nodeName)) {
        duration = Integer.valueOf(reader.getValue());        
      } else if ("start".equals(nodeName)) {
        start = reader.getValue();        
      } else if ("keywords".equals(nodeName)) {
        keywords = reader.getValue();        
      } else if ("published".equals(nodeName)) {
        published = Boolean.valueOf(reader.getValue());
      } else if ("visibility".equals(nodeName)) {
        visibility = reader.getValue();        
      } else if ("clientBookingRef".equals(nodeName)) {
        clientBookingRef = reader.getValue();        
      } else if ("url".equals(nodeName)) {   
        url = reader.getValue();        
      } else if ("categories".equals(nodeName)) {
        if (categories == null) {
          categories = new ArrayList<>();
        }
        while (reader.hasMoreChildren()) {
          reader.moveDown();
          categories.add(reader.getValue());
          reader.moveUp();          
        }        
      } else if ("status".equals(nodeName)) {
        status = reader.getValue();        
      } else if ("syndicationType".equals(nodeName)) {
        syndicationType = reader.getValue();        
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
    Date startDate = created != null ? dateTimeFormatter.parse(start) : null;
    Date createdDate = created != null ? dateTimeFormatter.parse(created) : null;
    Date lastUpdatedDate = lastUpdated != null ? dateTimeFormatter.parse(lastUpdated) : null;
    return new WebcastResource(id, title, description, presenter, duration, startDate, keywords, published, visibility, 
          clientBookingRef, url, categories, status, syndicationType, createdDate, lastUpdatedDate, links);    
  }
}
