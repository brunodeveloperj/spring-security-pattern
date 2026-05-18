package com.mds.security.interceptor.exception.resolver;

import static com.mds.error.handler.enumerator.Action.RETRY_ON_STATE;
import static com.mds.error.handler.enumerator.Type.SECURITY;
import static com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper.createError;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DEFAULT_ERROR_TITLE;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.mds.security.interceptor.exception.ExtractionDataSessionException;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.model.response.ErrorResponse;
import org.springframework.stereotype.Component;

/**
 * {@link ExceptionResolver} for {@link ExtractionDataSessionException}.
 *
 * <p>Returns a {@code 403 FORBIDDEN} response using the error code and
 * message carried by the exception.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class ExtractionDataSessionExceptionResolver implements ExceptionResolver<ExtractionDataSessionException> {

  /**
   * Resolves the {@link ExtractionDataSessionException} by creating an error response with the appropriate error code, message, and HTTP status.
   *
   * @param error the exception to resolve
   * @return an {@link ErrorResponse} containing the error details
   */
  @Override
  public ErrorResponse resolve(ExtractionDataSessionException error) {
    return createError(error,
                       RETRY_ON_STATE,
                       SECURITY,
                       FORBIDDEN.value(),
                       DEFAULT_ERROR_TITLE,
                       error.getCode(),
                       error.getMessage());
  }
}
