package com.mds.security.interceptor.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing parameter validation settings: whether parameter
 * encryption is active, excludes list, base-key usage flag, and the
 * {@link BaseKeyDTO} marker.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamDTO {

  private boolean active;
  private List<String> excludes;
  private boolean usingBaseKey;
  private BaseKeyDTO baseKey;
}
