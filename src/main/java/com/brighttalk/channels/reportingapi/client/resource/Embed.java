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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.google.common.base.Objects;

/**
 * Details supplied via the embed of a BrightTALK player in its hosting web-page.
 * 
 * @author Neil Brown  
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Embed {
  private String url;
  private String track;
  
  // Private, as only exists only to keep JAXB implementation happy.
  private Embed() {
  }    
  
  public Embed(String url, String track) {
    this.url = url;
    this.track = track;
  }

  public final String getUrl() {
    return this.url;
  }

  public final String getTrack() {
    return this.track;
  }    
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("url", this.url)
      .add("track", this.track)
      .toString();
    /* @formatter:on */    
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.track == null) ? 0 : this.track.hashCode());
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
    Embed other = (Embed) obj;
    if (this.track == null) {
      if (other.track != null) {
        return false;
      }
    } else if (!this.track.equals(other.track)) {
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