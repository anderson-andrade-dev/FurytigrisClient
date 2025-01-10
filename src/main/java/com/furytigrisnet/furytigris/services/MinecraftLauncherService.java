package com.furytigrisnet.furytigris.services;

import com.furytigrisnet.furytigris.util.OSHelper;
import com.furytigrisnet.furytigris.util.UnzipUtility;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Serviço para gerenciar as operações do launcher do Minecraft.
 * Combina funcionalidades de download de arquivos, extração e execução.
 *
 * @author Anderson Andrade Dev
 * @Data de Criação 09/01/2025
 */
@Service
public class MinecraftLauncherService {

    private static final Logger logger = LoggerFactory.getLogger(MinecraftLauncherService.class);

    // Caminho base para os arquivos baixados
    private final String basePath = System.getProperty("user.dir") + File.separator + "downloads";

    /**
     * Inicia o Minecraft com os parâmetros especificados.
     */
    public void launchMinecraft() {
        File jar = new File(basePath + File.separator + "client.jar");
        String command = buildMinecraftCommand(jar);

        try {
            logger.info("Executando comando: {}", command);
            Process process = Runtime.getRuntime().exec(command);
            logProcessOutput(process);
        } catch (IOException e) {
            logger.error("Erro ao iniciar o Minecraft: {}", e.getMessage(), e);
        }
    }

    /**
     * Constrói o comando para iniciar o Minecraft.
     *
     * @param jar O arquivo JAR do Minecraft.
     * @return O comando para execução.
     */
    private String buildMinecraftCommand(File jar) {
        return String.format("java -Xms1024M -Xmx4096M -Djava.library.path=\"%s/*\" -cp \"%s/*:%s\" net.minecraft.client.main.Main --width 854 --height 480 --username _ZinhoZin --version 1.8.8 --gameDir \"%s\" --assetsDir \"%s\" --assetsIndex 1.8.8 --uuid N/A --accessToken aeef7bc935f9420eb6314dea7ad7e1e5 --userType mojang",
                basePath + File.separator + "natives",
                basePath + File.separator + "libraries",
                jar.getAbsolutePath(),
                OSHelper.getOS().getMc(),
                OSHelper.getOS().getMc() + File.separator + "assets"
        );
    }

    /**
     * Registra a saída e os erros de um processo fornecido.
     *
     * @param process O processo para registrar.
     * @throws IOException Se ocorrer um erro durante a leitura.
     */
    private void logProcessOutput(Process process) throws IOException {
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = stdInput.readLine()) != null) {
                logger.info(line);
            }
            while ((line = stdError.readLine()) != null) {
                logger.error(line);
            }
        }
    }
}
