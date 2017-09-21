using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Linq;
using ToDoApi.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace ToDoApi.Controllers
{
    [Produces("application/json")]
    [Route("api/[controller]")]
    public class TodoController : Controller
    {
        private readonly TodoContext _context;

        public TodoController(TodoContext context)
        {
            _context = context;

            if (_context.TodoItems.Count() == 0)
            {
                _context.TodoItems.Add(new TodoItem { Name = "Item1" });
                _context.SaveChanges();
            }
        }

        /// <summary>
        /// Returns all Todo items
        /// </summary>
        /// <returns></returns>
        //GET /api/todo
        [HttpGet]
        public IEnumerable<TodoItem> GetAll()
        {
            return _context.TodoItems.ToList();
        }

        /// <summary>
        /// Return a specific Todo item
        /// </summary>
        /// <param name="id">The id of the Todo item to return</param>
        /// <returns></returns>
        //GET /api/todo/{id}
        [HttpGet("{id}", Name = "GetTodo")]
        public IActionResult GetById(long id)
        {
            var item = _context.TodoItems.FirstOrDefault(t => t.Id == id);
            if (item == null)
            {
                return NotFound();
            }
            return new ObjectResult(item);
        }

        /// <summary>
        /// Creats a Todo item
        /// </summary>
        /// <remarks
        /// Sample request:
        /// 
        ///     POST /Todo
        ///     {
        ///         "id": 1,
        ///         "name": "Item1",
        ///         "isComplete": true
        ///     }
        /// </remarks>
        /// <param name="item">The item to create</param>
        /// <returns>A newly created tool item</returns>
        /// <response code="201">Returns the newly-created item</response>
        /// <response code="400">If the item is null</response>  
        [HttpPost]
        [ProducesResponseType(typeof(TodoItem), 201)]
        [ProducesResponseType(typeof(TodoItem), 400)]
        public IActionResult Create([FromBody] TodoItem item)
        {
            //Post /api/todo/ The body needs to have the item in JSON
            //The [FromBody] attribute tells MVC to get the value of the to-do item from the body of the HTTP request.
            if (item == null)
            {
                return BadRequest();
            }

            _context.TodoItems.Add(item);
            _context.SaveChanges();

            /*The CreatedAtRoute method returns a 201 response, which is the standard response for an HTTP POST method that 
             * creates a new resource on the server. CreatedAtRoute also adds a Location header to the response. The Location 
             * header specifies the URI of the newly created to-do item*/
            return CreatedAtRoute("GetTodo", new { id = item.Id }, item);
        }

        /// <summary>
        /// Updates a specific Todo item
        /// </summary>
        /// <param name="id">The id of the item to update</param>
        /// <param name="item">The modified item</param>
        /// <returns></returns>
        //Put /api/todo/{id} The body needs to have the item in JSON
        [HttpPut("{id}")]
        public IActionResult Update(long id, [FromBody] TodoItem item)
        {
            if (item == null || item.Id != id)
            {
                return BadRequest();
            }

            var todo = _context.TodoItems.FirstOrDefault(t => t.Id == id);
            if (todo == null)
            {
                return NotFound();
            }

            todo.IsComplete = item.IsComplete;
            todo.Name = item.Name;

            _context.TodoItems.Update(todo);
            _context.SaveChanges();
            /*The response is 204 (No Content). According to the HTTP spec, a PUT request requires the client to send the entire 
             * updated entity, not just the deltas. To support partial updates, use HTTP PATCH.*/
            return new NoContentResult();
        }

        ////Put /api/todo/{id} The body needs to have the item in JSON
        //[HttpPatch("{id}")]
        //public IActionResult Update(long id, [FromBody] JsonPatchDocument<TodoItem> patch)
        //{
        //    var todo = _context.TodoItems.FirstOrDefault(t => t.Id == id);
        //    if (todo == null)
        //    {
        //        return NotFound();
        //    }

        //    patch.ApplyTo(todo, ModelState);

        //    if (!ModelState.IsValid)
        //    {
        //        return BadRequest();
        //    }

        //    _context.TodoItems.Update(todo);
        //    _context.SaveChanges();
        //    /*The response is 204 (No Content). According to the HTTP spec, a PUT request requires the client to send the entire 
        //     * updated entity, not just the deltas. To support partial updates, use HTTP PATCH.*/
        //    return new NoContentResult();
        //}

        /// <summary>
        /// Deletes a specific Todo item
        /// </summary>
        /// <param name="id">The id of the item to delete</param>
        /// <returns></returns>
        //Delete /api/todo/{id} 
        [HttpDelete("{id}")]
        public IActionResult Delete(long id)
        {
            var todo = _context.TodoItems.FirstOrDefault(t => t.Id == id);
            if (todo == null)
            {
                return NotFound();
            }

            _context.TodoItems.Remove(todo);
            _context.SaveChanges();
            return new NoContentResult();
        }
    }
}
