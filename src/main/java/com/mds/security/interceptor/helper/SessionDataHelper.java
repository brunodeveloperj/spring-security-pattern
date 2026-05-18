package com.mds.security.interceptor.helper;

import static com.mds.crypto.v2.enumeration.ClientMode.BACK_TO_BACK;
import static com.mds.shared.core.pattern.utils.ObjectUtils.isStringNotBlank;
import static com.mds.shared.core.pattern.utils.ObjectUtils.nonNull;
import static com.mds.security.interceptor.exception.keys.InterceptorExceptionKeys.EXTRACTION_DATA_SESSION_EXCEPTION_CODE;
import static com.mds.security.interceptor.exception.keys.InterceptorExceptionKeys.EXTRACTION_DATA_SESSION_EXCEPTION_MESSAGE;
import static com.mds.security.interceptor.keys.SecurityKeys.AUTHENTICATION_TYPE;
import static com.mds.security.interceptor.keys.SecurityKeys.CRYPTOGRAPHIC_CONTEXT;
import static com.mds.security.interceptor.keys.SecurityKeys.ENCRYPTED_OBJECT;
import static com.mds.security.interceptor.keys.SecurityKeys.LEGACY_SESSION;
import static com.mds.security.interceptor.keys.SecurityKeys.REGISTRATION;
import static com.mds.security.interceptor.keys.SecurityKeys.SESSION_DATA;
import static com.mds.security.interceptor.keys.SecurityKeys.USER_INFORMATION;
import static com.mds.shared.core.helper.ConversionHelper.convertJsonToObject;
import static com.mds.shared.core.helper.ConversionHelper.convertObjectToJson;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.mds.crypto.v2.security.AbstractDlbCrypto;
import com.mds.crypto.v2.security.FrontToBackCrypto;
import com.mds.crypto.v2.security.JwtEncryptedObjectDecoder;
import com.mds.security.interceptor.context.SecurityCryptoContext;
import com.mds.security.interceptor.exception.ExtractionDataSessionException;
import com.mds.security.interceptor.model.CryptographicContextDTO;
import com.mds.security.interceptor.model.RequestDTO;
import com.mds.security.interceptor.model.ResponseDTO;
import com.mds.security.interceptor.model.SessionDataDTO;
import com.mds.security.interceptor.model.UserContextDTO;
import com.mds.security.io.MdsContentCachingRequestWrapper;
import com.mds.security.io.MdsContentCachingResponseWrapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Static helper that resolves session data from JWT claims or HTTP headers,
 * decrypts request bodies, and encrypts response bodies when DLB
 * FrontToBack cryptography is active.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class SessionDataHelper {

  private static final Map<String, Class<?>> session;

  static {
    session = new LinkedHashMap<>();
    session.put(LEGACY_SESSION, String.class);
    session.put(CRYPTOGRAPHIC_CONTEXT, CryptographicContextDTO.class);
  }

  /**
   * Resolves session data based on the type of cryptography and sets it in the request and security context.
   *
   * @param wrappedRequest the request wrapper containing the HTTP request
   * @param dlbCrypto      the cryptography object used for encryption/decryption
   * @param contextCrypto  the security context to store the resolved session data
   */
  public static void resolveSessionData(MdsContentCachingRequestWrapper wrappedRequest, AbstractDlbCrypto dlbCrypto, SecurityCryptoContext contextCrypto) {
    SessionDataDTO sessionData = (dlbCrypto instanceof FrontToBackCrypto) ? extractSessionData(wrappedRequest) : createSessionDataFromHeaders(wrappedRequest);

    log.info("Session data loaded successfully, DataSession = {}", convertObjectToJson(sessionData));
    wrappedRequest.setAttribute(SESSION_DATA, sessionData);
    dlbCrypto.setEncryptedObject(sessionData.getEncryptedObject());
    contextCrypto.setDlbCrypto(dlbCrypto);
  }

  /**
   * Decrypts the request body if the client uses FrontToBack cryptography.
   *
   * @param wrappedRequest the request wrapper containing the HTTP request
   * @param dlbCrypto      the cryptography object used for decryption
   */
  public static void decryptRequestIfClientFrontToBack(MdsContentCachingRequestWrapper wrappedRequest, AbstractDlbCrypto dlbCrypto, boolean enabledCrypto) {
    if (enabledCrypto && dlbCrypto.isClientFrontToBack() && wrappedRequest.hasBody()) {
      RequestDTO request = wrappedRequest.getBodyAsObject(RequestDTO.class);
      if (nonNull(request) && isStringNotBlank(request.getRequest())) {
        final String decryptedBody = new String(dlbCrypto.decryptToByteArray(Base64.getDecoder().decode(request.getRequest())), StandardCharsets.UTF_8);
        wrappedRequest.setBody(decryptedBody);
      }
    }
  }

  /**
   * Encrypts the response body if the client uses FrontToBack cryptography.
   *
   * @param wrappedResponse the response wrapper containing the HTTP response
   * @param dlbCrypto       the cryptography object used for encryption
   */
  public static void encryptResponseIfClientFrontToBack(MdsContentCachingResponseWrapper wrappedResponse, AbstractDlbCrypto dlbCrypto, boolean enabledCrypto) {
    if (wrappedResponse.hasBody()) {
      String body = wrappedResponse.getBody();
      Object responseBody = (enabledCrypto && dlbCrypto != null && dlbCrypto.isClientFrontToBack()) ? new ResponseDTO(dlbCrypto.encrypt(body)) : body;
      wrappedResponse.setBody(responseBody);
    }
  }

  /**
   * Creates a DataSessionDTO object from the headers of the request.
   *
   * @param wrappedRequest the request wrapper containing the HTTP request
   * @return a DataSessionDTO object populated with data from the headers
   */
  private static SessionDataDTO createSessionDataFromHeaders(MdsContentCachingRequestWrapper wrappedRequest) {
    log.info("DLB cryptography type is not FrontToBackCrypto, loading session attributes from headers.");
    String registration = StringUtils.defaultIfBlank(wrappedRequest.getHeader(REGISTRATION), EMPTY);
    String encryptedObject = StringUtils.defaultIfBlank(wrappedRequest.getHeader(ENCRYPTED_OBJECT), EMPTY);

    SessionDataDTO sessionData = new SessionDataDTO();
    sessionData.setDocumentNumber(registration);
    sessionData.setRegistration(registration);
    sessionData.setEncryptedObject(encryptedObject);
    return sessionData;
  }

  /**
   * Extracts the SessionDataDTO from the request headers, handling both encrypted objects and legacy session data.
   *
   * @param wrappedRequest the request wrapper containing the HTTP request
   * @return a SessionDataDTO object extracted from the request headers
   * @throws ExtractionDataSessionException if no valid session data is found in the headers
   */
  private static SessionDataDTO extractSessionData(MdsContentCachingRequestWrapper wrappedRequest) {
    log.info("Extracting data session, Authorization = {}", wrappedRequest.getHeader(AUTHORIZATION));

    SessionDataDTO sessionData = null;
    if (wrappedRequest.hasHeader(ENCRYPTED_OBJECT)) {
      addAuthenticationTypeIfAbsent(wrappedRequest, BACK_TO_BACK.name());
      sessionData = createSessionDataFromHeaders(wrappedRequest);
      return sessionData;
    }

    for (Map.Entry<String, Class<?>> entry : session.entrySet()) {
      var objectDecoder = JwtEncryptedObjectDecoder.decodeJwtClaimWithoutValidation(wrappedRequest.getHeader(AUTHORIZATION), entry.getKey(), entry.getValue());

      if (objectDecoder == null) {
        log.warn("No object found for key: {}", entry.getKey());
        continue;
      }

      if (objectDecoder instanceof String decodeToken) {
        String decodedToken = new String(Base64.getDecoder().decode(decodeToken), StandardCharsets.UTF_8).replace("\\\"", "\"");
        return convertJsonToObject(decodedToken, SessionDataDTO.class).checkFieldValidation();
      }

      if (objectDecoder instanceof CryptographicContextDTO contextDTO) {
        String encryptedObject = contextDTO.getEncryptedObject();
        log.info("Encrypted object obtained successfully, EncryptedObject = {}", encryptedObject);

        UserContextDTO userContextDTO = JwtEncryptedObjectDecoder.decodeJwtClaimWithoutValidation(wrappedRequest.getHeader(AUTHORIZATION), USER_INFORMATION, UserContextDTO.class);
        log.info("User context obtained successfully, UserContext = {}", convertObjectToJson(userContextDTO));

        return SessionDataDTO.generateInstance(userContextDTO.getCredentialSubject().getNumber(), encryptedObject, null);
      }
    }

    throw new ExtractionDataSessionException(EXTRACTION_DATA_SESSION_EXCEPTION_CODE, EXTRACTION_DATA_SESSION_EXCEPTION_MESSAGE );
  }

  /**
   * Adds the authentication type to the HTTP request if it is absent.
   *
   * @param wrappedRequest     the HTTP request
   * @param authenticationType the authentication type to add
   */
  private static void addAuthenticationTypeIfAbsent(MdsContentCachingRequestWrapper wrappedRequest, String authenticationType) {
    if (!wrappedRequest.hasHeader(AUTHENTICATION_TYPE)) {
      wrappedRequest.addHeader(AUTHENTICATION_TYPE, authenticationType);
    }
  }
}
