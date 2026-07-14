/* eslint-disable no-unused-vars */
import { useContext, useEffect, useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import { AuthContext } from "react-oauth2-code-pkce";
import testService from "./services/testService";

function App() {
  const [count, setCount] = useState(0);
  const [message, setMessage] = useState("");
  const { token, tokenData, logIn, logOut, isAuthenticated } =
    useContext(AuthContext);

  useEffect(() => {
    localStorage.setItem("token", token);
    console.log("new token added !");
    fetchData();
  }, [token]);

  const fetchData = async () => {
    const data = await testService.test();
    setMessage(data);
    console.log("message :", data);
  };

  return (
    <>
      {token ? (
        <>
          <div>
            <a href='https://vite.dev' target='_blank'>
              <img src={viteLogo} className='logo' alt='Vite logo' />
            </a>
            <a href='https://react.dev' target='_blank'>
              <img src={reactLogo} className='logo react' alt='React logo' />
            </a>
          </div>
          <h1>data : </h1>
          <h3>{message}</h3>
          <div className='card'>
            <button onClick={() => logOut()}>Logout</button>
            <p>
              Edit <code>src/App.jsx</code> and save to test HMR
            </p>
          </div>
          <p className='read-the-docs'>
            Click on the Vite and React logos to learn more
          </p>
        </>
      ) : (
        <button onClick={() => logIn()}>Login</button>
      )}
    </>
  );
}

export default App;
