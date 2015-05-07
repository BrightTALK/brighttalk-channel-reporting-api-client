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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link SurveyResource surveys}.
 * 
 * @author Neil Brown
 */
@XmlRootElement(name = "surveys")
@XmlAccessorType(XmlAccessType.FIELD)
public class SurveysResource {
  
  /** URI template string for the relative URL of the Surveys resource for an identified channel. */
  public static final String FOR_CHANNELS_RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/surveys";
  
  @XmlElementRef
  private List<SurveyResource> surveys;
  
  // Private, as only exists only to keep JAXB implementation happy. 
  private SurveysResource() {
  }
  
  public SurveysResource(List<SurveyResource> surveys) {
    this.surveys = surveys;
  }
  
  public final List<SurveyResource> getSurveys() {
    return this.surveys != null ? this.surveys : new ArrayList<SurveyResource>();
  }
    
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("surveys", this.surveys)
      .toString();
    /* @formatter:on */    
  }   
}