package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.entity.Limit;
import io.github.julianaquirino.pennypincher.model.repository.CategoryRepository;
import io.github.julianaquirino.pennypincher.model.repository.LimitRepository;
import io.github.julianaquirino.pennypincher.model.repository.AppUserRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.LimitDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.LimitListDTO;
import io.github.julianaquirino.pennypincher.model.util.BigDecimalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/limits")
public class LimitController {

    private final LimitRepository repository;
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;
    private final BigDecimalConverter bigDecimalConverter;

    @Autowired
    private LimitController(LimitRepository repository, CategoryRepository categoryRepository,
                            BigDecimalConverter bigDecimalConverter, AppUserRepository appUserRepository){
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.categoryRepository = categoryRepository;
        this.bigDecimalConverter = bigDecimalConverter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Limit save(@RequestBody @Valid LimitDTO limitDTO) {

        Integer idCategory = limitDTO.getCategoryId();

        Category category =
                this.categoryRepository.findById(idCategory)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inexistente"));


        Limit limit = new Limit();
        limit.setCategory(category);
        limit.setMaxValue(bigDecimalConverter.converter(limitDTO.getMaxValue()));
        limit.setMonth(limitDTO.getMonth());
        limit.setYear(limitDTO.getYear());

        return repository.save(limit);
    }

    @GetMapping("{id}")
    public Limit findById(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Limite não encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(limit -> {
                    repository.delete(limit);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Limite não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id, @RequestBody LimitDTO updatedLimitDTO) {
        this.repository
                .findById(id)
                .map(limit -> {
                    Integer idCategory = updatedLimitDTO.getCategoryId();

                    Category category =
                            this.categoryRepository.findById(idCategory)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inexistente"));


                    limit.setCategory(category);
                    limit.setMaxValue(bigDecimalConverter.converter(updatedLimitDTO.getMaxValue()));
                    limit.setMonth(updatedLimitDTO.getMonth());
                    limit.setYear(updatedLimitDTO.getYear());
                    this.repository.save(limit);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Limite não encontrado"));
    }

    @GetMapping("{username}")
    @RequestMapping("/limitsByUsername")
    public List<LimitListDTO> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        List<Limit> limits = repository.findAllByUsernameOrderByYearMonthAsc(username);

        List<LimitListDTO> limitList = new ArrayList<>();
        for (Limit limit : limits) {

            LimitListDTO limitListItem = new LimitListDTO();
            limitListItem.setId(limit.getId());
            limitListItem.setCategoryName(limit.getCategory().getName());
            limitListItem.setMonth(limit.getMonthDescription());
            limitListItem.setYear(limit.getYear());
            String value = limit.getMaxValue().toString();
            value = value.replace(".", ",");
            limitListItem.setMaxValue(value);
            limitList.add(limitListItem);
        }

        return limitList;
    }

}

