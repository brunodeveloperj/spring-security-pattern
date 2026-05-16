# spring-security-pattern

A reusable Spring Boot library that centralizes authentication, authorization and Spring Security configuration patterns across APIs.

## Features

- JWT Authentication
- Authorization
- Security filters
- CORS
- CSRF configuration
- Role validation
- Method Security
- AuthenticationEntryPoint
- AccessDeniedHandler

---

## Security Flow

Request
↓
Security Filter
↓
JWT Validation
↓
Authorization
↓
Controller

---

## Future Features

- OAuth2
- SSO
- Keycloak integration
- Multi tenant security
