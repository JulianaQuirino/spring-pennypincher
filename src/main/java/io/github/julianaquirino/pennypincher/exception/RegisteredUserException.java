package io.github.julianaquirino.pennypincher.exception;

public class RegisteredUserException extends  RuntimeException {

    public RegisteredUserException(String login ) {
        super ("Usuário já cadastrado para o login " + login);
    }
}
