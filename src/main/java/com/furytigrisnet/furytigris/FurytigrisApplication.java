package com.furytigrisnet.furytigris;


import com.furytigrisnet.furytigris.services.MinecraftLauncherService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
public class FurytigrisApplication implements CommandLineRunner {


    private final MinecraftLauncherService minecraftLauncherService;

    public FurytigrisApplication(MinecraftLauncherService minecraftLauncherService) {
        this.minecraftLauncherService = minecraftLauncherService;
    }

    public static void main(String[] args) {
        if (!GraphicsEnvironment.isHeadless()) {
            SpringApplication.run(FurytigrisApplication.class, args);
        } else {
            System.err.println("Ambiente gráfico indisponível. A aplicação requer suporte a GUI.");
        }
    }

    @Override
    public void run(String... args) throws Exception {
        minecraftLauncherService.launchMinecraft();
    }

}