package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.entity.Goal;
import io.github.julianaquirino.pennypincher.model.entity.AppUser;
import io.github.julianaquirino.pennypincher.model.repository.GoalRepository;
import io.github.julianaquirino.pennypincher.model.repository.AppUserRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.GoalDTO;
import io.github.julianaquirino.pennypincher.model.util.BigDecimalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalRepository repository;
    private final AppUserRepository appUserRepository;
    private final BigDecimalConverter bigDecimalConverter;

    @Autowired
    private GoalController(GoalRepository repository, AppUserRepository appUserRepository,
                           BigDecimalConverter bigDecimalConverter){
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.bigDecimalConverter = bigDecimalConverter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Goal save(@RequestBody @Valid GoalDTO goalDTO) {
        String usernameAppUser = goalDTO.getUsernameAppUser();

        AppUser appUser =
                this.appUserRepository.findByUsername(usernameAppUser)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente"));

        Goal goal = new Goal();
        goal.setName(goalDTO.getName());
        goal.setValue(bigDecimalConverter.converter(goalDTO.getValue()));
        goal.setAppUser(appUser);

        return repository.save(goal);
    }

    @GetMapping("{id}")
    public Goal findById(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(goal -> {
                    repository.delete(goal);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id, @RequestBody GoalDTO updatedGoalDTO) {
        this.repository
                .findById(id)
                .map(goal -> {
                    goal.setName(updatedGoalDTO.getName());
                    goal.setValue(bigDecimalConverter.converter(updatedGoalDTO.getValue()));
                    this.repository.save(goal);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meta não encontrada"));
    }

    @GetMapping("{username}")
    @RequestMapping("/goalsByUsername")
    public List<Goal> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        return repository.findAllByUsernameOrderByNameAsc(username);
    }

}

