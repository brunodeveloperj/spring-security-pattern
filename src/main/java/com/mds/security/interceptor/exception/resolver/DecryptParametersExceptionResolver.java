package com.mds.security.interceptor.exception.resolver;

import static com.mds.security.interceptor.exception.keys.InterceptorExceptionKeys.DECRYPT_PARAMETERS_EXCEPTION_CODE;
import static com.mds.security.interceptor.exception.keys.InterceptorExceptionKeys.DECRYPT_PARAMETERS_EXCEPTION_MESSAGE;
import static com.mds.error.handler.enumerator.Action.RETRY_ON_STATE;
import static com.mds.error.handler.enumerator.Type.SECURITY;
import static com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper.createError;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DEFAULT_ERROR_TITLE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.mds.security.interceptor.exception.DecryptParametersException;
import com.mds.error.handler.exception.GeneralException;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.model.response.ErrorResponse;
import com.mds.error.handler.utils.ErrorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * {@link ExceptionResolver} for {@link DecryptParametersException}.
 *
 * <p>If the root cause is a {@link GeneralException}, the response is built
 * from its details; otherwise a default {@code BAD_REQUEST} error with code
 * {@code ASF_IE_0001} is returned.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class DecryptParametersExceptionResolver implements ExceptionResolver<DecryptParametersException> {

  /**
   * Utility for error handling and type resolution.
   */
  private final ErrorUtils errorUtils;

  /**
   * Resolves a {@link DecryptParametersException} into an {@link ErrorResponse}.
   *
   * <p>If the cause is a {@link GeneralException}, builds the response using its details.
   * Otherwise, returns a default error response for decryption errors.
   *
   * @param error the exception to resolve
   * @return the error response to be sent to the client
   */
  @Override
  public ErrorResponse resolve(DecryptParametersException error) {
    if (error.getCause() instanceof GeneralException gx) {
      return createError(gx,
                         gx.getAction(),
                         errorUtils.getType(gx),
                         gx.getHttpStatusCode(),
                         gx.getTitle(),
                         gx.getCode(),
                         gx.getMessage());
    }
    return createError(error,
                       RETRY_ON_STATE,
                       SECURITY,
                       BAD_REQUEST.value(),
                       DEFAULT_ERROR_TITLE,
                       DECRYPT_PARAMETERS_EXCEPTION_CODE,
                       DECRYPT_PARAMETERS_EXCEPTION_MESSAGE);
  }
}
