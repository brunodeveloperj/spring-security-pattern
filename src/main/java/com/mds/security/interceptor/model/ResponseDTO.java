package com.mds.security.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO wrapping an encrypted response body payload sent back to
 * FrontToBack clients after encryption by
 * {@link com.mds.security.interceptor.helper.SessionDataHelper}.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

  private String response;
}
