package com.mds.security.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the cryptographic context extracted from a JWT claim,
 * containing the key agreement, context ID and encrypted object values.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptographicContextDTO {

  private String keyAgreement;
  private String contextId;
  private String encryptedObject;
}
