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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * An aggregated summary of a Channel Subscriber’s activity for a given Webcast.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "subscriberWebcastActivity")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriberWebcastActivityResource {
  @XmlAttribute
  private int id;
  private WebcastResource webcast;
  private User user;
  private boolean preregistered;
  private int totalViewings;
  private int totalViewingDuration;
  private int liveViewings;
  private int liveViewingDuration;
  private int recordedViewings;
  private int recordedViewingDuration;
  @XmlElementRef
  private List<SurveyResponseResource> surveyResponses;
  private Date created;
  private Date lastUpdated;
  @XmlElement(name = "link")  
  private List<Link> links;

  // Private, as only exists only to keep JAXB implementation happy.
  private SubscriberWebcastActivityResource() {
  }  
  
  public SubscriberWebcastActivityResource(int id, WebcastResource webcast, User user, boolean preregistered,
      int totalViewings, int totalViewingDuration, int liveViewings, int liveViewingDuration, int recordedViewings,
      int recordedViewingDuration, List<SurveyResponseResource> surveyResponses, Date created, Date lastUpdated,
      List<Link> links) {
    this.id = id;
    this.webcast = webcast;
    this.user = user;
    this.preregistered = preregistered;
    this.totalViewings = totalViewings;
    this.totalViewingDuration = totalViewingDuration;
    this.liveViewings = liveViewings;
    this.liveViewingDuration = liveViewingDuration;
    this.recordedViewings = recordedViewings;
    this.recordedViewingDuration = recordedViewingDuration;
    this.surveyResponses = surveyResponses;
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

  public final User getUser() {
    return this.user;
  }

  public final boolean isPreregistered() {
    return this.preregistered;
  }

  public final int getTotalViewings() {
    return this.totalViewings;
  }

  public final int getTotalViewingDuration() {
    return this.totalViewingDuration;
  }

  public final int getLiveViewings() {
    return this.liveViewings;
  }

  public final int getLiveViewingDuration() {
    return this.liveViewingDuration;
  }

  public final int getRecordedViewings() {
    return this.recordedViewings;
  }

  public final int getRecordedViewingDuration() {
    return this.recordedViewingDuration;
  }

  public final List<SurveyResponseResource> getSurveyResponses() {
    return this.surveyResponses != null ? this.surveyResponses : new ArrayList<SurveyResponseResource>();
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
      .add("user", this.user)      
      .add("preregistered", this.preregistered)
      .add("totalViewings", this.totalViewings)      
      .add("totalViewingDuration", this.totalViewingDuration)
      .add("liveViewings", this.liveViewings)      
      .add("liveViewingDuration", this.liveViewingDuration)      
      .add("recordedViewings", this.recordedViewings)      
      .add("recordedViewingDuration", this.recordedViewingDuration)
      .add("surveyResponses", this.surveyResponses)
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
    result = prime * result + this.id;
    result = prime * result + ((this.lastUpdated == null) ? 0 : this.lastUpdated.hashCode());
    result = prime * result + ((this.links == null) ? 0 : this.links.hashCode());
    result = prime * result + this.liveViewingDuration;
    result = prime * result + this.liveViewings;
    result = prime * result + (this.preregistered ? 1231 : 1237);
    result = prime * result + this.recordedViewingDuration;
    result = prime * result + this.recordedViewings;
    result = prime * result + ((this.surveyResponses == null) ? 0 : this.surveyResponses.hashCode());
    result = prime * result + this.totalViewingDuration;
    result = prime * result + this.totalViewings;
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
    SubscriberWebcastActivityResource other = (SubscriberWebcastActivityResource) obj;
    if (this.created == null) {
      if (other.created != null) {
        return false;
      }
    } else if (!this.created.equals(other.created)) {
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
    if (this.liveViewingDuration != other.liveViewingDuration) {
      return false;
    }
    if (this.liveViewings != other.liveViewings) {
      return false;
    }
    if (this.preregistered != other.preregistered) {
      return false;
    }
    if (this.recordedViewingDuration != other.recordedViewingDuration) {
      return false;
    }
    if (this.recordedViewings != other.recordedViewings) {
      return false;
    }
    if (this.surveyResponses == null) {
      if (other.surveyResponses != null) {
        return false;
      }
    } else if (!this.surveyResponses.equals(other.surveyResponses)) {
      return false;
    }
    if (this.totalViewingDuration != other.totalViewingDuration) {
      return false;
    }
    if (this.totalViewings != other.totalViewings) {
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