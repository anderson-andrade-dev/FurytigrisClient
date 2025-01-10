package com.furytigrisnet.furytigris.services;

import com.furytigrisnet.furytigris.util.UnzipUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 09/01/2025
 */

@Service
public class InstallerService {

    private static final Logger logger = LoggerFactory.getLogger(InstallerService.class);
    private static final String NATIVE_URL = "https://raw.githubusercontent.com/BielZcode/Launcher-FuryTigris/main/natives.zip";
    private static final String LIBRARY_URL = "https://raw.githubusercontent.com/BielZcode/Launcher-FuryTigris/main/libraries.zip";
    private static final String JAR_URL = "https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar";

    private final String basePath = System.getProperty("user.dir");

    public void install() throws Exception {
        ensureDirectories();
        downloadFiles();
        unzipFiles();
    }

    /**
     * Garante que os diretórios necessários existam.
     */
    private void ensureDirectories() {
        createDirectoryIfNotExists(basePath + File.separator + "downloads");
        createDirectoryIfNotExists(basePath + File.separator + "downloads" + File.separator + "natives");
        createDirectoryIfNotExists(basePath + File.separator + "downloads" + File.separator + "libraries");
    }

    /**
     * Cria um diretório caso não exista.
     *
     * @param path Caminho do diretório a ser criado.
     */
    private void createDirectoryIfNotExists(String path) {
        Path dirPath = Paths.get(path);
        try {
            Files.createDirectories(dirPath);
            logger.info("Diretório garantido: {}", dirPath.toAbsolutePath());
        } catch (IOException e) {
            logger.error("Erro ao criar diretório: {}", e.getMessage(), e);
        }
    }

    private void downloadFiles() throws Exception {
        File natives = new File(basePath + File.separator + "downloads" + File.separator + "natives.zip");
        File libraries = new File(basePath + File.separator + "downloads" + File.separator + "libraries.zip");
        File jar = new File(basePath + File.separator + "downloads" + File.separator + "client.jar");

        downloadFileWithProgress(NATIVE_URL, natives);
        downloadFileWithProgress(LIBRARY_URL, libraries);
        downloadFileWithProgress(JAR_URL, jar);
    }

    private void downloadFileWithProgress(String fileUrl, File destination) throws Exception {
        logger.info("Iniciando download de: {}", fileUrl);

        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int fileSize = connection.getContentLength();

        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(destination)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalRead = 0;
            int lastProgress = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalRead += bytesRead;

                int progress = (int) ((totalRead * 100) / fileSize);
                if (progress != lastProgress) {
                    printProgressBar(progress);
                    lastProgress = progress;
                }
            }
            logger.info("\nDownload concluído: {}", destination.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Erro ao baixar o arquivo de {}: {}", fileUrl, e.getMessage());
            throw e;
        }
    }

    private void printProgressBar(int progress) {
        int barLength = 50;
        int filledLength = (int) (barLength * progress / 100.0);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < filledLength; i++) bar.append("=");
        for (int i = filledLength; i < barLength; i++) bar.append(" ");
        bar.append("]");

        System.out.print("\r" + bar + " " + progress + "%");
    }

    private void unzipFiles() throws Exception {
        File natives = new File(basePath + File.separator + "downloads" + File.separator + "natives.zip");
        File libraries = new File(basePath + File.separator + "downloads" + File.separator + "libraries.zip");

        UnzipUtility unzipper = new UnzipUtility();

        if (natives.exists()) {
            logger.info("Descompactando arquivo: {}", natives.getAbsolutePath());
            unzipper.unzip(natives.toString(), basePath + File.separator + "downloads" + File.separator + "natives");
            natives.delete();
        } else {
            logger.warn("Arquivo para descompactar não encontrado: {}", natives.getAbsolutePath());
        }

        if (libraries.exists()) {
            logger.info("Descompactando arquivo: {}", libraries.getAbsolutePath());
            unzipper.unzip(libraries.toString(), basePath + File.separator + "downloads"+ File.separator + "libraries");
            libraries.delete();
        } else {
            logger.warn("Arquivo para descompactar não encontrado: {}", libraries.getAbsolutePath());
        }
    }
}
