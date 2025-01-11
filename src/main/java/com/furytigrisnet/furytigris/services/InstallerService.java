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
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service responsável por gerenciar o processo de instalação, que inclui:
 * - Download de arquivos necessários (ZIPs e JARs)
 * - Garantia de existência de diretórios
 * - Descompactação de arquivos baixados
 *
 * @author Anderson Andrade Dev
 * @date 09/01/2025
 */

@Service
public class InstallerService {

    private static final Logger logger = LoggerFactory.getLogger(InstallerService.class);

    // URLs dos arquivos para download
    private static final String NATIVE_URL = "https://raw.githubusercontent.com/BielZcode/Launcher-FuryTigris/main/natives.zip";
    private static final String LIBRARY_URL = "https://raw.githubusercontent.com/BielZcode/Launcher-FuryTigris/main/libraries.zip";
    private static final String JAR_URL = "https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar";

    // Diretório base: Pasta Downloads do usuário
    private final String basePath = System.getProperty("user.dir");
    private final String homePath = System.getProperty("user.home");
    /**
     * Método principal que executa o processo completo de instalação.
     * Inclui:
     * 1. Garantia da existência de diretórios.
     * 2. Download de arquivos.
     * 3. Descompactação de arquivos ZIP.
     */
    public void install() throws Exception {
        ensureDirectories();
        downloadFiles();
        unzipFiles();
    }

    /**
     * Garante que os diretórios necessários existam antes do download e descompactação.
     */
    private void ensureDirectories() {
        // Diretórios principais de downloads e suas subpastas
        createDirectoryIfNotExists(basePath + File.separator + "downloads");
        createDirectoryIfNotExists(basePath + File.separator + "downloads" + File.separator + "natives");
        createDirectoryIfNotExists(basePath + File.separator + "downloads" + File.separator + "libraries");

        // Garantindo que o diretório appData exista
        var appDataDir = homePath+ File.separator +"AppData";
        createDirectoryIfNotExists(appDataDir);

        // Garantindo que o diretório Roaming exista
        var roamingDir = appDataDir+File.separator + "Roaming";
        createDirectoryIfNotExists(roamingDir);

        // Garantindo que o diretório .minecraft exista
        var minecraftDir = homePath + File.separator + ".minecraft";
        createDirectoryIfNotExists(minecraftDir);

        // Garantindo que o diretório de assets também exista
        var assetsDir = minecraftDir + File.separator + "assets";
        createDirectoryIfNotExists(assetsDir);
    }

    /**
     * Cria um diretório caso ele não exista.
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

    /**
     * Gerencia o download dos arquivos necessários.
     * Verifica se os arquivos já existem para evitar downloads redundantes.
     */
    private void downloadFiles() throws Exception {
        File natives = new File(basePath + File.separator + "downloads" + File.separator + "natives.zip");
        File libraries = new File(basePath + File.separator + "downloads" + File.separator + "libraries.zip");
        File jar = new File(basePath + File.separator + "downloads" + File.separator + "client.jar");

        // Baixa arquivos apenas se não existirem
        if (!natives.exists()) {
            downloadFileWithProgress(NATIVE_URL, natives);
        } else {
            logger.info("Arquivo já existente: {}", natives.getAbsolutePath());
        }

        if (!libraries.exists()) {
            downloadFileWithProgress(LIBRARY_URL, libraries);
        } else {
            logger.info("Arquivo já existente: {}", libraries.getAbsolutePath());
        }

        if (!jar.exists()) {
            downloadFileWithProgress(JAR_URL, jar);
        } else {
            logger.info("Arquivo já existente: {}", jar.getAbsolutePath());
        }
    }

    /**
     * Realiza o download de um arquivo com barra de progresso no console.
     *
     * @param fileUrl     URL do arquivo a ser baixado.
     * @param destination Arquivo de destino.
     */
    private void downloadFileWithProgress(String fileUrl, File destination) throws Exception {
        logger.info("Iniciando download de: {}", fileUrl);
        logger.info("Salvando arquivo em: {}", destination.getAbsolutePath());

        URL url = new URI(fileUrl).toURL();
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

    /**
     * Imprime uma barra de progresso no console para acompanhar o download.
     *
     * @param progress Porcentagem concluída.
     */
    private void printProgressBar(int progress) {
        int barLength = 50;
        int filledLength = (int) (barLength * progress / 100.0);

        StringBuilder bar = new StringBuilder("[");
        bar.append("=".repeat(Math.max(0, filledLength)));
        bar.append(" ".repeat(Math.max(0, barLength - filledLength)));
        bar.append("]");

        System.out.print("\r" + bar + " " + progress + "%");
    }

    /**
     * Descompacta os arquivos baixados para os diretórios apropriados.
     */
    private void unzipFiles() throws Exception {
        File natives = new File(basePath + File.separator + "downloads" + File.separator + "natives.zip");
        File libraries = new File(basePath + File.separator + "downloads" + File.separator + "libraries.zip");

        UnzipUtility unzipper = new UnzipUtility();

        if (natives.exists()) {
            logger.info("Descompactando arquivo: {}", natives.getAbsolutePath());
            unzipper.unzip(natives.toString(), basePath + File.separator + "downloads" + File.separator + "natives");
            if (natives.delete()) {
                logger.info("Arquivo Deletado com Sucesso: {}", natives.getAbsolutePath());
            }

        } else {
            logger.warn("Arquivo para descompactar não encontrado: {}", natives.getAbsolutePath());
        }

        if (libraries.exists()) {
            logger.info("Descompactando arquivo: {}", libraries.getAbsolutePath());
            unzipper.unzip(libraries.toString(), basePath + File.separator + "downloads" + File.separator + "libraries");

            if (libraries.delete()) {
                logger.info("Arquivo Deletado com Sucesso: {}", libraries.getAbsolutePath());
            }
        } else {
            logger.warn("Arquivo para descompactar não encontrado: {}", libraries.getAbsolutePath());
        }
    }
}
