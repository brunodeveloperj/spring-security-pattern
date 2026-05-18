package com.mds.security.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a single user entry in the legacy JWT token payload's
 * user list ({@code listaUsuario}), containing register number, code and name.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayloadBaseDTO {

  private String register;
  private String code;
  private String name;

}
