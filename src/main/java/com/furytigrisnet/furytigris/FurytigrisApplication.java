package com.furytigrisnet.furytigris;


import com.furytigrisnet.furytigris.services.InstallerService;
import com.furytigrisnet.furytigris.services.MinecraftLauncherService;
import com.furytigrisnet.furytigris.view.ui.LauncherFrame;
import com.furytigrisnet.furytigris.view.ui.LoadingFrame;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
public class FurytigrisApplication implements CommandLineRunner {


    private final LoadingFrame loadingFrame;
    private final LauncherFrame launcherFrame;

    public FurytigrisApplication(LoadingFrame loadingFrame,LauncherFrame launcherFrame) {
        this.loadingFrame = loadingFrame;
        this.launcherFrame = launcherFrame;
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
          this.loadingFrame.setVisible(true);

          this.launcherFrame.setVisible(true);

        this.loadingFrame.dispose();
    }

}