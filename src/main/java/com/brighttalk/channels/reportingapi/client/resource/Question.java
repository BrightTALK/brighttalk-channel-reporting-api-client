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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.google.common.base.Objects;

/**
 * A question, and optionally the answers supplied by a contextual user.
 * 
 * @author Neil Brown
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Question {
  @XmlAttribute
  private int id;
  @XmlAttribute  
  private String reportingId;
  @XmlAttribute  
  private String type;
  @XmlAttribute
  private boolean required;
  @XmlAttribute
  private boolean deleted;
  
  private String text;  
  
  @XmlElementWrapper(name = "options")
  @XmlElement(name = "option")
  private List<String> options;
  
  @XmlElementWrapper(name = "answers")
  @XmlElement(name = "answer")  
  private List<String> answers;  
  
  // Protected, as only exists only to keep JAXB implementation happy, but sub-class also needs access
  protected Question() {
  }  
  
  public Question(int id, String reportingId, String type, String text, List<String> options) {
    this.id = id;
    this.reportingId = reportingId;
    this.type = type;
    this.text = text;
    this.options = options;
  }

  public final int getId() {
    return this.id;
  }

  public final String getReportingId() {
    return this.reportingId;
  }

  public final String getType() {
    return this.type;
  }
    
  public final boolean getRequired() {
    return this.required;
  }
  
  public final void setRequired(boolean required) {
    this.required = required;
  }

  public final String getText() {
    return this.text;
  }

  public final List<String> getOptions() {    
    return this.options != null ? this.options : new ArrayList<String>();
  }
         
  public final boolean getDeleted() {
    return this.deleted;
  }

  public final void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public final List<String> getAnswers() {
    return this.answers;
  }

  public final void setAnswers(List<String> answers) {
    this.answers = answers;
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.id)
      .add("reportingId", this.reportingId)      
      .add("type", this.type)
      .add("required", this.required)      
      .add("deleted", this.deleted)      
      .add("text", this.text)
      .add("options", this.options)      
      .add("answers", this.answers)      
      .toString();
    /* @formatter:on */    
  }

  /** {@inheritDoc} */
  // auto-generated  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.answers == null) ? 0 : this.answers.hashCode());
    result = prime * result + (this.deleted ? 1231 : 1237);
    result = prime * result + this.id;
    result = prime * result + ((this.options == null) ? 0 : this.options.hashCode());
    result = prime * result + ((this.reportingId == null) ? 0 : this.reportingId.hashCode());
    result = prime * result + (this.required ? 1231 : 1237);
    result = prime * result + ((this.text == null) ? 0 : this.text.hashCode());
    result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
    return result;
  }

  /** {@inheritDoc} */
  @Override
  // auto-generated  
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Question)) {
      return false;
    }
    Question other = (Question) obj;
    if (this.answers == null) {
      if (other.answers != null) {
        return false;
      }
    } else if (!this.answers.equals(other.answers)) {
      return false;
    }
    if (this.deleted != other.deleted) {
      return false;
    }
    if (this.id != other.id) {
      return false;
    }
    if (this.options == null) {
      if (other.options != null) {
        return false;
      }
    } else if (!this.options.equals(other.options)) {
      return false;
    }
    if (this.reportingId == null) {
      if (other.reportingId != null) {
        return false;
      }
    } else if (!this.reportingId.equals(other.reportingId)) {
      return false;
    }
    if (this.required != other.required) {
      return false;
    }
    if (this.text == null) {
      if (other.text != null) {
        return false;
      }
    } else if (!this.text.equals(other.text)) {
      return false;
    }
    if (this.type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!this.type.equals(other.type)) {
      return false;
    }
    return true;
  }    
}