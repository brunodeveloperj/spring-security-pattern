package com.mds.security.interceptor;

import static com.mds.security.interceptor.helper.AuthenticationTypeHelper.getDlbCrypto;
import static com.mds.security.interceptor.helper.FilterCustomizerHelper.executeCustomizersIfPresent;
import static com.mds.security.interceptor.helper.SessionDataHelper.decryptRequestIfClientFrontToBack;
import static com.mds.security.interceptor.helper.SessionDataHelper.resolveSessionData;
import static com.mds.security.interceptor.keys.SecurityKeys.AUTHENTICATION_TYPE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.mds.security.interceptor.configuration.properties.CryptoConfigurationProperties;
import com.mds.security.interceptor.context.SecurityCryptoContext;
import com.mds.security.interceptor.converter.AttributeConverter;
import com.mds.security.interceptor.converter.ServletUnwrapper;
import com.mds.security.interceptor.exception.DecryptParametersException;
import com.mds.security.io.MdsContentCachingRequestWrapper;
import com.mds.security.io.MdsContentCachingResponseWrapper;
import com.mds.error.handler.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Spring MVC {@link HandlerInterceptor} that orchestrates the security
 * pipeline: resolves session data from JWT/headers, decrypts the request
 * body and URL parameters when DLB FrontToBack cryptography is active, and
 * executes pluggable {@link BeforeFilterCustomizer} / {@link AfterFilterCustomizer}
 * hooks around controller processing.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityHandlerInterceptor implements HandlerInterceptor {

  private final SecurityCryptoContext cryptoContext;
  private final AttributeConverter attributeConverter;
  private final List<BeforeFilterCustomizer> beforeFilterCustomizers;
  private final List<AfterFilterCustomizer> afterFilterCustomizers;
  private final CryptoConfigurationProperties cryptoProperties;

  /**
   * Intercepts the request before it is handled by the controller.
   * Resolves session data and decrypts the request if the client uses FrontToBack cryptography.
   *
   * @param request  The incoming HTTP request
   * @param response The outgoing HTTP response
   * @param handler  The handler that will be executed
   * @return true to continue processing, false to stop the request
   */
  @Override
  public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

    MdsContentCachingRequestWrapper wrappedRequest = ServletUnwrapper.unwrapResquest(request, MdsContentCachingRequestWrapper.class);
    MdsContentCachingResponseWrapper wrappedResponse = ServletUnwrapper.unwrapResponse(response, MdsContentCachingResponseWrapper.class);
    if (wrappedRequest == null || wrappedResponse == null) {
      log.info("Request or response is not wrapped properly, skipping security processing.");
      return false;
    }
    var dlbCrypto = getDlbCrypto(StringUtils.defaultIfBlank(wrappedRequest.getHeader(AUTHENTICATION_TYPE), EMPTY));
    resolveSessionData(wrappedRequest, dlbCrypto, cryptoContext);
    decryptRequestIfClientFrontToBack(wrappedRequest, dlbCrypto, cryptoProperties.isActive());
    decryptParametersIfClientFrontToBack(wrappedRequest);
    executeCustomizersIfPresent(wrappedRequest, wrappedResponse, beforeFilterCustomizers);
    return true;
  }

  /**
   * Intercepts the request after the controller has processed it.
   * Executes any after-filter customizers on the request and response.
   *
   * @param request  The incoming HTTP request
   * @param response The outgoing HTTP response
   * @param handler  The handler that was executed
   * @param ex       Any exception that was thrown during processing, or null if none
   */
  @Override
  public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
    MdsContentCachingRequestWrapper wrappedRequest = ServletUnwrapper.unwrapResquest(request, MdsContentCachingRequestWrapper.class);
    MdsContentCachingResponseWrapper wrappedResponse = ServletUnwrapper.unwrapResponse(response, MdsContentCachingResponseWrapper.class);
    executeCustomizersIfPresent(wrappedRequest, wrappedResponse, afterFilterCustomizers);
  }

  /**
   * Decrypts path and query parameters if the client uses FrontToBack cryptography.
   *
   * @param wrappedRequest The request wrapper containing the HTTP request
   */
  private void decryptParametersIfClientFrontToBack(MdsContentCachingRequestWrapper wrappedRequest) {
    if (!cryptoProperties.isActive() || !(cryptoContext.existsDlbCrypto() && cryptoContext.getDlbCrypto().isClientFrontToBack())) {
      return;
    }

    try {
      log.debug("Decrypting path and query parameters");
      attributeConverter.decryptPathParamInUrl(wrappedRequest);
      attributeConverter.decryptQueryParamInUrl(wrappedRequest);
      log.debug("Parameters decrypted successfully");
    } catch (GeneralException e) {
      log.error("Error decrypting parameters: {}", e.getMessage());
      throw new DecryptParametersException(e);
    }
  }

}
