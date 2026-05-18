package com.mds.security.io;

import static com.mds.security.io.exception.keys.IOExceptionKeys.READING_RESPONSE_BODY_PARSE_EXCEPTION_CODE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.READING_RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.RESPONSE_BODY_PARSE_EXCEPTION_CODE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.WRITING_RESPONSE_BODY_PARSE_EXCEPTION_CODE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.WRITING_RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE;
import static org.owasp.encoder.Encode.forHtmlContent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mds.security.io.exception.ResponseBodyParseException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * Extended {@link ContentCachingResponseWrapper} that exposes typed
 * accessor/mutator methods for response body manipulation, including
 * JSON parse, string retrieval and OWASP-encoded write. Applies UTF-8
 * encoding by default. Used by the security filter and interceptor pipeline.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class MdsContentCachingResponseWrapper extends ContentCachingResponseWrapper {

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Constructor that initializes the wrapper with the given HttpServletResponse. Sets the default character encoding to UTF-8.
   *
   * @param response the original HttpServletResponse
   */
  public MdsContentCachingResponseWrapper(HttpServletResponse response) {
    super(response);
    addCharacterEncoding("UTF-8");
  }

  // ========================================
  // ============ BODY (Cache) ==============
  // ========================================

  /**
   * Sets the character encoding for the response.
   *
   * @param encoding the character encoding to set
   */
  public void addCharacterEncoding(String encoding) {
    super.setCharacterEncoding(encoding);
  }

  /**
   * Retrieves the response body as a string. Ensures the response is flushed before accessing the body.
   *
   * @return the response body as a string, or an empty string if the body is empty
   * @throws ResponseBodyParseException if an error occurs while reading the response body
   */
  public String getBody() {
    try {
      flushBuffer(); // Ensure the response is flushed before modifying it
      byte[] content = getContentAsByteArray();
      return content.length > 0 ? new String(content, getCharacterEncoding()) : "";
    } catch (IOException e) {
      log.error("Error reading response body", e);
      throw new ResponseBodyParseException(READING_RESPONSE_BODY_PARSE_EXCEPTION_CODE, READING_RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE, e);
    }
  }

  /**
   * Parses the response body into an object of the specified type.
   *
   * @param targetType the class type to parse the body into
   * @param <T>        the type of the object
   * @return the parsed object
   * @throws ResponseBodyParseException if an error occurs while parsing the response body
   */
  public <T> T getBody(Class<T> targetType) {
    try {
      return objectMapper.readValue(getBody(), targetType);
    } catch (IOException e) {
      log.error("Error parsing response body", e);
      throw new ResponseBodyParseException(RESPONSE_BODY_PARSE_EXCEPTION_CODE, RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE, e);
    }
  }

  /**
   * Checks if the response body exists and is not blank.
   *
   * @return true if the body exists and is not blank, false otherwise
   */
  public boolean hasBody() {
    return StringUtils.isNoneBlank(getBody());
  }

  /**
   * Sets the response body to the specified object. If the object is not a string, it is serialized into JSON. Resets the response buffer and updates the content type to JSON.
   *
   * @param target the object to set as the response body
   * @param <T>    the type of the object
   * @throws ResponseBodyParseException if an error occurs while writing the response body
   */
  public <T> void setBody(T target) {
    try {
      String responseJson = ((target instanceof String t) ? t : objectMapper.writeValueAsString(target));
      resetBuffer();
      setContentType("application/json;charset=UTF-8");
      getWriter().write(forHtmlContent(responseJson));
      copyBodyToResponse();
    } catch (IOException e) {
      log.error("Error writing response body", e);
      throw new ResponseBodyParseException(WRITING_RESPONSE_BODY_PARSE_EXCEPTION_CODE, WRITING_RESPONSE_BODY_PARSE_EXCEPTION_MESSAGE, e);
    }
  }
}
