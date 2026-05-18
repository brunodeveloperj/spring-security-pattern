package com.mds.security.interceptor.exception.resolver;

import static com.mds.security.interceptor.exception.keys.InterceptorExceptionKeys.FILTER_CUSTOMIZER_EXCEPTION_CODE;
import static com.mds.security.interceptor.exception.keys.InterceptorExceptionKeys.FILTER_CUSTOMIZER_EXCEPTION_MESSAGE;
import static com.mds.error.handler.enumerator.Action.RETRY_ON_STATE;
import static com.mds.error.handler.enumerator.Type.SECURITY;
import static com.mds.error.handler.exception.helper.ErrorExceptionHandlerHelper.createError;
import static com.mds.error.handler.exception.keys.ExceptionMessageKeys.DEFAULT_ERROR_TITLE;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.mds.security.interceptor.exception.DecryptParametersException;
import com.mds.security.interceptor.exception.FilterCustomizerException;
import com.mds.error.handler.exception.GeneralException;
import com.mds.error.handler.exception.resolver.ExceptionResolver;
import com.mds.error.handler.model.response.ErrorResponse;
import com.mds.error.handler.utils.ErrorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * {@link ExceptionResolver} for {@link FilterCustomizerException}.
 *
 * <p>If the root cause is a {@link GeneralException}, the response is built
 * from its details; otherwise a default {@code 403 FORBIDDEN} error with code
 * {@code ASF_IE_0002} is returned.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class FilterCustomizerExceptionResolver implements ExceptionResolver<FilterCustomizerException> {

  /**
   * Utility for error handling and type resolution.
   */
  private final ErrorUtils errorUtils;

  /**
   * Resolves the given {@link FilterCustomizerException} into an {@link ErrorResponse}.
   *
   * @param error the exception to resolve
   * @return an {@link ErrorResponse} built from the cause or a default 403
   */
  @Override
  public ErrorResponse resolve(FilterCustomizerException error) {
    if (error.getCause() instanceof GeneralException gx) {
      return createError(gx,
                         gx.getAction(),
                         errorUtils.getType(gx),
                         gx.getHttpStatusCode(),
                         gx.getTitle(),
                         gx.getMessage(),
                         gx.getCode());
    }
    return createError(error,
                       RETRY_ON_STATE,
                       SECURITY,
                       FORBIDDEN.value(),
                       DEFAULT_ERROR_TITLE,
                       FILTER_CUSTOMIZER_EXCEPTION_CODE,
                       FILTER_CUSTOMIZER_EXCEPTION_MESSAGE);
  }
}
