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

    // URLs dos arquivos necessários para o Minecraft
    private static final String NATIVE_URL = "https://download1584.mediafire.com/ohxueeoflwagXrAzSP5dUXqcdDZtVQky4NnsO6Reoc6Cw5VYqnlSGg5L1yahPMmW7RcBKhiWjDUtlnR069Y5q6Rz5wlscrqm4DKrZHV_u0A5vLdJWGUxNKBm97z0WnRNvynBhZXazf4YnzABJ_Q0_WX9WzCdFfsrKAJ3aN3TsBoFygI/mzr3tnutaj1izv4/natives.zip";
    private static final String LIBRARY_URL = "https://download1500.mediafire.com/dw65rqhv7pkgv0HdSXdn6MtI60ScB-zyZmygYdyroa39MQsYozv_TpcTxiaxJjZZGVYfzQ9K0HwHIwFESyefURmdHS5iKMvvmEt0QondTB5jhq2oxlkR1y2srdf_QQALWTKlwlAzGQtVVvdDKoJJI3kbxDLoQixwiJ7vcuPsP0gihXA/h5z12385dpsjl2p/libraries.zip";
    private static final String JAR_URL = "https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar";

    // Caminho base para os arquivos baixados
    private final String basePath = System.getProperty("user.dir") + File.separator + "downloads";

    /**
     * Garante que os diretórios necessários existam.
     */
    private void ensureDirectories() {
        createDirectoryIfNotExists(basePath);
        createDirectoryIfNotExists(basePath + File.separator + "natives");
        createDirectoryIfNotExists(basePath + File.separator + "libraries");
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

    /**
     * Realiza o download dos arquivos necessários.
     */
    public void downloadFiles() {
        ensureDirectories();
        try {
            logger.info("Iniciando o download dos arquivos...");
            downloadFile(NATIVE_URL, basePath + File.separator + "natives.zip");
            downloadFile(LIBRARY_URL, basePath + File.separator + "libraries.zip");
            downloadFile(JAR_URL, basePath + File.separator + "client.jar");
            logger.info("Downloads concluídos.");
        } catch (Exception e) {
            logger.error("Erro ao baixar arquivos: {}", e.getMessage(), e);
        }
    }

    /**
     * Faz o download de um arquivo único a partir de uma URL fornecida para o destino especificado.
     *
     * @param url         A URL de onde baixar o arquivo.
     * @param destination O caminho do destino.
     * @throws IOException Se ocorrer um erro durante o download.
     */
    private void downloadFile(String url, String destination) throws IOException {
        File destinationFile = new File(destination);
        logger.info("Iniciando o download de: {}", url);

        FileUtils.copyURLToFile(new URL(url), destinationFile);

        if (!destinationFile.exists() || destinationFile.length() == 0) {
            throw new IOException("Download falhou: arquivo vazio ou inexistente em " + destination);
        }

        logger.info("Download concluído: {}", destinationFile.getAbsolutePath());
    }

    /**
     * Descompacta os arquivos baixados e exclui os arquivos zip.
     */
    public void unzipFiles() {
        try {
            unzipFile(basePath + File.separator + "natives.zip", basePath + File.separator + "natives");
            unzipFile(basePath + File.separator + "libraries.zip", basePath + File.separator + "libraries");
        } catch (Exception e) {
            logger.error("Erro ao descompactar arquivos: {}", e.getMessage(), e);
        }
    }

    /**
     * Descompacta um arquivo para um diretório específico.
     *
     * @param zipFile Caminho do arquivo zip.
     * @param folder  Caminho do diretório de destino.
     * @throws IOException Se ocorrer um erro ao descompactar.
     */
    private void unzipFile(String zipFile, String folder) throws IOException {
        UnzipUtility unzipper = new UnzipUtility();
        unzipper.unzip(zipFile, folder);
        Files.delete(Paths.get(zipFile));
        logger.info("Arquivo {} descompactado para {}", zipFile, folder);
    }

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
