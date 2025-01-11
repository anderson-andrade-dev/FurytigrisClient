package com.furytigrisnet.furytigris;


import com.furytigrisnet.furytigris.services.InstallerService;
import com.furytigrisnet.furytigris.services.MinecraftLauncherService;
import com.furytigrisnet.furytigris.view.ui.LauncherFrame;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
public class FurytigrisApplication implements CommandLineRunner {


    private final InstallerService installerService;
    private final MinecraftLauncherService minecraftLauncherService;
    private final LauncherFrame frame;

    public FurytigrisApplication(InstallerService installerService, MinecraftLauncherService minecraftLauncherService , LauncherFrame frame) {
        this.installerService = installerService;
        this.minecraftLauncherService = minecraftLauncherService;
        this.frame = frame;
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
        installerService.install();
        minecraftLauncherService.launchMinecraft();

    }

}