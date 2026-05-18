package com.mds.security.interceptor;

import com.mds.security.io.MdsContentCachingRequestWrapper;
import com.mds.security.io.MdsContentCachingResponseWrapper;
import com.mds.error.handler.exception.GeneralException;

/**
 * Functional interface for applying custom logic to the cached request and
 * response wrappers during the security interceptor pipeline.
 *
 * <p>Concrete implementations are split into {@link BeforeFilterCustomizer}
 * (executed in {@code preHandle}) and {@link AfterFilterCustomizer}
 * (executed in {@code afterCompletion}).
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@FunctionalInterface
public interface FilterCustomizer {

  /**
   * Customizes the request and response in a filter.
   *
   * <p>This method is called to apply custom logic to the request and response before they are
   * processed further in the filter chain.
   *
   * @param wrappedRequest  the wrapped HTTP servlet request
   * @param wrappedResponse the wrapped HTTP servlet response
   * @throws GeneralException if an error occurs during customization
   */
  void customize(MdsContentCachingRequestWrapper wrappedRequest, MdsContentCachingResponseWrapper wrappedResponse) throws GeneralException;
}
