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
package com.brighttalk.channels.reportingapi.client.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link WebcastResource webcast}.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "webcasts")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebcastsResource {
  
  /** URI template string for the relative URL of the Webcasts in an identified channel. */
  public static final String RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/webcasts";  
  
  @XmlElementRef
  private List<WebcastResource> webcasts;

  @XmlElement(name = "link")
  private List<Link> links;
  
  // Private, as only exists only to keep JAXB implementation happy.
  private WebcastsResource() {
  }  

  public WebcastsResource(List<WebcastResource> webcasts, List<Link> links) {
    this.webcasts = webcasts;
    this.links = links;
  }

  public final List<WebcastResource> getWebcasts() {
    return this.webcasts != null ? this.webcasts : new ArrayList<WebcastResource>();
  }

  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("webcasts", this.webcasts)
      .add("links", this.links)      
      .toString();
    /* @formatter:on */
  }
}