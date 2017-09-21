using Microsoft.EntityFrameworkCore;

namespace TryNetMVC.Models
{
    public class ApplicationDbContext : DbContext
    {
        public DbSet<Character> Characters { get; set; }
        //public DbSet<Equipment> Equipments { get; set; }

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)
        { }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            //Need to set this so that when updating the character, ID field is ignored
            builder.Entity<Character>().Property(p => p.ID).UseSqlServerIdentityColumn();

            base.OnModelCreating(builder);
        }
    }
}
