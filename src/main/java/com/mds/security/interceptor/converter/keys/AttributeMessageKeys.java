package com.mds.security.interceptor.converter.keys;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Utility class containing constant error codes and generic messages used
 * by {@link com.mds.security.interceptor.converter.AbstractAttributeBase}
 * when URL parameter validation or decryption fails.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = PRIVATE)
public class AttributeMessageKeys {

  /** Generic message for errors when processing requests. */
  public static final String FVEAMK_GENERIC_MESSAGE = "Ocorreu um erro ao processar sua solicitação. Tente novamente mais tarde.";

  /** Message key for code FVEAMK_0001. */
  public static final String FVEAMK_0001 = "FVEAMK_0001";

  /** Message associated with code FVEAMK_0001. */
  public static final String FVEAMK_0001_MESSAGE = FVEAMK_GENERIC_MESSAGE;

  /** Message key for code FVEAMK_0002. */
  public static final String FVEAMK_0002 = "FVEAMK_0002";

  /** Message associated with code FVEAMK_0002. */
  public static final String FVEAMK_0002_MESSAGE = FVEAMK_GENERIC_MESSAGE;

  /** Message key for code FVEAMK_0003. */
  public static final String FVEAMK_0003 = "FVEAMK_0003";

  /** Message associated with code FVEAMK_0003. */
  public static final String FVEAMK_0003_MESSAGE = FVEAMK_GENERIC_MESSAGE;
}
