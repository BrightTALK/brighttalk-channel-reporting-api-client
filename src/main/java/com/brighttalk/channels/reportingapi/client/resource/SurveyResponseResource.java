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
 * A BrightTALK user's response to a Survey.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "surveyResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class SurveyResponseResource {
  @XmlAttribute
  private int id;
  private SurveyResource survey;
  private User user;
  @XmlElementWrapper(name = "questions")
  @XmlElement(name = "question")
  private List<Question> questions;
  private Date created;
  private Date lastUpdated;
  @XmlElement(name = "link")
  private List<Link> links;

  // Private, as only exists only to keep JAXB implementation happy.
  private SurveyResponseResource() {
  }

  public SurveyResponseResource(int id, SurveyResource survey, User user, List<Question> questions, Date created,
      Date lastUpdated, List<Link> links) {
    this.id = id;
    this.survey = survey;
    this.user = user;
    this.questions = questions;
    this.created = created;
    this.lastUpdated = lastUpdated;
    this.links = links;
  }

  public final int getId() {
    return this.id;
  }
  
  

  public final User getUser() {
    return this.user;
  }

  public final List<Question> getQuestions() {
    return this.questions != null ? this.questions : new ArrayList<Question>();
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
      .add("survey", this.survey)      
      .add("user", this.user)
      .add("questions", this.questions)
      .add("created", this.created)
      .add("lastUpdated", this.lastUpdated)      
      .add("links", this.links)      
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
    result = prime * result + ((this.created == null) ? 0 : this.created.hashCode());
    result = prime * result + this.id;
    result = prime * result + ((this.lastUpdated == null) ? 0 : this.lastUpdated.hashCode());
    result = prime * result + ((this.links == null) ? 0 : this.links.hashCode());
    result = prime * result + ((this.questions == null) ? 0 : this.questions.hashCode());
    result = prime * result + ((this.survey == null) ? 0 : this.survey.hashCode());
    result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
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
    if (!(obj instanceof SurveyResponseResource)) {
      return false;
    }
    SurveyResponseResource other = (SurveyResponseResource) obj;
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
    if (this.questions == null) {
      if (other.questions != null) {
        return false;
      }
    } else if (!this.questions.equals(other.questions)) {
      return false;
    }
    if (this.survey == null) {
      if (other.survey != null) {
        return false;
      }
    } else if (!this.survey.equals(other.survey)) {
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