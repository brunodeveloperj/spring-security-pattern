package com.mds.security.io.exception.resolver;

import static com.mds.error.handler.enumerator.Action.RETRY_ON_STATE;
import static com.mds.error.handler.enumerator.Type.SECURITY;
import static com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper.createError;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DEFAULT_ERROR_TITLE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.mds.security.io.exception.RequestBodyParseException;
import com.mds.security.io.exception.RequestBodySerializeException;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.model.response.ErrorResponse;
import org.springframework.stereotype.Component;

/**
 * {@link ExceptionResolver} for {@link RequestBodySerializeException}.
 *
 * <p>Returns a {@code 500 INTERNAL_SERVER_ERROR} response using the
 * error code and message carried by the exception.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class RequestBodySerializeExceptionResolver implements ExceptionResolver<RequestBodySerializeException> {

  /**
   * Resolves the given {@link RequestBodySerializeException} into an {@link ErrorResponse}.
   *
   * @param error the exception to resolve
   * @return an {@link ErrorResponse} with HTTP 500 status
   */
  @Override
  public ErrorResponse resolve(RequestBodySerializeException error) {
    return createError(error,
                       RETRY_ON_STATE,
                       SECURITY,
                       INTERNAL_SERVER_ERROR.value(),
                       DEFAULT_ERROR_TITLE,
                       error.getCode(),
                       error.getMessage());}
}
