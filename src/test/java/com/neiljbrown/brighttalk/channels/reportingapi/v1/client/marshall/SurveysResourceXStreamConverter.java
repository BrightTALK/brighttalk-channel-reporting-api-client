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
package com.neiljbrown.brighttalk.channels.reportingapi.v1.client.marshall;

import java.util.ArrayList;
import java.util.List;

import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.SurveyResource;
import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource.SurveysResource;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of
 * {@link SurveysResource} - a collection of {@link SurveyResource}.
 * 
 * @see SurveyResourceXStreamConverter
 * @author Neil Brown
 */
public class SurveysResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(SurveysResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    List<SurveyResource> surveys = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      if ("survey".equals(reader.getNodeName())) {
        if (surveys == null) {
          surveys = new ArrayList<>();
        }
        SurveyResource survey = (SurveyResource) context.convertAnother(null, SurveyResource.class);
        surveys.add(survey);
      }
      reader.moveUp();
    }
    return new SurveysResource(surveys);
  }
}