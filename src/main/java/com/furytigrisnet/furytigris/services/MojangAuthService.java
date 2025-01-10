package com.furytigrisnet.furytigris.services;

import com.furytigrisnet.furytigris.records.MojangAuthRequest;
import com.furytigrisnet.furytigris.records.MojangAuthResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 10/01/2025
 */
@Service
public class MojangAuthService {

    private static final String AUTH_URL = "https://authserver.mojang.com/authenticate";

    public MojangAuthResponse authenticate(String email, String password) {
        MojangAuthRequest requestPayload = new MojangAuthRequest(email, password);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MojangAuthRequest> requestEntity = new HttpEntity<>(requestPayload, headers);

        try {
            ResponseEntity<MojangAuthResponse> response = restTemplate.postForEntity(
                    AUTH_URL,
                    requestEntity,
                    MojangAuthResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Autenticação bem-sucedida!");
                return response.getBody();
            } else {
                System.err.println("Erro ao autenticar. Status: " + response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
