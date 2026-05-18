package com.mds.security.io.exception;

import com.mds.error.handler.exception.base.BaseException;

/**
 * Exception thrown when an unsupported character encoding is set on
 * {@link com.mds.security.io.MdsContentCachingRequestWrapper}. Extends
 * {@link BaseException} with an error code and message.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class CharacterEncodingException extends BaseException {

  /**
   * Constructs a new CharacterEncodingException with a detailed message and cause.
   *
   * @param code    the error code associated with the exception
   * @param message the detail message describing the error
   * @param cause   the cause of the error (the exception that triggered this error)
   */
  public CharacterEncodingException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
