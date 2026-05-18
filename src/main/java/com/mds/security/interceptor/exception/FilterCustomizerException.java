package com.mds.security.interceptor.exception;

import com.mds.error.handler.exception.base.BaseException;

/**
 * Exception thrown when a {@link com.mds.security.interceptor.FilterCustomizer}
 * fails during execution. Wraps the original cause for resolution by
 * {@link com.mds.security.interceptor.exception.resolver.FilterCustomizerExceptionResolver}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class FilterCustomizerException extends BaseException {

  /**
   * Constructor for the FilterCustomizerException class.
   * Initializes the exception with the original cause of the error.
   *
   * @param cause the original cause of the exception
   */
  public FilterCustomizerException(Throwable cause) {
    super(cause);
  }
}
