# spring-security-pattern

Biblioteca Spring Boot reutilizável que centraliza filtros de segurança, interceptação de requisições, criptografia DLB (encrypt/decrypt de body e parâmetros de URL) e tratamento padronizado de exceções para APIs do ecossistema MDS.

---

## Dependência Maven

```xml
<dependency>
  <groupId>com.mds</groupId>
  <artifactId>spring-security-pattern</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

---

## Funcionalidades

- **Filtro de segurança** (`SecurityFilter`) — wraps request/response em `MdsContentCachingRequestWrapper` / `MdsContentCachingResponseWrapper` para cache de body, com encrypt transparente da resposta.
- **Interceptor MVC** (`SecurityHandlerInterceptor`) — resolve session data de JWT/headers, decripta body e parâmetros de URL, executa hooks before/after customizáveis.
- **Extensão via hooks** — interfaces `BeforeFilterCustomizer` / `AfterFilterCustomizer` para lógica customizada antes/depois do controller.
- **Criptografia DLB** — suporte a FrontToBack, BackToBack e SchedulerToBack via `AuthenticationTypeHelper`.
- **Decriptação de parâmetros** — `AttributeConverter` + `AbstractAttributeBase` decriptam path/query params com base-key + Base64 + DLB crypto.
- **OpenAPI 3.0** — configuração automática de security scheme `bearerAuth` (JWT).
- **Tratamento de exceções** — hierarquia de exceções (`DecryptParametersException`, `ExtractionDataSessionException`, `FilterCustomizerException`, `CharacterEncodingException`, `RequestBodyParseException`, `RequestBodySerializeException`, `ResponseBodyParseException`) com resolvers dedicados que produzem `ErrorResponse` padronizado.
- **Wrappers I/O** — `MdsContentCachingRequestWrapper` (override de params, headers e body) e `MdsContentCachingResponseWrapper` (leitura/escrita tipada com OWASP encoding).

---

## Configuração

### application.yml

```yaml
mds:
  security:
    crypto:
      active: true                              # habilita encrypt/decrypt
      context-path: /api                        # context path da aplicação
      validate:                                 # validação de parâmetros
        params:
          active: true
          using-base-key: true
          base-key:
            key: "ENC_"
          excludes:
            - "page"
            - "size"
      endpoint-excludes:                        # endpoints excluídos do filtro
        - /actuator/**
        - /swagger-ui/**
        - /v3/api-docs/**
```

---

## Fluxo de Segurança

```
Request HTTP
    │
    ▼
SecurityFilter (Order 1)
    │  ├─ Exclui URIs configuradas (URIFilterHelper)
    │  ├─ Wraps em MdsContentCachingRequestWrapper / ResponseWrapper
    │  └─ Encripta resposta se crypto ativo
    │
    ▼
SecurityHandlerInterceptor (preHandle)
    │  ├─ Resolve Authentication-Type → AbstractDlbCrypto
    │  ├─ Resolve Session Data (JWT claims / headers)
    │  ├─ Decripta request body (FrontToBack)
    │  ├─ Decripta path/query params (AttributeConverter)
    │  └─ Executa BeforeFilterCustomizer hooks
    │
    ▼
Controller (processamento normal)
    │
    ▼
SecurityHandlerInterceptor (afterCompletion)
    │  └─ Executa AfterFilterCustomizer hooks
    │
    ▼
Response HTTP
```

---

## Estrutura do Projeto

```
spring-security-pattern/
├── src/main/java/com/mds/security/
│   ├── SecurityFilterAutoConfiguration.java          # Auto-configuração
│   ├── filter/
│   │   ├── SecurityFilter.java                       # Filtro principal
│   │   └── helper/URIFilterHelper.java               # Helper de exclusão de URIs
│   ├── interceptor/
│   │   ├── FilterCustomizer.java                     # Interface funcional base
│   │   ├── BeforeFilterCustomizer.java               # Hook pré-controller
│   │   ├── AfterFilterCustomizer.java                # Hook pós-controller
│   │   ├── SecurityHandlerInterceptor.java           # Interceptor principal
│   │   ├── configuration/
│   │   │   ├── InterceptorConfiguration.java         # Registro do interceptor
│   │   │   ├── OpenApi30Configuration.java           # OpenAPI security scheme
│   │   │   └── properties/CryptoConfigurationProperties.java
│   │   ├── context/SecurityCryptoContext.java         # Contexto request-scoped
│   │   ├── converter/
│   │   │   ├── AbstractAttributeBase.java            # Base para decriptação de params
│   │   │   ├── AttributeConverter.java               # Decriptação de path/query params
│   │   │   ├── ServletUnwrapper.java                 # Utilitário de unwrap
│   │   │   └── keys/AttributeMessageKeys.java
│   │   ├── exception/                                # Exceções + resolvers
│   │   ├── helper/                                   # Helpers (session, auth type, customizer)
│   │   ├── keys/SecurityKeys.java                    # Constantes de headers
│   │   └── model/                                    # DTOs (Session, Token, Request, Response, etc.)
│   └── io/
│       ├── MdsContentCachingRequestWrapper.java      # Wrapper de request
│       ├── MdsContentCachingResponseWrapper.java     # Wrapper de response
│       └── exception/                                # Exceções I/O + resolvers
└── pom.xml
```

---

## Requisitos

- **Java**: 21+
- **Spring Boot**: 4.0.6+
- **Dependências internas**: `spring-crypto-pattern` (DLB crypto), `shared-core-lib` (error handler, utils)

---

## Extensibilidade

### Hooks customizados

Implemente `BeforeFilterCustomizer` ou `AfterFilterCustomizer` como `@Component`:

```java
@Component
public class AuditBeforeFilter implements BeforeFilterCustomizer {
  @Override
  public void customize(MdsContentCachingRequestWrapper request,
                        MdsContentCachingResponseWrapper response) throws GeneralException {
    // lógica de auditoria pré-controller
  }
}
```

---

## Melhorias Futuras

- Suporte a OAuth2 Resource Server
- Integração com Keycloak / RHSSO
- Multi-tenancy security
- Rate limiting por endpoint
- Métricas de segurança (Micrometer)
