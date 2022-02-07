package io.github.julianaquirino.pennypincher.model.rest;

        import io.github.julianaquirino.pennypincher.model.entity.Category;
        import io.github.julianaquirino.pennypincher.model.entity.Subcategory;
        import io.github.julianaquirino.pennypincher.model.repository.CategoryRepository;
        import io.github.julianaquirino.pennypincher.model.repository.SubcategoryRepository;
        import io.github.julianaquirino.pennypincher.model.rest.dto.SubcategoryDTO;
        import io.github.julianaquirino.pennypincher.model.rest.dto.SubcategoryListDTO;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;

        import javax.validation.Valid;
        import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    private final SubcategoryRepository repository;
    private final CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryController(SubcategoryRepository repository, CategoryRepository categoryRepository){
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubcategoryDTO save(@RequestBody @Valid SubcategoryDTO subcategoryDTO) {

        Integer idCategory = subcategoryDTO.getIdCategory();

        Category category =
                this.categoryRepository.findById(idCategory)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inexistente"));

        Subcategory subcategory = new Subcategory();
        subcategory.setName(subcategoryDTO.getName());
        subcategory.setCategory(category);

        subcategory = repository.save(subcategory);

        SubcategoryDTO subcategoryDTOSalva = new SubcategoryDTO();
        subcategoryDTOSalva.setId(subcategory.getId());
        subcategoryDTOSalva.setName(subcategory.getName());
        subcategoryDTOSalva.setIdCategory(subcategory.getCategory() != null ? subcategory.getCategory().getId() : null);
        return subcategoryDTOSalva;
    }

    /*@GetMapping("{id}")
    public Subcategory findById(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcategoria não encontrada"));
    }*/

    @GetMapping("{id}")
    public SubcategoryDTO findById(@PathVariable Integer id){
        return repository
                .findById(id)
                .map(subcategory -> {
                    SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
                    subcategoryDTO.setId(subcategory.getId());
                    subcategoryDTO.setName(subcategory.getName());
                    subcategoryDTO.setIdCategory(subcategory.getCategory() != null ? subcategory.getCategory().getId() : null);
                    return subcategoryDTO;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcategoria não encontrada"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(subcategory -> {
                    repository.delete(subcategory);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcategoria não encontrada"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id, @RequestBody @Valid SubcategoryDTO updatedSubcategoryDTO) {
        this.repository
                .findById(id)
                .map(subcategory -> {
                    subcategory.setName(updatedSubcategoryDTO.getName());
                    Category category =
                            this.categoryRepository.findById(updatedSubcategoryDTO.getIdCategory())
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subcategoria inexistente"));
                    subcategory.setCategory(category);
                    this.repository.save(subcategory);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcategoria não encontrada"));
    }

    @GetMapping("{username}")
    @RequestMapping("/subcategoriesByUsername")
    public List<SubcategoryListDTO> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        return repository.findAllByUsernameOrderByNameAsc(username);
    }

    @GetMapping("{idCategory}")
    @RequestMapping("/subcategoriesByIdCategory")
    public List<SubcategoryListDTO> getSubcategoriesByIdCategory(@RequestParam(value = "idCategory", required = true) Integer idCategory){
        return repository.getSubcategoriesByIdCategory(idCategory);
    }

}

