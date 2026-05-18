package com.mds.security.interceptor.configuration;

import com.mds.security.interceptor.SecurityHandlerInterceptor;
import com.mds.security.interceptor.configuration.properties.CryptoConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * {@link WebMvcConfigurer} that registers the {@link SecurityHandlerInterceptor}
 * on all paths ({@code /**}), excluding endpoints listed in
 * {@link CryptoConfigurationProperties#getEndpointExcludes()}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class InterceptorConfiguration implements WebMvcConfigurer {

  private final CryptoConfigurationProperties cryptoProperties;
  private final SecurityHandlerInterceptor securityHandlerInterceptor;

  /**
   * Adds the security interceptor to the registry. This interceptor will be applied to all paths
   * except those specified in the crypto properties.
   *
   * @param registry The interceptor registry to which the security interceptor is added
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(securityHandlerInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(cryptoProperties.getEndpointExcludes());
    log.info("SecurityHandlerInterceptor added to registry");
  }
}
