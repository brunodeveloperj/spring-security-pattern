package com.mds.security.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the user context extracted from the {@code vc} JWT
 * claim (Verifiable Credential), wrapping a {@link CredentialSubjectDTO}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContextDTO {

  private CredentialSubjectDTO credentialSubject;
}
