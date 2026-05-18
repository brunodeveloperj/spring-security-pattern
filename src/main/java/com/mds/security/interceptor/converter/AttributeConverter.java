package com.mds.security.interceptor.converter;

import static com.mds.shared.core.helper.ConversionHelper.convertObjectToJson;

import com.mds.security.interceptor.configuration.properties.CryptoConfigurationProperties;
import com.mds.security.interceptor.context.SecurityCryptoContext;
import com.mds.security.io.MdsContentCachingRequestWrapper;
import com.mds.error.handler.exception.GeneralException;
import com.mds.error.handler.utils.ThrowUtils;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Spring component responsible for decrypting path and query parameters in
 * HTTP requests. Extends {@link AbstractAttributeBase} to leverage
 * base-key validation and DLB crypto decryption.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
public final class AttributeConverter extends AbstractAttributeBase {

  /**
   *
   *
   * <pre>
   * The constructor of the class is responsible for passing
   * the injections to its final attributes.
   * </pre>
   *
   * @param throwUtil        Utility for throwing exceptions
   * @param cryptoContext    Security filter handler
   * @param cryptoProperties Cryptography configuration properties
   */
  private AttributeConverter(ThrowUtils throwUtil, SecurityCryptoContext cryptoContext, CryptoConfigurationProperties cryptoProperties) {
    super(throwUtil, cryptoContext, cryptoProperties);
  }

  /**
   * Method responsible for decrypting the path parameter contained in the URL.
   *
   * @param wrappedRequest The HTTP request containing the path parameters
   * @throws GeneralException If an error occurs during decryption
   */
  @SuppressWarnings("unchecked")
  public void decryptPathParamInUrl(MdsContentCachingRequestWrapper wrappedRequest) throws GeneralException {
    Map<String, Object> pathVariables = (Map<String, Object>) wrappedRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    if (pathVariables == null || pathVariables.isEmpty()) {
      return;
    }

    log.info("Decoding PathParams assigned in the URL.");
    log.info("Existing path parameters: {}", convertObjectToJson(pathVariables));
    log.info("Starting decryption of PathParams...");

    Object decryptMap = validateParamInUrl(pathVariables);

    log.info("Decoded PathParams: {}", convertObjectToJson(decryptMap));
    wrappedRequest.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, decryptMap);
  }

  /**
   * Method responsible for decrypting the query parameter contained in the URL.
   *
   * @param wrappedRequest The HTTP request containing the query parameters
   * @throws GeneralException If an error occurs during decryption
   */
  @SuppressWarnings("unchecked")
  public void decryptQueryParamInUrl(MdsContentCachingRequestWrapper wrappedRequest) throws GeneralException {
    if (wrappedRequest.getParameterMap().isEmpty()) {
      return;
    }

    log.info("Decoding QueryParams assigned in the URL.");
    log.info("Checking existing query parameters = {}", convertObjectToJson(wrappedRequest.getParameterMap()));
    log.info("Starting decryption of QueryParams...");

    var filteredParam = wrappedRequest.getParameterMap().entrySet().stream().filter(p -> !getCryptoProperties().getValidate().containsParamInExcludes(p.getKey())).collect(Collectors.toMap(Map.Entry::getKey, m -> (Object) m.getValue()));

    Map<String, String[]> decryptMap = (Map<String, String[]>) validateParamInUrl(filteredParam);

    log.info("Decoded QueryParams: {}", convertObjectToJson(decryptMap));
    wrappedRequest.addParameter(decryptMap);
  }
}
