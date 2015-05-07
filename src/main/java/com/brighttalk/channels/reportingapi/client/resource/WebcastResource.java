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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A podcast, webinar or video communication in a BrightTALK Channel.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "webcast")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebcastResource {
  
  /** URI template string for the relative URL of a Webcast */
  public static final String RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/webcast/{webcastId}";

  @XmlAttribute
  private int id;
  private String title;
  private String description;
  private String presenter;
  private int duration;
  private Date start;
  private String keywords;
  private boolean published;
  private String visibility;
  private String clientBookingRef;
  private String url;
  @XmlElementWrapper(name = "categories")  
  @XmlElement(name = "category")  
  private List<String> categories;
  private String status;
  private String syndicationType;
  private Date created;
  private Date lastUpdated;
  @XmlElement(name = "link")
  private List<Link> links;
  
  // Private, as only exists only to keep JAXB implementation happy.
  private WebcastResource() {
  }  
  
  public WebcastResource(int id) {
    this.id = id;
  }
  
  public WebcastResource(int id, String title, String description, String presenter, int duration, Date start,
      String keywords, boolean published, String visibility, String clientBookingRef, String url,
      List<String> categories, String status, String syndicationType, Date created, Date lastUpdated,
      List<Link> links) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.presenter = presenter;
    this.duration = duration;
    this.start = start;
    this.keywords = keywords;
    this.published = published;
    this.visibility = visibility;
    this.clientBookingRef = clientBookingRef;
    this.url = url;
    this.categories = categories;
    this.status = status;
    this.syndicationType = syndicationType;
    this.created = created;
    this.lastUpdated = lastUpdated;
    this.links = links;
  }

  public final int getId() {
    return this.id;
  }

  public final String getTitle() {
    return this.title;
  }

  public final String getDescription() {
    return this.description;
  }

  public final String getPresenter() {
    return this.presenter;
  }

  public final int getDuration() {
    return this.duration;
  }

  public final Date getStart() {
    return this.start;
  }

  public final String getKeywords() {
    return this.keywords;
  }

  public final boolean isPublished() {
    return this.published;
  }

  public final String getVisibility() {
    return this.visibility;
  }

  public final String getClientBookingRef() {
    return this.clientBookingRef;
  }

  public final String getUrl() {
    return this.url;
  }

  public final List<String> getCategories() {
    return this.categories;
  }

  public final String getStatus() {
    return this.status;
  }

  public final String getSyndicationType() {
    return this.syndicationType;
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
      .add("title", this.title)
      .add("description", this.description)
      .add("presenter", this.presenter)      
      .add("duration", this.duration)
      .add("start", this.start)
      .add("keywords", this.keywords)
      .add("published", this.published)
      .add("visbility", this.visibility)
      .add("clientBookingRef", this.clientBookingRef)
      .add("url", this.url)
      .add("categories", this.categories)
      .add("status", this.status)
      .add("syndicationType", this.syndicationType)
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
    result = prime * result + ((this.categories == null) ? 0 : this.categories.hashCode());
    result = prime * result + ((this.clientBookingRef == null) ? 0 : this.clientBookingRef.hashCode());
    result = prime * result + ((this.created == null) ? 0 : this.created.hashCode());
    result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
    result = prime * result + this.duration;
    result = prime * result + this.id;
    result = prime * result + ((this.keywords == null) ? 0 : this.keywords.hashCode());
    result = prime * result + ((this.lastUpdated == null) ? 0 : this.lastUpdated.hashCode());
    result = prime * result + ((this.links == null) ? 0 : this.links.hashCode());
    result = prime * result + ((this.presenter == null) ? 0 : this.presenter.hashCode());
    result = prime * result + (this.published ? 1231 : 1237);
    result = prime * result + ((this.start == null) ? 0 : this.start.hashCode());
    result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
    result = prime * result + ((this.syndicationType == null) ? 0 : this.syndicationType.hashCode());
    result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
    result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
    result = prime * result + ((this.visibility == null) ? 0 : this.visibility.hashCode());
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
    WebcastResource other = (WebcastResource) obj;
    if (this.categories == null) {
      if (other.categories != null) {
        return false;
      }
    } else if (!this.categories.equals(other.categories)) {
      return false;
    }
    if (this.clientBookingRef == null) {
      if (other.clientBookingRef != null) {
        return false;
      }
    } else if (!this.clientBookingRef.equals(other.clientBookingRef)) {
      return false;
    }
    if (this.created == null) {
      if (other.created != null) {
        return false;
      }
    } else if (!this.created.equals(other.created)) {
      return false;
    }
    if (this.description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!this.description.equals(other.description)) {
      return false;
    }
    if (this.duration != other.duration) {
      return false;
    }
    if (this.id != other.id) {
      return false;
    }
    if (this.keywords == null) {
      if (other.keywords != null) {
        return false;
      }
    } else if (!this.keywords.equals(other.keywords)) {
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
    if (this.presenter == null) {
      if (other.presenter != null) {
        return false;
      }
    } else if (!this.presenter.equals(other.presenter)) {
      return false;
    }
    if (this.published != other.published) {
      return false;
    }
    if (this.start == null) {
      if (other.start != null) {
        return false;
      }
    } else if (!this.start.equals(other.start)) {
      return false;
    }
    if (this.status == null) {
      if (other.status != null) {
        return false;
      }
    } else if (!this.status.equals(other.status)) {
      return false;
    }
    if (this.syndicationType == null) {
      if (other.syndicationType != null) {
        return false;
      }
    } else if (!this.syndicationType.equals(other.syndicationType)) {
      return false;
    }
    if (this.title == null) {
      if (other.title != null) {
        return false;
      }
    } else if (!this.title.equals(other.title)) {
      return false;
    }
    if (this.url == null) {
      if (other.url != null) {
        return false;
      }
    } else if (!this.url.equals(other.url)) {
      return false;
    }
    if (this.visibility == null) {
      if (other.visibility != null) {
        return false;
      }
    } else if (!this.visibility.equals(other.visibility)) {
      return false;
    }
    return true;
  }    
}