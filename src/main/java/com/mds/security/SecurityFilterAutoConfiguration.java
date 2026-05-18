package com.mds.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring auto-configuration entry point for the {@code spring-security-pattern} library.
 *
 * <p>Enables component scanning on the {@code com.mds.security} package so that
 * all security filters, interceptors, exception resolvers, and I/O wrappers
 * are automatically registered in the application context when the library is
 * included as a dependency.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@ComponentScan
public class SecurityFilterAutoConfiguration {

}
