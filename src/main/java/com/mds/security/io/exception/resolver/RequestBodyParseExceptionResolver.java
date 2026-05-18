package com.mds.security.io.exception.resolver;

import static com.mds.error.handler.enumerator.Action.RETRY_ON_STATE;
import static com.mds.error.handler.enumerator.Type.SECURITY;
import static com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper.createError;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DEFAULT_ERROR_TITLE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.mds.security.io.exception.CharacterEncodingException;
import com.mds.security.io.exception.RequestBodyParseException;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.model.response.ErrorResponse;
import org.springframework.stereotype.Component;

/**
 * {@link ExceptionResolver} for {@link RequestBodyParseException}.
 *
 * <p>Returns a {@code 400 BAD_REQUEST} response using the error code
 * and message carried by the exception.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class RequestBodyParseExceptionResolver implements ExceptionResolver<RequestBodyParseException> {

  /**
   * Resolves the given {@link RequestBodyParseException} into an {@link ErrorResponse}.
   *
   * @param error the exception to resolve
   * @return an {@link ErrorResponse} with HTTP 400 status
   */
  @Override
  public ErrorResponse resolve(RequestBodyParseException error) {
    return createError(error,
                       RETRY_ON_STATE,
                       SECURITY,
                       BAD_REQUEST.value(),
                       DEFAULT_ERROR_TITLE,
                       error.getCode(),
                       error.getMessage());
  }
}
