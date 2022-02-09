package io.github.julianaquirino.pennypincher.service;

import io.github.julianaquirino.pennypincher.exception.RegisteredUserException;
import io.github.julianaquirino.pennypincher.model.entity.AppUser;
import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.repository.AppUserRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.AppUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository repository;

    public void createUserAdmin(){

        Optional<AppUser> appUser = repository.findByUsername("admin");

        if (appUser == null || !appUser.isPresent()) {
            AppUser userAdmin = new AppUser();
            userAdmin.setUsername("admin");
            userAdmin.setName("Admin");
            userAdmin.setPassword("12345");
            userAdmin.setPhone("(31) 99245-4848");
            userAdmin.setEmail("juquisilva@gmail.com");
            userAdmin.setAdmin(true);
            repository.save(userAdmin);
        }
    }

    public AppUser save (AppUser appUser){

        if (appUser.getUsername().contains(" ")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de usuário não deve conter espaços em branco!");
        }
        boolean exists = repository.existsByUsername(appUser.getUsername());
        if (exists){
            throw new RegisteredUserException(appUser.getUsername());
        }
        return repository.save(appUser);
    }

    public void delete (String username){

        AppUser appUser = findByUsername(username);
        repository.delete(appUser);
    }

    public AppUser findByUsername(String username){
        return repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado"));
    }

    public AppUser findById(Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public List<AppUser> findAllOrderByUsernameAsc(){
        return repository
                .findAllOrderByUsernameAsc();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository
                            .findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles("USER")
                .build()
                ;
    }

    public void update(AppUserDTO updatedAppUserDTO) {
        AppUser appUser = findByUsername(updatedAppUserDTO.getUsername());

        appUser.setName(updatedAppUserDTO.getName());
        appUser.setEmail(updatedAppUserDTO.getEmail());
        appUser.setPhone(updatedAppUserDTO.getPhone());
        appUser.setPassword(updatedAppUserDTO.getPassword());
        appUser.setAdmin(updatedAppUserDTO.isAdmin());

        this.repository.save(appUser);
    }
}
