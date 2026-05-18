package com.mds.security.interceptor;

/**
 * Marker extension of {@link FilterCustomizer} whose implementations are
 * executed <em>before</em> the controller processes the request
 * (in {@link SecurityHandlerInterceptor#preHandle}).
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public interface BeforeFilterCustomizer extends FilterCustomizer {

}
