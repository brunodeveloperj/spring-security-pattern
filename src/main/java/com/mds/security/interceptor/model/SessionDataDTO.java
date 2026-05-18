package com.mds.security.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DTO representing the resolved session data for the current request,
 * including the document number, encrypted object, and optional
 * {@link EncryptedDataDTO} fallback. Extends {@link TokenPayloadBaseDTO}
 * to inherit legacy token payload fields.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SessionDataDTO extends TokenPayloadBaseDTO {

  private String documentNumber;
  private EncryptedDataDTO encryptedData;
  private String encryptedObject;

  SessionDataDTO(String documentNumber, String encryptedObject, EncryptedDataDTO encryptedData) {
    this.setRegistration(documentNumber);
    this.documentNumber = documentNumber;
    this.encryptedObject = encryptedObject;
    this.encryptedData = encryptedData;
    this.checkEncryptedObject();
  }

  public static SessionDataDTO generateInstance(String documentNumber, String encryptedObject, EncryptedDataDTO encryptedData) {
    return new SessionDataDTO(documentNumber, encryptedObject, encryptedData);
  }

  public boolean existEncryptedObject() {
    return (encryptedObject != null && !encryptedObject.isBlank());
  }

  public SessionDataDTO checkFieldValidation() {
    checkEncryptedObject();
    return this;
  }

  private void checkEncryptedObject() {
    log.debug("(extractEncryptedObjectByDataSession) : validation of the encryptedObject...");
    if (!existEncryptedObject() && encryptedData != null) {
      this.encryptedObject = encryptedData.getEncryptedObject();
    }
    log.debug("(extractEncryptedObjectByDataSession) : encryptedObject is available = {} ", encryptedObject);
  }
}
