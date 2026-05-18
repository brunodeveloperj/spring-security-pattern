package com.mds.security.interceptor.converter;

import static com.mds.shared.core.pattern.utils.ObjectUtils.isStringNotBlank;
import static com.mds.security.interceptor.converter.keys.AttributeMessageKeys.FVEAMK_0001;
import static com.mds.security.interceptor.converter.keys.AttributeMessageKeys.FVEAMK_0001_MESSAGE;
import static com.mds.security.interceptor.converter.keys.AttributeMessageKeys.FVEAMK_0002;
import static com.mds.security.interceptor.converter.keys.AttributeMessageKeys.FVEAMK_0002_MESSAGE;
import static com.mds.security.interceptor.converter.keys.AttributeMessageKeys.FVEAMK_0003;
import static com.mds.security.interceptor.converter.keys.AttributeMessageKeys.FVEAMK_0003_MESSAGE;
import static com.mds.shared.core.helper.ConversionHelper.converterBase64ToString;
import static com.mds.error.handler.enumerator.Action.RETRY_ON_STATE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.mds.security.interceptor.configuration.properties.CryptoConfigurationProperties;
import com.mds.security.interceptor.context.SecurityCryptoContext;
import com.mds.error.handler.exception.GeneralException;
import com.mds.error.handler.exception.TechnicalException;
import com.mds.error.handler.utils.ThrowUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Sealed abstract base for URL parameter validation and decryption.
 *
 * <p>Validates whether path/query parameter values contain a base-key marker,
 * decodes them from Base64, and optionally decrypts via the DLB crypto session.
 * Only {@link AttributeConverter} is permitted to extend this class.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public abstract sealed class AbstractAttributeBase permits AttributeConverter {

  @Getter
  @Setter
  private boolean base64 = false;
  private final ThrowUtils throwUtil;
  private final SecurityCryptoContext cryptoContext;
  @Getter
  private final CryptoConfigurationProperties cryptoProperties;

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
  protected AbstractAttributeBase(ThrowUtils throwUtil, SecurityCryptoContext cryptoContext, CryptoConfigurationProperties cryptoProperties) {
    this.throwUtil = throwUtil;
    this.cryptoContext = cryptoContext;
    this.cryptoProperties = cryptoProperties;
  }

  /**
   *
   *
   * <pre>
   * Validation of the parameters passed in the URL, where it
   * will be checked if the parameter is encoded in base 64.
   * </pre>
   *
   * @param paramMap Map of parameters to be validated
   * @return Map of decrypted parameters
   * @throws GeneralException If an error occurs during validation
   */
  protected Object validateParamInUrl(Map<String, Object> paramMap) throws GeneralException {
    Map<String, Object> decryptMap = new HashMap<>();
    AtomicReference<GeneralException> atomicException = new AtomicReference<>();

    paramMap.forEach((key, value) -> {
      try {
        decryptMap.putIfAbsent(key, buildValueWithRealInstance(value));
      } catch (GeneralException gx) {
        atomicException.set(gx);
      } catch (Exception ex) {
        atomicException.set(new TechnicalException(FVEAMK_0001, FVEAMK_0001_MESSAGE, RETRY_ON_STATE));
      }
    });

    if (atomicException.get() != null) {
      throw atomicException.get();
    }

    return decryptMap;
  }

  /**
   *
   *
   * <pre>
   * Check whether the basic key exists in the encoded parameter.
   * <b>Example:</b>
   *  - baseKey: Xyz
   *  - paramValue: OTk5OTk5OTk5OTku<b>Xyz</b>ODg4ODg4ODg4ODg4ODg
   * response on parameter: <b>true(base key exist)</b>
   * </pre>
   *
   * @param paramValue Parameter value to be checked
   * @return true if the basic key exists, false otherwise
   * @throws GeneralException If an error occurs during the check
   */
  private boolean isContainsBaseKeyInEncodedParam(final String paramValue) throws GeneralException {
    if (!cryptoProperties.getValidate().containsBaseKeyInEncodedParam(paramValue)) {
      if (TRUE.equals(cryptoProperties.getValidate().isActiveParams())) {
        throwUtil.throwErrorBySecurity(FVEAMK_0002, FVEAMK_0002_MESSAGE);
      }
      return false;
    }
    return true;
  }

  /**
   * Builds the real value from the target value.
   *
   * @param targetValue Target value to be converted
   * @return Converted real value
   * @throws GeneralException If an error occurs during conversion
   */
  private Object buildValueWithRealInstance(final Object targetValue) throws GeneralException {
    boolean isStringArray = targetValue instanceof String[];
    String responseValue = convertTargetValueToString(targetValue, isStringArray);

    if (isStringNotBlank(responseValue)) {
      String decryptedValue = decryptBase64(responseValue);
      responseValue = isBase64() ? decryptValueWithAuthorization(decryptedValue) : decryptedValue;
    }

    return isStringArray ? new String[]{responseValue} : responseValue;
  }

  /**
   * Converts the target value to a string.
   *
   * @param targetValue           Target value to be converted
   * @param instanceOfStringArray Indicates if the target value is a string array
   * @return Converted value as a string
   */
  private String convertTargetValueToString(final Object targetValue, final boolean instanceOfStringArray) {
    return instanceOfStringArray ? Arrays.stream((String[]) targetValue).findFirst().orElse(EMPTY) : (String) targetValue;
  }

  /**
   * Decrypts a base64 encoded value.
   *
   * @param responseValue Value to be decrypted
   * @return Decrypted value
   * @throws GeneralException If an error occurs during decryption
   */
  private String decryptBase64(String responseValue) throws GeneralException {
    if (!isContainsBaseKeyInEncodedParam(responseValue)) {
      setBase64(false);
      return responseValue;
    }

    String decryptBase64 = converterBase64ToString(cryptoProperties.getValidate().removeBaseKeyOfEncodedParam(responseValue), false);
    if (decryptBase64 == null || decryptBase64.isBlank()) {
      throwUtil.throwErrorBySecurity(FVEAMK_0003, FVEAMK_0003_MESSAGE);
    }

    setBase64(true);
    return decryptBase64;
  }

  /**
   * Decrypts a value with authorization.
   *
   * @param target Value to be decrypted
   * @return Decrypted value
   */
  private String decryptValueWithAuthorization(String target) {
    try {
      return cryptoContext.getDlbCrypto().decrypt(target);
    } catch (Exception e) {
      log.error("An error occurred while decrypting the request", e);
    }
    return target;
  }
}
