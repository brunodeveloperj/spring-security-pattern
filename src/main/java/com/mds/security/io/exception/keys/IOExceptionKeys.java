package com.mds.security.io.exception.keys;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Utility class holding constant error codes ({@code ASF_IOE_0001} through
 * {@code ASF_IOE_0008}) and user-facing messages for I/O-related exceptions
 * in the request/response wrapper classes.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = PRIVATE)
public class IOExceptionKeys {

  /**
   * Error code for character encoding exceptions. This code is used to identify errors related to character encoding issues in the system.
   */
  public static final String CHARACTER_ENCODING_EXCEPTION_CODE = "ASF_IOE_0001";

  /**
   * Error message for character encoding exceptions. This message provides a user-friendly description of the error that occurred.
   */
  public static final String CHARACTER_ENCODING_EXCEPTION_MESSAGE = "Ocorreu um erro ao processar os dados da sua requisição. Por favor, refaça a operação e caso persista o erro entrar em contato com o suporte comercial.";

  /**
   * Error code for load request body parsing exceptions. This code is used to identify errors that occur when parsing the request body.
   */
  public static final String LOAD_REQUEST_BODY_PARSE_EXCEPTION_CODE = "ASF_IOE_0002";

  /**
   * Error message for load request body parsing exceptions. This message provides a user-friendly description of the error that occurred when parsing the request body.
   */
  public static final String LOAD_REQUEST_BODY_PARSE_EXCEPTION_MESSAGE = "Ocorreu um erro ao validar os dados da solicitação. Por favor, verifique se os dados enviados estão em um formato válido e tente novamente.";

  /**
   * Error code for request body parsing exceptions. This code is used to identify errors that occur when parsing to object the request body.
   */
  public static final String REQUEST_BODY_PARSE_TO_OBJECT_EXCEPTION_CODE = "ASF_IOE_0003";

  /**
   * Error message for request body parsing exceptions. This message provides a user-friendly description of the error that occurred when parsing to object the request body.
   */
  public static final String REQUEST_BODY_PARSE_TO_OBJECT_EXCEPTION_MESSAGE = LOAD_REQUEST_BODY_PARSE_EXCEPTION_MESSAGE;

  /**
   * Error code for request body parsing validation exceptions. This code is used to identify errors that occur when parsing validation the request body.
   */
  public static final String REQUEST_BODY_PARSE_VALIDATION_EXCEPTION_CODE = "ASF_IOE_0004";

  /**
   * Error message for request body parsing validation exceptions. This message provides a user-friendly description of the error that occurred when parsing validation the request body.
   */
  public static final String REQUEST_BODY_PARSE_VALIDATION_EXCEPTION_MESSAGE = LOAD_REQUEST_BODY_PARSE_EXCEPTION_MESSAGE;

  /**
   * Error code for request body serialization exceptions. This code is used to identify errors that occur when serializing the request body.
   */
  public static final String REQUEST_BODY_SERIALIZE_EXCEPTION_CODE = "ASF_IOE_0005";

  /**
   * Error message for request body serialization exceptions. This message provides a user-friendly description of the error that occurred when serializing the request body.
   */
  public static final String REQUEST_BODY_SERIALIZE_EXCEPTION_MESSAGE = "Ocorreu um erro ao validar os dados da solicitação. Por favor, refaça a operação e caso persista o erro entrar em contato com o suporte comercial.";

  /**
   * Error code for reading response body parsing exceptions. This code is used to identify errors that occur when parsing the response body.
   */
  public static final String READING_RESPONSE_BODY_PARSE_EXCEPTION_CODE = "ASF_IOE_0006";

  /**
   * Error message for reading response body parsing exceptions. This message provides a user-friendly description of the error that occurred when parsing the response body.
   */
  public static final String READING_RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE = "Ocorreu um erro ao processar a resposta referente a solicitação realizada. Por favor, refaça a operação e caso persista o erro entrar em contato com o suporte comercial.";

  /**
   * Error code for response body parsing exceptions. This code is used to identify errors that occur when parsing the response body.
   */
  public static final String RESPONSE_BODY_PARSE_EXCEPTION_CODE = "ASF_IOE_0007";

  /**
   * Error message for response body parsing exceptions. This message provides a user-friendly description of the error that occurred when parsing the response body.
   */
  public static final String RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE = READING_RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE;

  /**
   * Error code for writing response body parsing exceptions. This code is used to identify errors that occur when writing the response body.
   */
  public static final String WRITING_RESPONSE_BODY_PARSE_EXCEPTION_CODE = "ASF_IOE_0008";

  /**
   * Error message for writing response body parsing exceptions. This message provides a user-friendly description of the error that occurred when writing the response body.
   */
  public static final String WRITING_RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE = READING_RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE;
}
