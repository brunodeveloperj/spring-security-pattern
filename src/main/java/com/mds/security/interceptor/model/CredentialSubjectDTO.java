package com.mds.security.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the credential subject extracted from a Verifiable
 * Credential (VC) JWT claim, containing the user's identification number
 * and name.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialSubjectDTO {

  private String number;
  private String name;
}
