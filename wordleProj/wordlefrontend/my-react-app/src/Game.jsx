import { useState } from "react";
import * as api from "./api";

export default function Game({ token }) {
  const [gameId, setGameId] = useState(null);
  const [guess, setGuess] = useState("");
  const [result, setResult] = useState(null);

  const start = () => {
    api.startGame(token).then(res => {
      setGameId(res.data.gameId);
      setResult(null);
    });
  };

  const sendGuess = () => {
    api.guess(gameId, guess, token).then(res => {
      setResult(res.data);
      setGuess("");
    });
  };

  const getColor = (c) => {
    if (c === "green") return "green";
    if (c === "yellow") return "gold";
    return "lightgray";
  };

  return (
    <div>
      <h2>Wordle</h2>

      <button onClick={start}>Start Game</button>

      {gameId && (
        <>
          <p>Game ID: {gameId}</p>

          <input
            value={guess}
            onChange={(e) => setGuess(e.target.value)}
            placeholder="guess word"
          />

          <button onClick={sendGuess}>Guess</button>

          {result && (
            <div>
              <p>Attempts left: {result.attemptsLeft}</p>

              <div style={{ display: "flex" }}>
                {result.result.map((c, i) => (
                  <div
                    key={i}
                    style={{
                      width: 30,
                      height: 30,
                      margin: 2,
                      backgroundColor: getColor(c)
                    }}
                  />
                ))}
              </div>

              {result.isWin && <h3>YOU WIN 🎉</h3>}
              {result.isFinished && !result.isWin && <h3>GAME OVER</h3>}
            </div>
          )}
        </>
      )}
    </div>
  );
}