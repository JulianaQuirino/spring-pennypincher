package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.entity.Project;
import io.github.julianaquirino.pennypincher.model.entity.AppUser;
import io.github.julianaquirino.pennypincher.model.repository.ProjectRepository;
import io.github.julianaquirino.pennypincher.model.repository.AppUserRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository repository;
    private final AppUserRepository appUserRepository;

    @Autowired
    private ProjectController(ProjectRepository repository, AppUserRepository appUserRepository){
        this.repository = repository;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project save(@RequestBody @Valid ProjectDTO projectDTO) {
        String usernameAppUser = projectDTO.getUsernameAppUser();

        AppUser appUser =
                this.appUserRepository.findByUsername(usernameAppUser)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente"));

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setAppUser(appUser);

        return repository.save(project);
    }

    @GetMapping("{id}")
    public Project findById(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(project -> {
                    repository.delete(project);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id, @RequestBody ProjectDTO updatedProjectDTO) {
        this.repository
                .findById(id)
                .map(project -> {
                    project.setName(updatedProjectDTO.getName());
                    project.setDescription(updatedProjectDTO.getDescription());
                    this.repository.save(project);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrada"));
    }

    @GetMapping("{username}")
    @RequestMapping("/projectsByUsername")
    public List<Project> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        return repository.findAllByUsernameOrderByNameAsc(username);
    }

}

