package com.mds.security.interceptor.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base DTO for legacy JWT token payload fields, mapped via Gson
 * {@code @SerializedName} annotations. Contains corporate employee ID,
 * token, branch info, registration, session ID, user list, AD login,
 * domain, logged user, terminal logic and contributor type.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayloadBaseDTO {

  @SerializedName("idCorporativoFuncionario")
  private String corporateEmployeeId;

  @SerializedName("token")
  private String token;

  @SerializedName("codigoAgencia")
  private String branchCode;

  @SerializedName("tipoAgencia")
  private String branchType;

  @SerializedName("matricula")
  private String registration;

  @SerializedName("idSessao")
  private String sessionId;

  @SerializedName("listaUsuario")
  private List<UserPayloadBaseDTO> userList;

  @SerializedName("loginAD")
  private String adLogin;

  @SerializedName("dominio")
  private String domain;

  @SerializedName("usuarioLogado")
  private String loggedUser;

  @SerializedName("logicTerminal")
  private String terminalLogic;

  @SerializedName("contributorType")
  private String contributorType;

  public boolean hasRegistration() {
    return (registration != null && !registration.isBlank());
  }

}
