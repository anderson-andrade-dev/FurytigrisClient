package com.furytigrisnet.furytigris.records;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 10/01/2025
 */
public record MojangAuthResponse(
        String accessToken,
        Profile selectedProfile
) {}
