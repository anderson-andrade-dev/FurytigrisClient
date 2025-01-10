package com.furytigrisnet.furytigris.controller;

import com.furytigrisnet.furytigris.records.MojangAuthResponse;
import com.furytigrisnet.furytigris.services.MojangAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 10/01/2025
 */
@RestController
@RequestMapping("/auth")
public class MojangAuthController {

    private final MojangAuthService mojangAuthService;

    public MojangAuthController(MojangAuthService mojangAuthService) {
        this.mojangAuthService = mojangAuthService;
    }

    @PostMapping("/login")
    public MojangAuthResponse authenticate(@RequestParam String email, @RequestParam String password) {
        return mojangAuthService.authenticate(email, password);
    }
}
