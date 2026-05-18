package com.mds.security.interceptor.model;

import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO wrapping a {@link ParamDTO} and providing convenience methods to
 * check whether parameter encryption is active, whether a base-key marker
 * is present in an encoded value, and whether a parameter name is in the
 * excludes list.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateDTO {

  private ParamDTO params;

  public Boolean isActiveParams() {
    return Optional.ofNullable(params).map(ParamDTO::isActive).orElse(TRUE);
  }

  public boolean containsBaseKeyInEncodedParam(String encodedParam) {
    return Optional.ofNullable(params).map(ParamDTO::getBaseKey).map(BaseKeyDTO::getKey).stream().anyMatch(encodedParam::contains);
  }

  public String removeBaseKeyOfEncodedParam(String encodedParam) {
    return Optional.ofNullable(params).map(ParamDTO::getBaseKey).map(BaseKeyDTO::getKey).map(key -> encodedParam.replace(key, EMPTY)).orElse(EMPTY);
  }

  public boolean containsParamInExcludes(String paramName) {
    return Optional.ofNullable(params).map(ParamDTO::getExcludes).orElse(new ArrayList<>()).stream().anyMatch(paramName::equalsIgnoreCase);
  }
}
