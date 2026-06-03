using Microsoft.EntityFrameworkCore;
using Wordle.Domain.Models;

namespace Wordle.Infrastructure.Data;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options)
        : base(options)
    {
    }

    public DbSet<User> Users => Set<User>();

    public DbSet<Game> Games => Set<Game>();

    public DbSet<Statistic> Statistics => Set<Statistic>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<User>()
            .HasOne(u => u.Statistic)
            .WithOne()
            .HasForeignKey<Statistic>(s => s.UserId);

        modelBuilder.Entity<Game>()
            .HasOne<User>()
            .WithMany()
            .HasForeignKey(g => g.UserId);
    }
}
