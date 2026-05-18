package com.mds.security.interceptor.converter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * Utility class that traverses the chain of {@link HttpServletRequestWrapper} /
 * {@link HttpServletResponseWrapper} decorators to locate a wrapper instance of
 * a specific target type (e.g. {@link com.mds.security.io.MdsContentCachingRequestWrapper}).
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public class ServletUnwrapper {

  @SuppressWarnings("unchecked")
  public static <T> T unwrapResquest(HttpServletRequest request, Class<T> targetClass) {
    HttpServletRequest current = request;
    while (current instanceof HttpServletRequestWrapper wrapper) {
      if (targetClass.isInstance(current)) {
        return (T) current;
      }
      current = (HttpServletRequest) wrapper.getRequest();
    }
    // Check the last level (in case it is not a wrapper)
    if (targetClass.isInstance(current)) {
      return (T) current;
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static <T> T unwrapResponse(HttpServletResponse response, Class<T> targetClass) {
    HttpServletResponse current = response;
    while (current instanceof HttpServletResponseWrapper wrapper) {
      if (targetClass.isInstance(current)) {
        return (T) current;
      }
      current = (HttpServletResponse) wrapper.getResponse();
    }
    // Check the last level (in case it is not a wrapper)
    if (targetClass.isInstance(current)) {
      return (T) current;
    }
    return null;
  }
}
