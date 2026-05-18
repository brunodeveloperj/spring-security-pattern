package com.mds.security.io;

import static com.mds.security.io.exception.keys.IOExceptionKeys.CHARACTER_ENCODING_EXCEPTION_CODE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.CHARACTER_ENCODING_EXCEPTION_MESSAGE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.LOAD_REQUEST_BODY_PARSE_EXCEPTION_CODE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.LOAD_REQUEST_BODY_PARSE_EXCEPTION_MESSAGE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.REQUEST_BODY_PARSE_TO_OBJECT_EXCEPTION_CODE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.REQUEST_BODY_PARSE_TO_OBJECT_EXCEPTION_MESSAGE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.REQUEST_BODY_PARSE_VALIDATION_EXCEPTION_CODE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.REQUEST_BODY_PARSE_VALIDATION_EXCEPTION_MESSAGE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.REQUEST_BODY_SERIALIZE_EXCEPTION_CODE;
import static com.mds.security.io.exception.keys.IOExceptionKeys.REQUEST_BODY_SERIALIZE_EXCEPTION_MESSAGE;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.mds.security.io.exception.CharacterEncodingException;
import com.mds.security.io.exception.RequestBodyParseException;
import com.mds.security.io.exception.RequestBodySerializeException;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * Extended {@link ContentCachingRequestWrapper} that supports overriding
 * parameters, headers, and body content. Caches the request body on
 * construction so it can be read multiple times, and exposes typed
 * accessor/mutator methods for body manipulation (JSON parse, serialize,
 * raw bytes). Used by the security filter and interceptor pipeline.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
public class MdsContentCachingRequestWrapper extends ContentCachingRequestWrapper {

  private byte[] overriddenContent;
  private final Map<String, String[]> overriddenParameters = new HashMap<>();
  private final Map<String, String> overriddenHeaders = new HashMap<>();
  private final ObjectMapper objectMapper = JsonMapper.builder().build();

  /**
   * Constructor that initializes the wrapper with the given HttpServletRequest. Sets the default character encoding and loads the request body into cache.
   *
   * @param request the original HttpServletRequest
   */
  public MdsContentCachingRequestWrapper(HttpServletRequest request) {
    super(request, 8192);
    addParameter(super.getParameterMap());
    addCharacterEncoding("UTF-8");
    loadRequestBody();
  }

  // ========================================
  // ============== PARAMETERS ==============
  // ========================================

  /**
   * Adds a single parameter to the overridden parameters map.
   *
   * @param name  the name of the parameter
   * @param value the value of the parameter
   */
  public void addParameter(String name, String value) {
    addParameter(name, new String[]{value});
  }

  /**
   * Adds a parameter with multiple values to the overridden parameters map.
   *
   * @param name   the name of the parameter
   * @param values the values of the parameter
   */
  public void addParameter(String name, String[] values) {
    overriddenParameters.put(name, values);
  }

  /**
   * Adds multiple parameters to the overridden parameters map.
   *
   * @param parameters a map of parameter names and their values
   */
  public void addParameter(Map<String, String[]> parameters) {
    overriddenParameters.putAll(parameters);
  }

  /**
   * Retrieves the value of a parameter by its name. If the parameter is overridden, returns the overridden value; otherwise, retrieves the original value.
   *
   * @param name the name of the parameter
   * @return the value of the parameter
   */
  @NonNull
  @Override
  public String getParameter(@NonNull String name) {
    String[] values = overriddenParameters.get(name);
    if (values != null && values.length > 0) {
      return values[0];
    }
    return super.getParameter(name);
  }

  /**
   * Retrieves an enumeration of all parameter names, including overridden ones.
   *
   * @return an enumeration of parameter names
   */
  @NonNull
  @Override
  public Enumeration<String> getParameterNames() {
    Set<String> names = new HashSet<>(overriddenParameters.keySet());
    return Collections.enumeration(names);
  }

  /**
   * Retrieves the values of a parameter by its name. If the parameter is overridden, returns the overridden values; otherwise, retrieves the original values.
   *
   * @param name the name of the parameter
   * @return an array of parameter values
   */
  @NonNull
  @Override
  public String[] getParameterValues(@NonNull String name) {
    String[] values = overriddenParameters.get(name);
    if (values != null) {
      return values;
    }
    return super.getParameterValues(name);
  }

  /**
   * Retrieves a map of all parameters, including overridden ones.
   *
   * @return a map of parameter names and their values
   */
  @NonNull
  @Override
  public Map<String, String[]> getParameterMap() {
    return overriddenParameters;
  }

  // ========================================
  // =============== HEADERS ================
  // ========================================

  /**
   * Adds a header to the overridden headers map.
   *
   * @param name  the name of the header
   * @param value the value of the header
   */
  public void addHeader(String name, String value) {
    overriddenHeaders.put(name, value);
  }

  /**
   * Checks if a header exists in the overridden headers map or in the original request.
   *
   * @param name the name of the header to check
   * @return true if the header exists, false otherwise
   */
  public boolean hasHeader(String name) {
    return overriddenHeaders.containsKey(name) || super.getHeader(name) != null;
  }

  /**
   * Retrieves the value of a header by its name. If the header is overridden, returns the overridden value; otherwise, retrieves the original value.
   *
   * @param name the name of the header
   * @return the value of the header
   */
  @NonNull
  @Override
  public String getHeader(@NonNull String name) {
    String value = overriddenHeaders.get(name);
    return value != null ? value : super.getHeader(name);
  }

  /**
   * Retrieves an enumeration of all header names, including overridden ones.
   *
   * @return an enumeration of header names
   */
  @Override
  public Enumeration<String> getHeaderNames() {
    Set<String> names = new HashSet<>();
    Enumeration<String> baseNames = super.getHeaderNames();
    while (baseNames.hasMoreElements()) {
      names.add(baseNames.nextElement());
    }
    names.addAll(overriddenHeaders.keySet());
    return Collections.enumeration(names);
  }

  /**
   * Retrieves an enumeration of all values for a specific header name. If the header is overridden, returns the overridden value; otherwise, retrieves the original values.
   *
   * @param name the name of the header
   * @return an enumeration of header values
   */
  @Override
  public Enumeration<String> getHeaders(String name) {
    if (overriddenHeaders.containsKey(name)) {
      return Collections.enumeration(Collections.singleton(overriddenHeaders.get(name)));
    }
    return super.getHeaders(name);
  }

  // ========================================
  // ============ BODY (Cache) ==============
  // ========================================

  /**
   * Sets the character encoding for the request. Throws a custom exception if the encoding is unsupported.
   *
   * @param encoding the character encoding to set
   */
  public void addCharacterEncoding(String encoding) {
    try {
      super.setCharacterEncoding(encoding);
    } catch (Exception e) {
      log.error("Unsupported character encoding: {}", encoding, e);
      throw new CharacterEncodingException(CHARACTER_ENCODING_EXCEPTION_CODE, CHARACTER_ENCODING_EXCEPTION_MESSAGE, e);
    }
  }

  /**
   * Loads the request body into the cache for later access. Throws a custom exception if the body cannot be read.
   */
  public void loadRequestBody() {
    try {
      readAndCacheBody(super.getInputStream());
    } catch (IOException e) {
      log.error("Failed to read requestWrapper body", e);
      throw new RequestBodyParseException(LOAD_REQUEST_BODY_PARSE_EXCEPTION_CODE, LOAD_REQUEST_BODY_PARSE_EXCEPTION_MESSAGE, e);
    }
  }

  /**
   * Retrieves the request body as a byte array.
   *
   * @return the request body as bytes
   * @throws IOException if an error occurs while reading the body
   */
  public byte[] getBodyAsBytes() throws IOException {
    return overriddenContent;
  }

  /**
   * Parses the request body into an object of the specified class. Throws a custom exception if the body cannot be parsed.
   *
   * @param clazz the class to parse the body into
   * @param <T>   the type of the object
   * @return the parsed object
   */
  public <T> T getBodyAsObject(Class<T> clazz) {
    try {
      byte[] content = getBodyAsBytes();
      return objectMapper.readValue(content, clazz);
    } catch (IOException e) {
      log.error("Failed to parse request body to object", e);
      throw new RequestBodyParseException(REQUEST_BODY_PARSE_TO_OBJECT_EXCEPTION_CODE, REQUEST_BODY_PARSE_TO_OBJECT_EXCEPTION_MESSAGE, e);
    }
  }

  /**
   * Retrieves the request body as a string.
   *
   * @return the request body as a string
   */
  public String getBody() {
    return getBodyAsObject(String.class);
  }

  /**
   * Sets the request body to the specified object. If the object is not a string, it is serialized into JSON. Throws a custom exception if serialization fails.
   *
   * @param target the object to set as the request body
   * @param <T>    the type of the object
   */
  public <T> void setBody(T target) {
    try {
      String requestJson = (target instanceof String t) ? t : objectMapper.writeValueAsString(target);
      overriddenContent = requestJson.getBytes(getCharacterEncoding());
    } catch (Exception e) {
      log.error("Failed to serialize request body", e);
      throw new RequestBodySerializeException(REQUEST_BODY_SERIALIZE_EXCEPTION_CODE, REQUEST_BODY_SERIALIZE_EXCEPTION_MESSAGE, e);
    }
  }

  /**
   * Checks if the request body exists and is not empty.
   *
   * @return true if the body exists and is not empty, false otherwise
   */
  public boolean hasBody() {
    try {
      return getBodyAsBytes().length > 0;
    } catch (IOException e) {
      log.error("Failed to body validation", e);
      throw new RequestBodyParseException(REQUEST_BODY_PARSE_VALIDATION_EXCEPTION_CODE, REQUEST_BODY_PARSE_VALIDATION_EXCEPTION_MESSAGE, e);
    }
  }

  /**
   * Retrieves the request body as a ServletInputStream for reading.
   *
   * @return the request body as a ServletInputStream
   * @throws IOException if an error occurs while reading the body
   */
  @NonNull
  @Override
  public ServletInputStream getInputStream() throws IOException {
    byte[] content = getBodyAsBytes();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);

    return new ServletInputStream() {
      @Override
      public int read() {
        return byteArrayInputStream.read();
      }

      @Override
      public boolean isFinished() {
        return byteArrayInputStream.available() == 0;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
      }
    };
  }

  /**
   * Retrieves the request body as a BufferedReader for reading.
   *
   * @return the request body as a BufferedReader
   * @throws IOException if an error occurs while reading the body
   */
  @NonNull
  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }

  /**
   * Reads the request body from the given InputStream and caches it.
   *
   * @param inputStream the InputStream to read the body from
   * @throws IOException if an error occurs while reading the body
   */
  private void readAndCacheBody(InputStream inputStream) throws IOException {
    BufferedReader bufferedRequest = new BufferedReader(new InputStreamReader(inputStream));
    this.overriddenContent = IOUtils.toString(bufferedRequest).getBytes(getCharacterEncoding());
  }
}
