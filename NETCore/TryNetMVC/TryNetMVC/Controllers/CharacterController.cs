using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using TryNetMVC.Models;

namespace TryNetMVC.Controllers
{
    public class CharacterController : Controller
    {
        private readonly ApplicationDbContext _context;

        public CharacterController(ApplicationDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IActionResult Index()
        {
            ViewData["Title"] = "Characters";
            var model = _context.Characters.ToList();
            return View(model);
        }

        public IActionResult GetActive()
        {
            var model = _context.Characters.Where(character => character.IsActive).ToList();
            return View(model);
        }

        [HttpGet]
        //search friendly address
        [Route("Character/{name}/Details")]
        public IActionResult Details(string name)
        {
            ViewData["Title"] = name;
            var model = _context.Characters.FirstOrDefault(character => character.Name.Equals(name));
            return View(model);
        }

        [HttpGet]
        [Route("Character/{name}/Edit")]
        public IActionResult Edit(string name)
        {
            ViewData["Title"] = "Edit " + name;
            var model = _context.Characters.FirstOrDefault(e => e.Name == name);
            return View(model);
        }

        [HttpGet]
        public IActionResult Create()
        {
            return View();
        }


        [HttpPost]
        public IActionResult Create(Character character)
        {
            //We can add an error to our ModelState using
            //the AddModelError method
            if (_context.Characters.Any(e => e.Name == character.Name))
            {
                ModelState.AddModelError("Name", "Name is already in use.");
            }
            //If our Model isn’t valid, we should return our
            //view with the provided input.This will cause
            //asp - validation - summary to display our errors.
            if (!ModelState.IsValid)
            {
                return View(character);
            }
            _context.Characters.Add(character);
            _context.SaveChanges();
            return RedirectToAction("Index");
        }

        [HttpPost]
        public IActionResult Update(Character character)
        {
            _context.Entry(character).State = EntityState.Modified;
            _context.SaveChanges();
            return RedirectToAction("Index");
        }

        [HttpGet]
        [Route("Character/{name}/Delete")]
        public IActionResult Delete(string name)
        {
            ViewData["Title"] = "Delete " + name;
            var model = _context.Characters.FirstOrDefault(e => e.Name == name);
            return View(model);
        }

        //Need to find a way to use override methods Delete(Character) for HttpPost
        [HttpPost]
        public IActionResult Remove(Character character)
        {
            var original = _context.Characters.FirstOrDefault(e => e.ID == character.ID);
            if (original != null)
            {
                _context.Characters.Remove(original);
                _context.SaveChanges();
            }
            return RedirectToAction("Index");
        }
    }
}
