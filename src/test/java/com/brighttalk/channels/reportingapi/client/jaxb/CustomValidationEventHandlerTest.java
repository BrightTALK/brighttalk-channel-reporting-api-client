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
package com.brighttalk.channels.reportingapi.client.jaxb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.helpers.ValidationEventImpl;

import org.junit.Before;
import org.junit.Test;

import com.brighttalk.channels.reportingapi.client.jaxb.CustomValidationEventHandler;

/**
 * Unit tests for {@link CustomValidationEventHandler}.
 * 
 * @author Neil Brown
 */
public class CustomValidationEventHandlerTest {

  /** Instance of unit under test */
  private CustomValidationEventHandler uut;

  /**
   * @throws java.lang.Exception If an unexpected error occurs.
   */
  @Before
  public void setUp() throws Exception {
    // Create an instance of the event handler with its default configuration
    this.uut = new CustomValidationEventHandler();
  }

  /**
   * Tests how {@link CustomValidationEventHandler#handleEvent(ValidationEvent)} handles an event with severity of
   * fatal.
   */
  @Test
  public final void testHandleEventForFatalEvent() {
    ValidationEvent event = new ValidationEventImpl(ValidationEvent.FATAL_ERROR, "Fatal error", null);

    final boolean result = this.uut.handleEvent(event);

    // Handler should signal this is a fatal error and processing should be terminated
    assertThat(result, is(false));
  }
  
  /**
   * Tests how {@link CustomValidationEventHandler#handleEvent(ValidationEvent)} handles an event with severity of error
   * when the handler's lenient flag is set to the non-default value of {@code false}.
   */
  @Test
  public final void testHandleEventForErrorEventWhenNotLenient() {
    ValidationEvent event = new ValidationEventImpl(ValidationEvent.ERROR, "Error", null);
    this.uut.setLenient(false);

    final boolean result = this.uut.handleEvent(event);

    // Handler should signal this is a fatal error and processing should be terminated
    assertThat(result, is(false));
  }  

  /**
   * Tests how {@link CustomValidationEventHandler#handleEvent(ValidationEvent)} handles an event with severity of
   * error, when the handler is by default lenient, and the event has no linked exception.
   */
  @Test
  public final void testHandleEventForErrorEventWhenLenienetAndNoLinkedException() {
    ValidationEvent event = new ValidationEventImpl(ValidationEvent.ERROR, "Error", null);
    this.uut.setLenient(true);

    final boolean result = this.uut.handleEvent(event);

    // Handler should signal this is a NON-fatal error and processing should continue
    assertThat(result, is(true));
  }
  
  /**
   * Tests how {@link CustomValidationEventHandler#handleEvent(ValidationEvent)} handles an event with severity of
   * error, when the handler is by default lenient, and the event has a linked exception of a class which is NOT
   * configured as being fatal.
   */
  @Test
  public final void testHandleEventForErrorEventWhenLenientAndLinkedExceptionDoesNotMatchesConfiguredFatalExceptionClasses() {
    final IllegalArgumentException iae = new IllegalArgumentException();
    ValidationEvent event = new ValidationEventImpl(ValidationEvent.ERROR, "Error", null, iae);
    this.uut.setLenient(true);
    List<Class<? extends Exception>> exceptionClasses = new ArrayList<Class<? extends Exception>>();
    exceptionClasses.add(NumberFormatException.class);
    this.uut.setFatalLinkedExceptions(exceptionClasses);

    final boolean result = this.uut.handleEvent(event);

    // Handler should signal this is a NON-fatal error and processing should continue
    assertThat(result, is(true));
  }  

  /**
   * Tests how {@link CustomValidationEventHandler#handleEvent(ValidationEvent)} handles an event with severity of
   * error, when the handler is by default lenient, and the event has a linked exception of a class which IS configured
   * as being fatal.
   */
  @Test
  public final void testHandleEventForErrorEventWhenLenientAndLinkedExceptionMatchesConfiguredFatalExceptionClass() {
    final NumberFormatException nfe = new NumberFormatException();
    ValidationEvent event = new ValidationEventImpl(ValidationEvent.ERROR, "Error", null, nfe);
    this.uut.setLenient(true);
    List<Class<? extends Exception>> exceptionClasses = new ArrayList<Class<? extends Exception>>();
    exceptionClasses.add(nfe.getClass());
    this.uut.setFatalLinkedExceptions(exceptionClasses);

    final boolean result = this.uut.handleEvent(event);

    // Handler should signal this is a fatal error and processing should terminate
    assertThat(result, is(false));
  }
  
  /**
   * Tests how {@link CustomValidationEventHandler#handleEvent(ValidationEvent)} handles an event with severity of
   * error, when the handler is by default lenient, and the event has a linked exception of a class which itself is
   * NOT configured as being fatal, but the root cause of the exception is. 
   */
  @Test
  public final void testHandleEventForErrorEventWhenLenientAndLinkedExceptionMatchesInnermostConfiguredFatalExceptionClass() {
    final NumberFormatException nfe = new NumberFormatException();
    final JAXBException jaxbe = new JAXBException(nfe);
    ValidationEvent event = new ValidationEventImpl(ValidationEvent.ERROR, "Error", null, jaxbe);
    this.uut.setLenient(true);
    List<Class<? extends Exception>> exceptionClasses = new ArrayList<Class<? extends Exception>>();
    exceptionClasses.add(nfe.getClass());
    this.uut.setFatalLinkedExceptions(exceptionClasses);

    final boolean result = this.uut.handleEvent(event);

    // Handler should signal this is a fatal error and processing should terminate
    assertThat(result, is(false));
  }  
}