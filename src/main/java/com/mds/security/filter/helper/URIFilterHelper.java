package com.mds.security.filter.helper;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;

/**
 * Helper class for URI filtering operations. Provides utility methods to check
 * if a given request URI matches a list of exclusion patterns using Spring's
 * {@link AntPathMatcher}. This class is non-instantiable and thread-safe.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = PRIVATE)
public class URIFilterHelper {

  // Thread-safe instance of AntPathMatcher used for pattern matching.
  private static final AntPathMatcher MATCHER = new AntPathMatcher();

  /**
   * Checks if the given request URI matches any of the exclusion patterns.
   *
   * @param requestURI The URI of the incoming request to be checked.
   * @param endpointExcludes A list of patterns representing excluded endpoints.
   * @return true if the request URI matches any exclusion pattern, false otherwise.
   */
  public static boolean isEndpointExcluded(String requestURI, List<String> endpointExcludes) {
    for (String pattern : endpointExcludes) {
      if (MATCHER.match(pattern, requestURI)) {
        return true;
      }
    }
    return false;
  }
}
