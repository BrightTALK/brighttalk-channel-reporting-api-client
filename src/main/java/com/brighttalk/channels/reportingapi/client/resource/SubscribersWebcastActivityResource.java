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
 * A collection of {@link SubscriberWebcastActivityResource subscriber webcast activity}.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "subscribersWebcastActivity")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscribersWebcastActivityResource {

  /** URI template string for the relative URL of the Subscribers Webcast Activity for an identified channel. */
  public static final String FOR_CHANNEL_RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/subscribers_webcast_activity";

  /** URI template string for the relative URL of the Subscribers Webcast Activity for an identified webcast. */
  public static final String FOR_WEBCAST_RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/webcast/{webcastId}/subscribers_webcast_activity";

  @XmlElementRef
  private List<SubscriberWebcastActivityResource> subscriberWebcastActivities = new ArrayList<>();
  
  @XmlElement(name = "link")
  private List<Link> links = new ArrayList<>();

  // Private, as only exists only to keep JAXB implementation happy.
  private SubscribersWebcastActivityResource() {
  }

  public SubscribersWebcastActivityResource(List<SubscriberWebcastActivityResource> subscriberWebcastActivities,
      List<Link> links) {
    this.subscriberWebcastActivities = subscriberWebcastActivities;
    this.links = links;
  }

  /**
   * @return the subscriberWebcastActivities
   */
  public final List<SubscriberWebcastActivityResource> getSubscriberWebcastActivities() {
    return this.subscriberWebcastActivities != null ? this.subscriberWebcastActivities
        : new ArrayList<SubscriberWebcastActivityResource>();
  }

  /**
   * @return the links
   */
  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("subscriberWebcastActivities", this.subscriberWebcastActivities)
      .add("links", this.links)
      .toString();
    /* @formatter:on */
  }
}