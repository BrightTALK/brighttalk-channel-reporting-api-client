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
 * A BrightTALK Channel - a container for managing and delivering webcasts and other content.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelResource {

  /** Template for the relative URL of this class of API resource */
  private static final String RELATIVE_URL_TEMPLATE = "/v1/channel/{channelId}";

  @XmlAttribute
  private int id;
  private String name;
  private String strapline;
  private String description;
  private String url;
  private String organisation;
  private String keywords;
  private String type;
  private Date created;
  private Date lastUpdated;
  @XmlElement(name = "link")
  private List<Link> links;

  // Private, as only exists only to keep JAXB implementation happy.
  private ChannelResource() {
  }

  public ChannelResource(int id, String name, String strapline, String description, String url, String organisation,
      String keywords, String type, Date created, Date lastUpdated, List<Link> links) {
    this.id = id;
    this.name = name;
    this.strapline = strapline;
    this.description = description;
    this.url = url;
    this.organisation = organisation;
    this.keywords = keywords;
    this.type = type;
    this.created = created;
    this.lastUpdated = lastUpdated;
    this.links = links;
  }

  public final int getId() {
    return this.id;
  }

  public final String getName() {
    return this.name;
  }

  public final String getStrapline() {
    return this.strapline;
  }

  public final String getDescription() {
    return this.description;
  }

  public final String getUrl() {
    return this.url;
  }

  public final String getOrganisation() {
    return this.organisation;
  }

  public final String getKeywords() {
    return this.keywords;
  }

  public final String getType() {
    return this.type;
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
      .add("name", this.name)
      .add("strapline", this.strapline)      
      .add("description", this.description)
      .add("url", this.url)
      .add("organisation", this.organisation)
      .add("keywords", this.keywords)
      .add("type", this.type)
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
    result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
    result = prime * result + this.id;
    result = prime * result + ((this.keywords == null) ? 0 : this.keywords.hashCode());
    result = prime * result + ((this.lastUpdated == null) ? 0 : this.lastUpdated.hashCode());
    result = prime * result + ((this.links == null) ? 0 : this.links.hashCode());
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.organisation == null) ? 0 : this.organisation.hashCode());
    result = prime * result + ((this.strapline == null) ? 0 : this.strapline.hashCode());
    result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
    result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
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
    ChannelResource other = (ChannelResource) obj;
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
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.organisation == null) {
      if (other.organisation != null) {
        return false;
      }
    } else if (!this.organisation.equals(other.organisation)) {
      return false;
    }
    if (this.strapline == null) {
      if (other.strapline != null) {
        return false;
      }
    } else if (!this.strapline.equals(other.strapline)) {
      return false;
    }
    if (this.type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!this.type.equals(other.type)) {
      return false;
    }
    if (this.url == null) {
      if (other.url != null) {
        return false;
      }
    } else if (!this.url.equals(other.url)) {
      return false;
    }
    return true;
  }      
}