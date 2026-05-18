package com.mds.security.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a single base-key string used to identify encoded
 * parameter markers in URL path/query values.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseKeyDTO {

  private String key;

}
