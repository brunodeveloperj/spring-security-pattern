package com.mds.security.interceptor.configuration.properties;

import com.mds.security.interceptor.model.ValidateDTO;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for cryptographic settings, bound to the
 * {@code mds.security.crypto} prefix.
 *
 * <p>Controls whether request/response encryption is active, defines the
 * application context path stripped from URIs, validation rules for
 * encoded parameters ({@link ValidateDTO}), and the list of endpoint
 * patterns excluded from the security filter pipeline.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@Configuration
@ConfigurationProperties("mds.security.crypto")
public class CryptoConfigurationProperties {

  /**
   * The context path for the cryptographic operations. This is typically the base path of the
   * application where cryptographic operations are applied.
   */
  private String contextPath = "";

  /** Flag indicating if cryptographic operations are active. */
  private boolean active;

  /** Validation settings for cryptographic operations. */
  private ValidateDTO validate;

  /** List of endpoint URIs to be excluded from cryptographic operations. */
  private List<String> endpointExcludes = List.of();
}
