using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TryNetMVC.Models
{
    public class Character
    {
        //It is set to false so that EditorForModel does not show this in Update View 
        [ScaffoldColumn(false)]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }
        [Required(ErrorMessage = "The Name field is required.")]
        [MinLength(3)]
        public string Name { get; set; }
        [Required(ErrorMessage = "The IsActive field is required.")]
        [Display(Name = "Is Active")]
        public bool IsActive { get; set; }
        [Required(ErrorMessage = "The Level field is required.")]
        [Range(1, 20)]
        public int Level { get; set; }
        [Required(ErrorMessage = "The Strength field is required.")]
        [Range(1, 18)]
        public int Strength { get; set; }
        [Required(ErrorMessage = "The Dexterity field is required.")]
        public int Dexterity { get; set; }
        [Required(ErrorMessage = "The Intelligence field is required.")]
        public int Intelligence { get; set; }

        //public List<Equipment> Equipment { get; set; }
    }
}
