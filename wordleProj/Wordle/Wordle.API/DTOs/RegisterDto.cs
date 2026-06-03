using Microsoft.AspNetCore.Mvc;
using Wordle.Domain.Models;

namespace Wordle.API.DTOs
{
    public class RegisterDto
    {
        public string Email { get; set; }
        public string Password { get; set; }
    }
}
