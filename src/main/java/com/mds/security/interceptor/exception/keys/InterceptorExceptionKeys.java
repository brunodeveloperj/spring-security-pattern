package com.mds.security.interceptor.exception.keys;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Utility class holding constant error codes and user-facing messages for
 * the interceptor exception hierarchy ({@code ASF_IE_0001} through
 * {@code ASF_IE_0003}).
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = PRIVATE)
public class InterceptorExceptionKeys {

  /**
   * Error code for exceptions when decrypting parameters.
   *
   * <p>This code is used to identify errors related to failures in decrypting parameters sent in
   * the request.
   */
  public static final String DECRYPT_PARAMETERS_EXCEPTION_CODE = "ASF_IE_0001";

  /**
   * Friendly message for exceptions when decrypting parameters.
   *
   * <p>This message is shown to the client when an error occurs while processing the parameters
   * sent in the request.
   */
  public static final String DECRYPT_PARAMETERS_EXCEPTION_MESSAGE = "Não foi possível processar os parâmetros enviados. Verifique se os dados estão corretos e tente novamente.";

  /**
   * Error code for exceptions when customizing filters.
   *
   * <p>This code is used to identify errors related to failures in customizing security filters.
   */
  public static final String FILTER_CUSTOMIZER_EXCEPTION_CODE = "ASF_IE_0002";

  /**
   * Friendly message for exceptions when customizing filters.
   *
   * <p>This message is shown to the client when a security-related error occurs while processing
   * the request.
   */
  public static final String FILTER_CUSTOMIZER_EXCEPTION_MESSAGE = "Não foi possível processar sua solicitação por motivos de segurança. Por favor, revise os dados enviados e tente novamente.";

  /**
   * Error code for exceptions when extracting data from the session.
   *
   * <p>This code is used to identify errors related to failures in extracting data from the
   * session.
   */
  public static final String EXTRACTION_DATA_SESSION_EXCEPTION_CODE = "ASF_IE_0003";

  /**
   * Friendly message for exceptions when extracting data from the session.
   *
   * <p>This message is shown to the client when an error occurs while extracting data from the
   * session.
   */
  public static final String EXTRACTION_DATA_SESSION_EXCEPTION_MESSAGE = "Não foi possível validar os dados da sessão. Por favor, tente novamente e caso persista o erro entre em contato com o suporte comercial..";
}
