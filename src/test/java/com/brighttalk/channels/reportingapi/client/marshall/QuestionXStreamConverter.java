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
package com.brighttalk.channels.reportingapi.client.marshall;

import java.util.ArrayList;
import java.util.List;

import com.brighttalk.channels.reportingapi.client.resource.Question;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of a {@link Question}.
 * 
 * @author Neil Brown
 */
public class QuestionXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")  
  public boolean canConvert(Class type) {
    return type.equals(Question.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");    
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {    
    int id = Integer.parseInt(reader.getAttribute("id"));
    String reportingId = reader.getAttribute("reportingId");
    String type = reader.getAttribute("type"); 
    boolean required = reader.getAttribute("required") != null ? Boolean.parseBoolean(reader.getAttribute("required")) : false;
    boolean deleted = reader.getAttribute("deleted") != null ? Boolean.parseBoolean(reader.getAttribute("deleted")) : false;    
    String text = null;
    List<String> options = null;
    List<String> answers = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if (nodeName.equals("text")) {
        text = reader.getValue();
      } else if (nodeName.equals("options")) {
        if (options == null) {
          options = new ArrayList<>();
        }
        while (reader.hasMoreChildren()) {
          reader.moveDown();
          options.add(reader.getValue());
          reader.moveUp();          
        }
      } else if (nodeName.equals("answers")) {
        if (answers == null) {
          answers = new ArrayList<>();
        }
        while (reader.hasMoreChildren()) {
          reader.moveDown();
          answers.add(reader.getValue());
          reader.moveUp();          
        }        
      }
      reader.moveUp();
    }
    Question q = new Question(id, reportingId, type, text, options);
    q.setRequired(required);
    q.setDeleted(deleted);
    if (answers != null) {
      q.setAnswers(answers);
    }
    return q;
  }
}