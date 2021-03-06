package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.entity.Account;
import io.github.julianaquirino.pennypincher.model.entity.AppUser;
import io.github.julianaquirino.pennypincher.model.repository.AccountRepository;
import io.github.julianaquirino.pennypincher.model.repository.AppUserRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.AccountDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.GoalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository repository;
    private final AppUserRepository appUserRepository;

    @Autowired
    private AccountController(AccountRepository repository, AppUserRepository appUserRepository){
        this.repository = repository;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account save(@RequestBody @Valid AccountDTO accountDTO) {
        String usernameAppUser = accountDTO.getUsernameAppUser();

        AppUser appUser =
                this.appUserRepository.findByUsername(usernameAppUser)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente"));

        Account account = new Account();
        account.setName(accountDTO.getName());
        account.setDescription(accountDTO.getDescription());
        account.setAppUser(appUser);

        return repository.save(account);
    }

    @GetMapping("{id}")
    public AccountDTO findById(@PathVariable Integer id){
        Account account = repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        AccountDTO accountDTO = null;
        if(account != null){
            accountDTO = new AccountDTO(account.getId(), account.getName(), account.getDescription(), account.getAppUser().getUsername());
        }
        return accountDTO;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(account -> {
                    repository.delete(account);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id, @RequestBody AccountDTO updatedAccountDTO) {
        this.repository
                .findById(id)
                .map(account -> {
                    account.setName(updatedAccountDTO.getName());
                    account.setDescription(updatedAccountDTO.getDescription());
                    this.repository.save(account);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    @GetMapping("{username}")
    @RequestMapping("/accountsByUsername")
    public List<AccountDTO> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        return repository.findAllByUsernameOrderByNameAsc(username);
    }

}

