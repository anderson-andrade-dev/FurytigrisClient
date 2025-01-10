package com.furytigrisnet.furytigris.records;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 10/01/2025
 */
public record MojangAuthRequest(
        Agent agent,
        String username,
        String password,
        boolean requestUser
) {
    public MojangAuthRequest(String username, String password) {
        this(new Agent("Minecraft", 1), username, password, true);
    }
}
