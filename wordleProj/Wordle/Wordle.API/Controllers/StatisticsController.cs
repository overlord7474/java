using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Wordle.Infrastructure.Data;
using System.Security.Claims;

namespace Wordle.API.Controllers;

[Authorize]
[ApiController]
[Route("api/[controller]")]
public class StatisticsController : ControllerBase
{
    private readonly AppDbContext _db;

    public StatisticsController(AppDbContext db)
    {
        _db = db;
    }

    [HttpGet]
    public IActionResult GetStats()
    {
        int userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier).Value);

        var stats = _db.Statistics
            .FirstOrDefault(s => s.UserId == userId);

        if (stats == null)
            return NotFound();

        return Ok(stats);
    }
}
