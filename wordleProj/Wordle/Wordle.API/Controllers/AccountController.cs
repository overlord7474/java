using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Wordle.API.DTOs;
using Wordle.Infrastructure.Data;
using Wordle.API.DTOs;
using Wordle.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace Wordle.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly AppDbContext _db;

        private readonly JwtService _jwt;

        public AccountController(AppDbContext db, JwtService jwt)
        {
            _db = db;
            _jwt = jwt;
        }

        [HttpPost("login")]
        public IActionResult Login(LoginDto dto)
        {
            var user = _db.Users.FirstOrDefault(x =>
                x.Email == dto.Email &&
                x.PasswordHash == dto.Password);

            if (user == null)
                return Unauthorized("Invalid login");

            var token = _jwt.GenerateToken(user.Id, user.Email);

            return Ok(new { token });
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register(RegisterDto dto)
        {
            var userExists = _db.Users.Any(x => x.Email == dto.Email);

            if (userExists)
                return BadRequest("User already exists");

            var user = new User
            {
                Email = dto.Email,
                PasswordHash = dto.Password
            };

            _db.Users.Add(user);
            await _db.SaveChangesAsync();

            var stat = new Statistic
            {
                UserId = user.Id,
                GamesPlayed = 0,
                Wins = 0,
                CurrentStreak = 0,
                MaxStreak = 0,
                TotalPoints = 0
            };

            _db.Statistics.Add(stat);
            await _db.SaveChangesAsync();

            return Ok("User registered");
        }
    }
}
