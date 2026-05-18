package com.mds.security.interceptor;

/**
 * Marker extension of {@link FilterCustomizer} whose implementations are
 * executed <em>after</em> the controller has processed the request
 * (in {@link SecurityHandlerInterceptor#afterCompletion}).
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public interface AfterFilterCustomizer extends FilterCustomizer {

}
