package io.github.julianaquirino.pennypincher;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class MinhaConfiguracao {

    /*@Bean(name="applicationName")
    public String applicationName(){
        return "Penny Pincher";
    }*/

    @Bean
    public CommandLineRunner executar(){
        return args -> {
            System.out.println("Rodando a aplicação em ambiente desenvolvimento");
        };
    }


}
