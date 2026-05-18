package com.mds.security.io.exception;

import com.mds.error.handler.exception.base.BaseException;

/**
 * Exception thrown when reading, deserializing or writing the HTTP response
 * body fails in {@link com.mds.security.io.MdsContentCachingResponseWrapper}.
 * Extends {@link BaseException} with an error code and message.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class ResponseBodyParseException extends BaseException {

  /**
   * Constructs a new ResponseBodyParseException with a detailed message and cause.
   *
   * @param code the error code associated with the exception
   * @param message the detail message describing the error
   * @param cause the cause of the error (the exception that triggered this error)
   */
  public ResponseBodyParseException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
