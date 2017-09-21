using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TryNetMVC.Models
{
    public static class DbInitializer
    {
        public static void Initialize(ApplicationDbContext context)
        {
            context.Database.EnsureCreated();

            if (context.Characters.Any())
            {
                return;
            }

            var characters = new Character[]
                {
                    new Character{Name = "Wolf", Dexterity = 10, Intelligence= 2, IsActive = true, Level = 1, Strength = 10},
                    new Character{Name = "Magneto", Dexterity= 5, Intelligence= 10, IsActive = true, Level = 1, Strength = 5}
                };

            foreach (var character in characters)
            {
                context.Characters.Add(character);
            }

            context.SaveChanges();
        }
    }
}
