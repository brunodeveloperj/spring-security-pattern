package com.mds.security.interceptor.helper;

import com.mds.security.interceptor.FilterCustomizer;
import com.mds.security.interceptor.exception.FilterCustomizerException;
import com.mds.security.io.MdsContentCachingRequestWrapper;
import com.mds.security.io.MdsContentCachingResponseWrapper;
import com.mds.error.handler.exception.GeneralException;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility that iterates over a list of {@link FilterCustomizer} instances and
 * invokes each one on the cached request/response wrappers, wrapping any
 * {@link GeneralException} in a {@link FilterCustomizerException}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterCustomizerHelper {

  /**
   * Executes a list of customizers on the provided request and response wrappers.
   *
   * @param wrappedRequest  The request wrapper to customize
   * @param wrappedResponse The response wrapper to customize
   * @param customizers     The list of customizers to execute
   * @param <T>             The type of the customizer, extending FilterCustomizer
   */
  public static <T extends FilterCustomizer> void executeCustomizersIfPresent(MdsContentCachingRequestWrapper wrappedRequest, MdsContentCachingResponseWrapper wrappedResponse, List<T> customizers) {
    if (customizers == null || customizers.isEmpty()) {
      return;
    }

    log.debug("Executing customizers");
    try {
      for (FilterCustomizer customizer : customizers) {
        customizer.customize(wrappedRequest, wrappedResponse);
      }
    } catch (GeneralException e) {
      log.error("Error executing customizers: {}", e.getMessage());
      throw new FilterCustomizerException(e);
    }
    log.debug("Customizers executed successfully");
  }

}
