package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.entity.AppUser;
import io.github.julianaquirino.pennypincher.model.repository.CategoryRepository;
import io.github.julianaquirino.pennypincher.model.repository.AppUserRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.CategoryDTO;
import io.github.julianaquirino.pennypincher.model.util.CategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository repository;
    private final AppUserRepository appUserRepository;

    @Autowired
    private CategoryController(CategoryRepository repository, AppUserRepository appUserRepository){
        this.repository = repository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping
    public List<Category> getAll(){
        return repository.findAllByOrderByNameAsc();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category save(@RequestBody @Valid CategoryDTO categoryDTO) {
        String usernameAppUser = categoryDTO.getUsernameAppUser();

        AppUser appUser =
                this.appUserRepository.findByUsername(usernameAppUser)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente"));

        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setCategoryType(categoryDTO.getCategoryType().equals("C") ? CategoryType.C : CategoryType.D);
        category.setAppUser(appUser);

        return repository.save(category);
    }

    @GetMapping("{id}")
    public Category findById(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(category -> {
                    repository.delete(category);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id, @RequestBody CategoryDTO updatedCategoryDTO) {
        this.repository
                .findById(id)
                .map(category -> {
                    category.setName(updatedCategoryDTO.getName());
                    category.setCategoryType(updatedCategoryDTO.getCategoryType().equals("C") ? CategoryType.C : CategoryType.D);
                    this.repository.save(category);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    @GetMapping
    @RequestMapping("/categoriesByTypeUsername")
    public List<Category> getCategoriesTypeUsername(@RequestParam(value = "categoryType", required = true) String categoryType,
                                                    @RequestParam(value = "username", required = true) String username){
        return this.repository.findByTypeUsername("%" + categoryType + "%", username);
    }

    @GetMapping("{username}")
    @RequestMapping("/categoriesByUsername")
    public List<Category> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        return repository.findAllByUsernameOrderByNameAsc(username);
    }

}
