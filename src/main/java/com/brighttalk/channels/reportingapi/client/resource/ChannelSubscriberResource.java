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
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A current or past subscriber to a BrightTALK Channel. Comprises the subscriber’s BrightTALK user ID, their accessible
 * user details, details of their last subscription and indication of their current subscription status.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "channelSubscriber")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelSubscriberResource {
  @XmlAttribute
  private int id;
  private Date lastSubscribed;
  private Date unsubscribed;
  private Embed embed;
  private User user;
  @XmlElement(name = "link")
  private List<Link> links = new ArrayList<>();
  
  // Private, as only exists only to keep JAXB implementation happy.
  private ChannelSubscriberResource() {
  }  

  public ChannelSubscriberResource(int id, Date lastSubscribed, Date unsubscribed, Embed embed, User user,
      List<Link> links) {
    this.id = id;
    this.lastSubscribed = lastSubscribed;
    this.unsubscribed = unsubscribed;
    this.embed = embed;
    this.user = user;
    this.links = links;
  }

  public final int getId() {
    return this.id;
  }

  public final Date getLastSubscribed() {
    return this.lastSubscribed;
  }

  public final Date getUnsubscribed() {
    return this.unsubscribed;
  }

  public final Embed getEmbed() {
    return this.embed;
  }

  public final User getUser() {
    return this.user;
  }

  public final List<Link> getLinks() {
    return this.links;
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.id)
      .add("lastSubscribed", this.lastSubscribed)
      .add("unsubscribed", this.unsubscribed)
      .add("embed", this.embed)      
      .add("user", this.user)
      .add("links", this.links)      
      .toString();
    /* @formatter:on */
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.embed == null) ? 0 : this.embed.hashCode());
    result = prime * result + this.id;
    result = prime * result + ((this.lastSubscribed == null) ? 0 : this.lastSubscribed.hashCode());
    result = prime * result + ((this.links == null) ? 0 : this.links.hashCode());
    result = prime * result + ((this.unsubscribed == null) ? 0 : this.unsubscribed.hashCode());
    result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ChannelSubscriberResource other = (ChannelSubscriberResource) obj;
    if (this.embed == null) {
      if (other.embed != null) {
        return false;
      }
    } else if (!this.embed.equals(other.embed)) {
      return false;
    }
    if (this.id != other.id) {
      return false;
    }
    if (this.lastSubscribed == null) {
      if (other.lastSubscribed != null) {
        return false;
      }
    } else if (!this.lastSubscribed.equals(other.lastSubscribed)) {
      return false;
    }
    if (this.links == null) {
      if (other.links != null) {
        return false;
      }
    } else if (!this.links.equals(other.links)) {
      return false;
    }
    if (this.unsubscribed == null) {
      if (other.unsubscribed != null) {
        return false;
      }
    } else if (!this.unsubscribed.equals(other.unsubscribed)) {
      return false;
    }
    if (this.user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!this.user.equals(other.user)) {
      return false;
    }
    return true;
  }    
}