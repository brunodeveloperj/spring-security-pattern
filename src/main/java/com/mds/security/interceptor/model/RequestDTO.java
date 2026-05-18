package com.mds.security.interceptor.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO wrapping an encrypted request body payload (Base64-encoded) received
 * from FrontToBack clients, decrypted by {@link com.mds.security.interceptor.helper.SessionDataHelper}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO implements Serializable {

  private String request;
}
