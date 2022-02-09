package io.github.julianaquirino.pennypincher;

import io.github.julianaquirino.pennypincher.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class MinhaConfiguracao {

    /*@Bean(name="applicationName")
    public String applicationName(){
        return "Penny Pincher";
    }*/
    @Autowired
    private AppUserService appUserService;

    @Bean
    public CommandLineRunner executar(){

        appUserService.createUserAdmin();

        return args -> {
            System.out.println("Rodando a aplicação em ambiente produção");
        };
    }


}
