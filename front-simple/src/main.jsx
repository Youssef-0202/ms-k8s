import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import { authConfig } from "./auth/authConfig.js";
import { AuthProvider } from "react-oauth2-code-pkce";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <AuthProvider authConfig={authConfig}>
      <App />
    </AuthProvider>
  </StrictMode>
);
