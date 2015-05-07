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
 * A viewing of a {@link WebcastResource webcast}.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "webcastViewing")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebcastViewingResource {
  @XmlAttribute
  private int id;
  private WebcastResource webcast;
  private String webcastStatus;
  private int duration;
  private User user;
  private Embed embed;
  private Date created;
  private Date lastUpdated;
  @XmlElement(name = "link")
  private List<Link> links;

  // Private, as only exists only to keep JAXB implementation happy.
  private WebcastViewingResource() {
  }

  public WebcastViewingResource(int id, WebcastResource webcast, String webcastStatus, int duration, User user,
      Embed embed, Date created, Date lastUpdated, List<Link> links) {
    this.id = id;
    this.webcast = webcast;
    this.webcastStatus = webcastStatus;
    this.duration = duration;
    this.user = user;
    this.embed = embed;
    this.created = created;
    this.lastUpdated = lastUpdated;
    this.links = links;
  }

  public final int getId() {
    return this.id;
  }

  public final WebcastResource getWebcast() {
    return this.webcast;
  }
    
  public final String getWebcastStatus() {
    return this.webcastStatus;
  }

  public final int getDuration() {
    return this.duration;
  }

  public final User getUser() {
    return this.user;
  }

  public final Embed getEmbed() {
    return this.embed;
  }

  public final Date getCreated() {
    return this.created;
  }

  public final Date getLastUpdated() {
    return this.lastUpdated;
  }

  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.id)
      .add("webcast", this.webcast)
      .add("duration", this.duration)
      .add("user", this.user)
      .add("embed", this.embed)
      .add("created", this.created)
      .add("lastUpdated", this.lastUpdated)        
      .add("links", this.links)      
      .toString();    
    /* @formatter:on */
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.created == null) ? 0 : this.created.hashCode());
    result = prime * result + this.duration;
    result = prime * result + ((this.embed == null) ? 0 : this.embed.hashCode());
    result = prime * result + this.id;
    result = prime * result + ((this.lastUpdated == null) ? 0 : this.lastUpdated.hashCode());
    result = prime * result + ((this.links == null) ? 0 : this.links.hashCode());
    result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
    result = prime * result + ((this.webcast == null) ? 0 : this.webcast.hashCode());
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
    WebcastViewingResource other = (WebcastViewingResource) obj;
    if (this.created == null) {
      if (other.created != null) {
        return false;
      }
    } else if (!this.created.equals(other.created)) {
      return false;
    }
    if (this.duration != other.duration) {
      return false;
    }
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
    if (this.lastUpdated == null) {
      if (other.lastUpdated != null) {
        return false;
      }
    } else if (!this.lastUpdated.equals(other.lastUpdated)) {
      return false;
    }
    if (this.links == null) {
      if (other.links != null) {
        return false;
      }
    } else if (!this.links.equals(other.links)) {
      return false;
    }
    if (this.user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!this.user.equals(other.user)) {
      return false;
    }
    if (this.webcast == null) {
      if (other.webcast != null) {
        return false;
      }
    } else if (!this.webcast.equals(other.webcast)) {
      return false;
    }
    return true;
  }
}