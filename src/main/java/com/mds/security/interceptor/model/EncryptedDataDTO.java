package com.mds.security.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO holding encrypted data fields (encrypted object, encrypted key,
 * key agreement and context ID) used as a fallback source for the
 * encrypted object in {@link SessionDataDTO}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncryptedDataDTO {

  private String encryptedObject;
  private String encryptedKey;
  private String keyAgreement;
  private String contextId;
}
