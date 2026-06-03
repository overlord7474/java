using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wordle.Domain.Models
{
    public class Game
    {
        public int Id { get; set; }

        public int UserId { get; set; }

        public string Word { get; set; }

        public bool IsWon { get; set; }

        public int Attempts { get; set; }

        public DateTime PlayedAt { get; set; }
    }
}
