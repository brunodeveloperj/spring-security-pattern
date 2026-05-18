package com.mds.security.interceptor.exception;

import com.mds.error.handler.exception.base.BaseException;

/**
 * Exception thrown when path or query parameter decryption fails in the
 * security interceptor pipeline. Wraps the original cause for resolution
 * by {@link com.mds.security.interceptor.exception.resolver.DecryptParametersExceptionResolver}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class DecryptParametersException extends BaseException {

  /**
   * Constructs a new DecryptParametersException with the specified cause.
   *
   * @param cause the original cause of the exception
   */
  public DecryptParametersException(Throwable cause) {
    super(cause);
  }
}
