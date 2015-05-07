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

import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.base.Objects;

/**
 * A grouping of users of the BrightTALK Channels platform that are registered, authenticated and managed externally.
 * 
 * @author Neil Brown
 */
public class UserRealm {
  @XmlAttribute
  private int id;
  
  // Private, as only exists only to keep JAXB implementation happy.
  private UserRealm() {
  }  

  public UserRealm(int id) {
    this.id = id;
  }

  public final int getId() {
    return this.id;
  }      
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.id)
      .toString();
    /* @formatter:on */    
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.id;
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
    UserRealm other = (UserRealm) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }  
}