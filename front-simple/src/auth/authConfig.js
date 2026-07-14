export const authConfig = {
  clientId: "spring-client-credentials-id",
  authorizationEndpoint:
    "http://localhost:8443/realms/spring-microservices-realm/protocol/openid-connect/auth",
  tokenEndpoint:
    "http://localhost:8443/realms/spring-microservices-realm/protocol/openid-connect/token",
  redirectUri: "http://localhost:5173",
  scope: "openid profile email offline_access",
  onRefreshToken: (event) => event.logIn(),
};
