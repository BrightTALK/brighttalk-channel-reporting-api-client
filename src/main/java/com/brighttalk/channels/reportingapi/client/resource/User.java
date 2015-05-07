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
import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.base.Objects;

/**
 * The set of BrightTALK subscriber's registered user details which are accessible to a Channel owner.
 * 
 * @author Neil Brown
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
  @XmlAttribute
  private int id;
  private String email;
  private UserRealm realm;
  private String realmUserId;
  private String firstName;
  private String lastName;
  private String timeZone;
  private String phone;
  private String jobTitle;
  private String level;
  private String companyName;
  private String companySize;
  private String industry;
  private String country;
  private String stateProvince;

  // Private, as only exists only to keep JAXB implementation happy.
  private User() {
  }

  public User(int id, String email, UserRealm realm, String realmUserId, String firstName, String lastName,
      String timeZone, String phone, String jobTitle, String level, String companyName, String companySize,
      String industry, String country, String stateProvince) {
    this.id = id;
    this.email = email;
    this.realm = realm;
    this.realmUserId = realmUserId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.timeZone = timeZone;
    this.phone = phone;
    this.jobTitle = jobTitle;
    this.level = level;
    this.companyName = companyName;
    this.companySize = companySize;
    this.industry = industry;
    this.country = country;
    this.stateProvince = stateProvince;
  }

  public final int getId() {
    return this.id;
  }

  public final String getEmail() {
    return this.email;
  }

  public final UserRealm getRealm() {
    return this.realm;
  }

  public final String getRealmUserId() {
    return this.realmUserId;
  }

  public final String getFirstName() {
    return this.firstName;
  }

  public final String getLastName() {
    return this.lastName;
  }
  
  public final String getTimeZone() {
    return this.timeZone;
  }

  public final String getPhone() {
    return this.phone;
  }

  public final String getJobTitle() {
    return this.jobTitle;
  }

  public final String getLevel() {
    return this.level;
  }

  public final String getCompanyName() {
    return this.companyName;
  }

  public final String getCompanySize() {
    return this.companySize;
  }

  public final String getIndustry() {
    return this.industry;
  }

  public final String getCountry() {
    return this.country;
  }

  public final String getStateProvince() {
    return this.stateProvince;
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.id)
      .add("email", this.email)
      .add("realm", this.realm)      
      .add("realmUserId", this.realmUserId) 
      .add("firstName", this.firstName)      
      .add("lastName", this.lastName)
      .add("timeZone", this.timeZone)
      .add("phone", this.phone)      
      .add("jobTitle", this.jobTitle)      
      .add("level", this.level)
      .add("companyName", this.companyName)      
      .add("companySize", this.companySize)
      .add("industry", this.industry)
      .add("country", this.country)
      .add("stateProvince", this.stateProvince)      
      .toString();
    /* @formatter:on */
  }

  /** 
   * {@inheritDoc}
   */
  // Auto-generated  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.companyName == null) ? 0 : this.companyName.hashCode());
    result = prime * result + ((this.companySize == null) ? 0 : this.companySize.hashCode());
    result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
    result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
    result = prime * result + ((this.firstName == null) ? 0 : this.firstName.hashCode());
    result = prime * result + this.id;
    result = prime * result + ((this.industry == null) ? 0 : this.industry.hashCode());
    result = prime * result + ((this.jobTitle == null) ? 0 : this.jobTitle.hashCode());
    result = prime * result + ((this.lastName == null) ? 0 : this.lastName.hashCode());
    result = prime * result + ((this.level == null) ? 0 : this.level.hashCode());
    result = prime * result + ((this.phone == null) ? 0 : this.phone.hashCode());
    result = prime * result + ((this.realm == null) ? 0 : this.realm.hashCode());
    result = prime * result + ((this.realmUserId == null) ? 0 : this.realmUserId.hashCode());
    result = prime * result + ((this.stateProvince == null) ? 0 : this.stateProvince.hashCode());
    result = prime * result + ((this.timeZone == null) ? 0 : this.timeZone.hashCode());
    return result;
  }

  /** 
   * {@inheritDoc}
   */
  // Auto-generated
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof User)) {
      return false;
    }
    User other = (User) obj;
    if (this.companyName == null) {
      if (other.companyName != null) {
        return false;
      }
    } else if (!this.companyName.equals(other.companyName)) {
      return false;
    }
    if (this.companySize == null) {
      if (other.companySize != null) {
        return false;
      }
    } else if (!this.companySize.equals(other.companySize)) {
      return false;
    }
    if (this.country == null) {
      if (other.country != null) {
        return false;
      }
    } else if (!this.country.equals(other.country)) {
      return false;
    }
    if (this.email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!this.email.equals(other.email)) {
      return false;
    }
    if (this.firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!this.firstName.equals(other.firstName)) {
      return false;
    }
    if (this.id != other.id) {
      return false;
    }
    if (this.industry == null) {
      if (other.industry != null) {
        return false;
      }
    } else if (!this.industry.equals(other.industry)) {
      return false;
    }
    if (this.jobTitle == null) {
      if (other.jobTitle != null) {
        return false;
      }
    } else if (!this.jobTitle.equals(other.jobTitle)) {
      return false;
    }
    if (this.lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!this.lastName.equals(other.lastName)) {
      return false;
    }
    if (this.level == null) {
      if (other.level != null) {
        return false;
      }
    } else if (!this.level.equals(other.level)) {
      return false;
    }
    if (this.phone == null) {
      if (other.phone != null) {
        return false;
      }
    } else if (!this.phone.equals(other.phone)) {
      return false;
    }
    if (this.realm == null) {
      if (other.realm != null) {
        return false;
      }
    } else if (!this.realm.equals(other.realm)) {
      return false;
    }
    if (this.realmUserId == null) {
      if (other.realmUserId != null) {
        return false;
      }
    } else if (!this.realmUserId.equals(other.realmUserId)) {
      return false;
    }
    if (this.stateProvince == null) {
      if (other.stateProvince != null) {
        return false;
      }
    } else if (!this.stateProvince.equals(other.stateProvince)) {
      return false;
    }
    if (this.timeZone == null) {
      if (other.timeZone != null) {
        return false;
      }
    } else if (!this.timeZone.equals(other.timeZone)) {
      return false;
    }
    return true;
  }
}