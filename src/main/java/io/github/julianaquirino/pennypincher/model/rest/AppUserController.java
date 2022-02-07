package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.exception.RegisteredUserException;
import io.github.julianaquirino.pennypincher.model.entity.AppUser;
import io.github.julianaquirino.pennypincher.model.rest.dto.AppUserDTO;
import io.github.julianaquirino.pennypincher.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/app-users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid AppUser appUser){
        try {
            service.save(appUser);
        } catch (RegisteredUserException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<AppUser> getAll(){
        return service.findAllOrderByUsernameAsc();
    }

    @GetMapping("{id}")
    public AppUserDTO findById(@PathVariable Integer id){
        AppUser appUser = service
                .findById(id);

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(appUser.getId());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setName(appUser.getName());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setAdmin(appUser.isAdmin());
        appUserDTO.setPhone(appUser.getPhone());
        appUserDTO.setPassword(appUser.getPassword());

        System.out.println("Usuário: ");
        System.out.println(appUserDTO);
        return appUserDTO;
    }

    @GetMapping("{username}")
    @RequestMapping("/userByUsername")
    public AppUserDTO findByUsername(@RequestParam(value = "username", required = true) String username){
        AppUser appUser = service
                .findByUsername(username);

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(appUser.getId());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setName(appUser.getName());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setAdmin(appUser.isAdmin());
        appUserDTO.setPhone(appUser.getPhone());
        appUserDTO.setPassword(appUser.getPassword());

        System.out.println("Usuário: ");
        System.out.println(appUserDTO);
        return appUserDTO;
    }

    @GetMapping("{username}")
    @RequestMapping("/userIsAdmin")
    public boolean isUserAdmin(@RequestParam(value = "username", required = true) String username){
        AppUser appUser = service
                .findByUsername(username);
        return appUser.isAdmin();
    }

    @DeleteMapping("{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable String username) {
        this.service.delete(username);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@RequestBody @Valid AppUserDTO updatedAppUserDTO) {
        System.out.println(updatedAppUserDTO);
        this.service.update(updatedAppUserDTO);
    }

}
