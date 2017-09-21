using System.ComponentModel.DataAnnotations;

namespace ToDoApi.Models
{
    public class TodoItem
    {
        [Required]
        public long Id { get; set; }
        [Required]
        public string Name { get; set; }
        public bool IsComplete { get; set; }
    }
}
