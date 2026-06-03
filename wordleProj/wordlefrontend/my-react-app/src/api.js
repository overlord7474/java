import axios from "axios";

const baseUrl = "https://localhost:50001/api";

export const login = (email, password) => {
  return axios.post(`${baseUrl}/account/login`, {
    email,
    password
  });
};

export const register = (email, password) => {
  return axios.post(`${baseUrl}/account/register`, {
    email,
    password
  });
};

export const startGame = (token) => {
  return axios.post(
    `${baseUrl}/games/start`,
    {},
    {
      headers: { Authorization: `Bearer ${token}` }
    }
  );
};

export const guess = (gameId, guessWord, token) => {
  return axios.post(
    `${baseUrl}/games/guess?gameId=${gameId}&guess=${guessWord}`,
    {},
    {
      headers: { Authorization: `Bearer ${token}` }
    }
  );
};