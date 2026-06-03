using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Wordle.Infrastructure.Data;
using Wordle.Domain.Models;
using System.Security.Claims;

namespace Wordle.API.Controllers;

[Authorize]
[ApiController]
[Route("api/[controller]")]
public class GamesController : ControllerBase
{
    private readonly AppDbContext _db;

    public GamesController(AppDbContext db)
    {
        _db = db;
    }

    [HttpGet]
    public IActionResult GetGames()
    {
        int userId = int.Parse(User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier).Value);

        var games = _db.Games
            .Where(g => g.UserId == userId)
            .Select(g => new
            {
                g.Id,
                g.Attempts,
                g.IsWon,
                g.PlayedAt
            })
            .ToList();

        return Ok(games);
    }

    [HttpGet("{id}")]
    public IActionResult GetGame(int id)
    {
        int userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier).Value);

        var game = _db.Games
            .FirstOrDefault(g => g.Id == id && g.UserId == userId);

        if (game == null)
            return NotFound();

        return Ok(game);
    }

    [HttpPost("finish")]
    public IActionResult FinishGame(int gameId, bool isWin, int attempts)
    {
        int userId = int.Parse(User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier).Value);

        var game = _db.Games.FirstOrDefault(g => g.Id == gameId && g.UserId == userId);

        if (game == null)
            return NotFound();

        // update game
        game.IsWon = isWin;
        game.Attempts = attempts;

        // get stats
        var stats = _db.Statistics.FirstOrDefault(s => s.UserId == userId);

        if (stats == null)
            return NotFound();

        stats.GamesPlayed++;

        if (isWin)
        {
            stats.Wins++;
            stats.CurrentStreak++;

            if (stats.CurrentStreak > stats.MaxStreak)
                stats.MaxStreak = stats.CurrentStreak;

            stats.TotalPoints += Math.Max(1, 7 - attempts);
        }
        else
        {
            stats.CurrentStreak = 0;
        }

        _db.SaveChanges();

        return Ok(new
        {
            message = "Game updated",
            pointsAdded = isWin ? Math.Max(1, 7 - attempts) : 0
        });
    }

    [HttpPost("start")]
    public IActionResult StartGame()
    {
        int userId = int.Parse(User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier).Value);

        var words = new[] { "apple", "table", "chair", "world", "train" };

        var random = new Random();
        var word = words[random.Next(words.Length)];

        var game = new Game
        {
            UserId = userId,
            Word = word,
            Attempts = 0,
            IsWon = false,
            PlayedAt = DateTime.UtcNow
        };

        _db.Games.Add(game);
        _db.SaveChanges();

        return Ok(new
        {
            gameId = game.Id,
            maxAttempts = 6,
            wordLength = word.Length
        });
    }

    [HttpPost("guess")]
    public IActionResult Guess(int gameId, string guess)
    {
        int userId = int.Parse(User.FindFirst(System.Security.Claims.ClaimTypes.NameIdentifier).Value);

        // 1. validate input
        if (string.IsNullOrWhiteSpace(guess))
            return BadRequest("Guess cannot be empty");

        guess = guess.ToLower().Trim();

        // 2. get game
        var game = _db.Games.FirstOrDefault(g => g.Id == gameId && g.UserId == userId);

        if (game == null)
            return NotFound("Game not found");

        // 3. prevent playing finished game
        if (game.IsWon)
            return BadRequest("Game already completed");

        // 4. max attempts rule
        if (game.Attempts >= 6)
            return BadRequest("Max attempts reached");

        // 5. word validation
        if (guess.Length != game.Word.Length)
            return BadRequest($"Word must be {game.Word.Length} letters");

        game.Attempts++;

        string target = game.Word.ToLower();

        var result = new List<string>();

        for (int i = 0; i < target.Length; i++)
        {
            if (guess[i] == target[i])
                result.Add("green");
            else if (target.Contains(guess[i]))
                result.Add("yellow");
            else
                result.Add("gray");
        }

        bool isWin = guess == target;

        if (isWin)
        {
            game.IsWon = true;

            // update stats immediately
            var stats = _db.Statistics.FirstOrDefault(s => s.UserId == userId);

            if (stats != null)
            {
                stats.GamesPlayed++;
                stats.Wins++;
                stats.CurrentStreak++;

                if (stats.CurrentStreak > stats.MaxStreak)
                    stats.MaxStreak = stats.CurrentStreak;

                stats.TotalPoints += Math.Max(1, 7 - game.Attempts);
            }
        }
        else if (game.Attempts >= 6)
        {
            // auto-fail game
            game.IsWon = false;

            var stats = _db.Statistics.FirstOrDefault(s => s.UserId == userId);

            if (stats != null)
            {
                stats.GamesPlayed++;
                stats.CurrentStreak = 0;
            }
        }

        _db.SaveChanges();

        return Ok(new
        {
            guess,
            result,
            attemptsLeft = 6 - game.Attempts,
            isWin,
            isFinished = game.IsWon || game.Attempts >= 6
        });
    }
}
