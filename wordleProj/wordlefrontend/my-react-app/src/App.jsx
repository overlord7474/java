import { useState } from "react";
import * as api from "./api";
import Game from "./Game";

function App() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [token, setToken] = useState(localStorage.getItem("token"));

  const login = () => {
    api.login(email, password).then(res => {
      localStorage.setItem("token", res.data.token);
      setToken(res.data.token);
    });
  };

  if (token) {
    return <Game token={token} />;
  }

  return (
    <div>
      <h2>Login</h2>

      <input
        placeholder="email"
        onChange={(e) => setEmail(e.target.value)}
      />

      <input
        placeholder="password"
        type="password"
        onChange={(e) => setPassword(e.target.value)}
      />

      <button onClick={login}>Login</button>
    </div>
  );
}

export default App;