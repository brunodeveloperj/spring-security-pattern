package com.mds.security.filter;

import static com.mds.security.interceptor.helper.SessionDataHelper.encryptResponseIfClientFrontToBack;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.mds.security.filter.helper.URIFilterHelper;
import com.mds.security.interceptor.configuration.properties.CryptoConfigurationProperties;
import com.mds.security.interceptor.context.SecurityCryptoContext;
import com.mds.security.io.MdsContentCachingRequestWrapper;
import com.mds.security.io.MdsContentCachingResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet filter (order 1) that wraps every incoming request and outgoing
 * response in {@link MdsContentCachingRequestWrapper} /
 * {@link MdsContentCachingResponseWrapper} to allow body caching, and
 * transparently encrypts the response body when cryptography is active.
 *
 * <p>Extends {@link OncePerRequestFilter} to guarantee single execution per
 * request. Endpoints listed in
 * {@link CryptoConfigurationProperties#getEndpointExcludes()} bypass the filter
 * entirely.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public final class SecurityFilter extends OncePerRequestFilter {

  private final SecurityCryptoContext cryptoContext;
  private final CryptoConfigurationProperties cryptoProperties;

  /**
   * This method is called for each request to process it. It checks if the request URI is excluded from security filtering,
   * wraps the request and response in caching wrappers, and then delegates to the filter chain for further processing.
   *
   * @param request  the incoming HTTP request
   * @param response the outgoing HTTP response
   * @param chain    the filter chain to continue processing
   * @throws IOException      if an I/O error occurs during processing
   * @throws ServletException if a servlet error occurs during processing
   */
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain chain) throws IOException, ServletException {
    log.debug("Processing request: {}", request.getRequestURI());

    var requestURI = request.getRequestURI().replace(cryptoProperties.getContextPath(), EMPTY);
    if (URIFilterHelper.isEndpointExcluded(requestURI, cryptoProperties.getEndpointExcludes())) {
      log.debug("Request URI {} is excluded from security filter", request.getRequestURI());
      chain.doFilter(request, response);
      return;
    }

    var wrappedRequest = new MdsContentCachingRequestWrapper(request);
    var wrappedResponse = new MdsContentCachingResponseWrapper(response);

    try {
      chain.doFilter(wrappedRequest, wrappedResponse);
    } finally {
      log.debug("Request processing completed: {}", request.getRequestURI());
      encryptResponseIfClientFrontToBack(wrappedResponse, cryptoContext.getDlbCrypto(), cryptoProperties.isActive());
    }
  }
}
