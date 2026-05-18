package com.mds.security.interceptor.keys;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class holding constant HTTP header names and request attribute keys
 * used by the security interceptor pipeline for session data resolution and
 * authentication type handling.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityKeys {

  /** Key for the encrypted object header. */
  public static final String ENCRYPTED_OBJECT = "x-encryptedobject";

  /** Key for the logged user header. */
  public static final String LOGGED_USER = "logged-user";

  /** Key for the legacy session header. */
  public static final String LEGACY_SESSION = "legacy-session";

  /** Key for the cryptographic context header. */
  public static final String CRYPTOGRAPHIC_CONTEXT = "cryptographicContext";

  /** Key for the Apigee user information header. */
  public static final String USER_INFORMATION = "vc";

  /** Key for the session data attribute. */
  public static final String SESSION_DATA = "session-data";

  /** Key for the registration header. */
  public static final String REGISTRATION = "registration";

  /** Key for the authentication type header. */
  public static final String AUTHENTICATION_TYPE = "Authentication-Type";
}
