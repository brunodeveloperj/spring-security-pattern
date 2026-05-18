package com.mds.security.interceptor.context;

import com.mds.crypto.v2.security.AbstractDlbCrypto;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Request-scoped Spring component that holds the {@link AbstractDlbCrypto}
 * instance resolved for the current HTTP request, ensuring thread-safe
 * access to cryptographic operations throughout the interceptor pipeline.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@Component
@RequestScope // Ensures a new instance is created for each HTTP request and avoids thread unsafe.
public class SecurityCryptoContext {

  /**
   * Represents the cryptographic instance used for security operations. It is injected or set
   * externally and can be null if not initialized.
   */
  private AbstractDlbCrypto dlbCrypto;

  /**
   * Checks if the cryptographic instance (dlbCrypto) exists.
   *
   * @return true if dlbCrypto is not null, false otherwise.
   */
  public boolean existsDlbCrypto() {
    return dlbCrypto != null;
  }
}
