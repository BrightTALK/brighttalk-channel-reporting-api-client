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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

/**
 * A custom implementation of JAXB {@link ValidationEventHandler} which supports a small set of configurable rules for
 * classifying when a {@link ValidationEvent} should be deemed a fatal error, as well as logging validation events to
 * the application's log file.
 * 
 * <h2>Motivation</h2>
 * <p>
 * The JAXB 2.0+ spec states that only malformed XML documents should be treated as fatal errors, and that all other
 * errors should _not_ cause binding to fail. In practice, this can lead to undesirable results, with invalid element
 * values that result in type mismatch /conversion errors being suppressed and the values of the bound fields having
 * erroneous (null or default) values. This custom ValidationEventHandler optionally enforces stricter validation, by
 * allowing events with a severity of {@link ValidationEvent#ERROR} with a root cause of certain configured classes of
 * Exception to be treated as fatal errors.
 * 
 * <h2>Configuration</h2>
 * <p>
 * The default configuration of this ValidationEventHandler is such that only {@link ValidationEvent} with a severity of
 * {@link ValidationEvent#FATAL_ERROR} are considered fatal (those with a severity of {@link ValidationEvent#ERROR} are
 * not). This (lenient) behaviour is identical the default behaviour in JAXB 2.0+.
 * <p>
 * Use {@link #setFatalLinkedExceptions} to selectively configure validation events of severity 'error' as fatal
 * marshalling errors if the root cause of their ('linked') exceptions matches.
 * <p>
 * Setting the lenient property to {@code false}, via {@link #setLenient}, results in ALL {@link ValidationEvent} with a
 * severity of {@link ValidationEvent#ERROR} being treated as fatal.
 * 
 * @author Neil Brown
 */
public class CustomValidationEventHandler implements ValidationEventHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomValidationEventHandler.class);

  private boolean lenient = true;

  private List<Class<? extends Exception>> fatalLinkedExceptions = new ArrayList<Class<? extends Exception>>();

  /**
   * {@inheritDoc}
   * <p>
   * If the validation event severity is classified as being fatal then it is logged as an error and the method returns
   * {@code false} to signal that processing of the content should be terminated. Otherwise, the validation error is
   * conditionally logged for info and the method returns {@code false} to indicate that processing should continue if
   * possible.
   * 
   * @see #isValidationEventFatal(ValidationEvent)
   */
  @Override
  public boolean handleEvent(ValidationEvent event) {
    final Throwable t = event.getLinkedException();
    if (this.isValidationEventFatal(event)) {
      logger.error("Fatal JAXB validation error [{}]."
          + (t != null ? " Causal ('linked') exception [" + t.toString() + "]." : ""), event);
      // Signal that current processing (marshalling, unmarshalling or validation) should be terminated
      return false;
    }
    logger.info("JAXB validation error [{}]."
        + (t != null ? " Causal ('linked') exception [" + t.toString() + "]." : ""), event);
    // Signal that current processing should continue if possible
    return true;
  }

  /**
   * Classifies whether or not the current event constitutes a fatal error.
   * <p>
   * An event is fatal if it has a severity of fatal; or has a severity of error and this handler is non-lenient; or has
   * a severity of error and the event has a linked (causal) exception with a root cause which matches or is a sub-class
   * of one of the configured fatal classes of exceptions.
   * 
   * @param event The current event.
   * @return {@code true} if the event is fatal, otherwise {@code false}.
   * @see #setFatalLinkedExceptions
   */
  private boolean isValidationEventFatal(ValidationEvent event) {
    final int severity = event.getSeverity();
    if (ValidationEvent.FATAL_ERROR == severity) {
      return true;
    } else if (ValidationEvent.ERROR == severity && !this.isLenient()) {
      return true;
    } else if (ValidationEvent.ERROR == severity && event.getLinkedException() != null) {
      Throwable linkedException = event.getLinkedException();
      Throwable rootCause = Throwables.getRootCause(linkedException);
      for (Class<? extends Exception> fatalException : this.fatalLinkedExceptions) {
        if (fatalException.isAssignableFrom(rootCause.getClass())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @return {@code true} if this handler is lenient.
   * @see #setLenient(boolean)
   */
  public final boolean isLenient() {
    return lenient;
  }

  /**
   * @param lenient Flag controlling whether or not this handler treats all validation events of severity 'error' as
   * fatal. Defaults to {@code true} meaning the handler is lenient, which is consistent with the default behaviour in
   * JAXB 2.0+.
   */
  public final void setLenient(boolean lenient) {
    this.lenient = lenient;
  }

  /**
   * @param fatalLinkedExceptions A list of Exception classes which if reported as (causal) linked exceptions of an
   * event of severity 'error' should result in the event being deemed a fatal error.
   */
  public final void setFatalLinkedExceptions(List<Class<? extends Exception>> fatalLinkedExceptions) {
    this.fatalLinkedExceptions = fatalLinkedExceptions;
  }
}