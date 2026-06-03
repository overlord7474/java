using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wordle.Domain.Models
{
    public class Statistic
    {
        public int Id { get; set; }

        public int UserId { get; set; }

        public int GamesPlayed { get; set; }

        public int Wins { get; set; }

        public int CurrentStreak { get; set; }

        public int MaxStreak { get; set; }

        public int TotalPoints { get; set; }
    }
}
