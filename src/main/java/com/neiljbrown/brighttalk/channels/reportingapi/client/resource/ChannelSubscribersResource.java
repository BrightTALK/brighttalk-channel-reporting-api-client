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
package com.neiljbrown.brighttalk.channels.reportingapi.client.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link ChannelSubscriberResource channel subscribers}.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "channelSubscribers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelSubscribersResource {
  
  /** URI template string for the relative URL of this API resource. @see http://tools.ietf.org/html/rfc6570 */
  public static final String RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/subscribers";
  
  @XmlElementRef
  private List<ChannelSubscriberResource> channelSubscribers = new ArrayList<>();
  @XmlElement(name = "link")
  private List<Link> links = new ArrayList<>();
  
  // Private, as only exists only to keep JAXB implementation happy.
  private ChannelSubscribersResource() {
  }  
  
  public ChannelSubscribersResource(List<ChannelSubscriberResource> channelSubscribers, List<Link> links) {
    this.channelSubscribers = channelSubscribers;
    this.links = links;
  }

  public final List<ChannelSubscriberResource> getChannelSubscribers() {
    return this.channelSubscribers;
  }

  public final List<Link> getLinks() {
    return this.links;
  }    
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("channelSubscribers", this.channelSubscribers)
      .add("links", this.links)
      .toString();
    /* @formatter:on */    
  }  
}