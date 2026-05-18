package com.mds.security.interceptor.exception;

import com.mds.error.handler.exception.base.BaseException;

/**
 * Exception thrown when session data extraction from JWT claims or HTTP
 * headers fails. Carries an error code and user-facing message for
 * resolution by {@link com.mds.security.interceptor.exception.resolver.ExtractionDataSessionExceptionResolver}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class ExtractionDataSessionException extends BaseException {

  /**
   * Constructor for the ExtractionDataSessionException class.
   * Initializes the exception with a code and descriptive message.
   *
   * @param code    the error code associated with the exception
   * @param message the descriptive message of the exception
   */
  public ExtractionDataSessionException(String code, String message) {
    super(code, message);
  }
}