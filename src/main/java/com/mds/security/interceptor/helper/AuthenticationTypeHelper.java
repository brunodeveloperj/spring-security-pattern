package com.mds.security.interceptor.helper;

import static lombok.AccessLevel.PRIVATE;

import com.mds.crypto.v2.security.AbstractDlbCrypto;
import com.mds.crypto.v2.security.BackToBackDlbCrypto;
import com.mds.crypto.v2.security.FrontToBackCrypto;
import com.mds.crypto.v2.security.SchedulerToBackDlbCrypto;
import lombok.NoArgsConstructor;

/**
 * Helper that maps the {@code Authentication-Type} header value to the
 * appropriate {@link AbstractDlbCrypto} implementation:
 * {@link SchedulerToBackDlbCrypto}, {@link BackToBackDlbCrypto}, or
 * {@link FrontToBackCrypto} (default).
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = PRIVATE)
public class AuthenticationTypeHelper {

  /**
   * Determines the appropriate {@link AbstractDlbCrypto} implementation based on the provided
   * authentication type.
   *
   * <p>This method evaluates the given authentication type and returns an instance of the
   * corresponding cryptographic implementation. If the authentication type matches {@code
   * SCHEDULER_TO_BACK}, it returns {@link SchedulerToBackDlbCrypto}. If it matches {@code
   * BACK_TO_BACK}, it returns {@link BackToBackDlbCrypto}. For all other cases, it defaults to
   * returning {@link FrontToBackCrypto}.
   *
   * @param authenticationType the type of authentication to evaluate
   * @return an instance of {@link AbstractDlbCrypto} corresponding to the authentication type
   */
  public static AbstractDlbCrypto getDlbCrypto(String authenticationType) {
    return switch (authenticationType) {
      case "SCHEDULER_TO_BACK" -> new SchedulerToBackDlbCrypto();
      case "BACK_TO_BACK" -> new BackToBackDlbCrypto();
      default -> new FrontToBackCrypto();
    };
  }

// TODO: Implement isFrontToBackAndSafety() if needed in future.
}
