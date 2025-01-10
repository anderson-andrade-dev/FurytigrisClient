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
    private static final String JAVA_URL = "https://www.openlogic.com/openjdk-downloads?field_java_parent_version_target_id=416&field_operating_system_target_id=All&field_architecture_target_id=391&field_java_package_target_id=401";
    private static final String NATIVE_URL = "https://download1584.mediafire.com/ohxueeoflwagXrAzSP5dUXqcdDZtVQky4NnsO6Reoc6Cw5VYqnlSGg5L1yahPMmW7RcBKhiWjDUtlnR069Y5q6Rz5wlscrqm4DKrZHV_u0A5vLdJWGUxNKBm97z0WnRNvynBhZXazf4YnzABJ_Q0_WX9WzCdFfsrKAJ3aN3TsBoFygI/mzr3tnutaj1izv4/natives.zip";
    private static final String LIBRARY_URL = "https://download1500.mediafire.com/dw65rqhv7pkgv0HdSXdn6MtI60ScB-zyZmygYdyroa39MQsYozv_TpcTxiaxJjZZGVYfzQ9K0HwHIwFESyefURmdHS5iKMvvmEt0QondTB5jhq2oxlkR1y2srdf_QQALWTKlwlAzGQtVVvdDKoJJI3kbxDLoQixwiJ7vcuPsP0gihXA/h5z12385dpsjl2p/libraries.zip";
    private static final String JAR_URL = "https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar";

    private final String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator +
            "main" + File.separator + "resources" + File.separator + "static" + File.separator + "download";

    /**
     * Garante que os diretórios necessários existam.
     */
    private void ensureDirectories() {
        createDirectoryIfNotExists(basePath);
        createDirectoryIfNotExists(basePath + File.separator + "java");
    }

    /**
     * Cria um diretório caso não exista.
     * @param path Caminho do diretório a ser criado.
     */
    private void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists() && directory.mkdirs()) {
            logger.info("Diretório criado: {}", directory.getAbsolutePath());
        } else {
            logger.error("Falha ao criar o diretório ou diretório já existe: {}", directory.getAbsolutePath());
        }
    }

    /**
     * Realiza o download dos arquivos necessários.
     */
    public void downloadFiles() {
        ensureDirectories();
        try {
            downloadFile(NATIVE_URL, new File(basePath + File.separator + "natives.zip"));
            downloadFile(LIBRARY_URL, new File(basePath + File.separator + "libraries.zip"));
            downloadFile(JAR_URL, new File(basePath + File.separator + "client.jar"));
        } catch (Exception e) {
            logger.error("Erro ao baixar arquivos: {}", e.getMessage());
        }
    }

    /**
     * Faz o download de um arquivo único a partir de uma URL fornecida para o destino especificado.
     * Agora com verificação do download.
     *
     * @param url         A URL de onde baixar o arquivo.
     * @param destination O arquivo de destino.
     * @throws Exception se ocorrer um erro durante o download.
     */
    private void downloadFile(String url, File destination) throws Exception {
        logger.info("Baixando arquivo de: {}", url);
        FileUtils.copyURLToFile(new URL(url), destination);

        // Verificar se o arquivo foi baixado com sucesso
        while (!destination.exists() || destination.length() == 0) {
            logger.warn("Aguardando o download do arquivo: {}", destination.getName());
            Thread.sleep(1000);
        }

        logger.info("Arquivo baixado para: {}", destination.getAbsolutePath());
    }

    /**
     * Descompacta os arquivos baixados e exclui os arquivos zip.
     */
    public void unzipFiles() {
        UnzipUtility unzipper = new UnzipUtility();

        try {
            unzipFile(basePath + File.separator + "natives.zip", "natives");
            unzipFile(basePath + File.separator + "libraries.zip", "libraries");
        } catch (Exception e) {
            logger.error("Erro ao descompactar arquivos: {}", e.getMessage());
        }
    }

    /**
     * Descompacta um arquivo para um diretório específico.
     * @param zipFile Caminho do arquivo zip.
     * @param folder Nome da pasta de destino.
     * @throws Exception se ocorrer um erro ao descompactar.
     */
    private void unzipFile(String zipFile, String folder) throws Exception {
        UnzipUtility unzipper = new UnzipUtility();
        unzipper.unzip(zipFile, basePath + File.separator + folder);
        new File(zipFile).delete();
    }

    /**
     * Inicia o Minecraft com os parâmetros especificados.
     */
    public void launchMinecraft() {

        File javaExe = new File(basePath + File.separator + "java" + File.separator + "bin" + File.separator + "java.exe");
        File minecraftDirectory = new File(OSHelper.getOS().getMc());
        File minecraftAssets = new File(minecraftDirectory + File.separator + "assets");
        File jar = new File(basePath + File.separator + "client.jar");

        try {
            String command = buildMinecraftCommand(javaExe, minecraftDirectory, minecraftAssets, jar);
            logger.info("Executando comando: {}", command);
            Process process = Runtime.getRuntime().exec(command);

            logProcessOutput(process);
        } catch (IOException e) {
            logger.error("Erro ao iniciar o Minecraft: {}", e.getMessage());
        }
    }

    /**
     * Constrói o comando para iniciar o Minecraft.
     *
     * @param javaExe O caminho do executável do Java.
     * @param minecraftDirectory O diretório do Minecraft.
     * @param minecraftAssets O diretório de assets do Minecraft.
     * @param jar O arquivo JAR do Minecraft.
     * @return O comando para execução.
     */
    private String buildMinecraftCommand(File javaExe, File minecraftDirectory, File minecraftAssets, File jar) {
        String javaCommand = "java";
        String osName = System.getProperty("os.name").toLowerCase();

        // Verificar o sistema operacional e ajustar o comando
        if (osName.contains("win")) {
            javaCommand = "\"" + basePath + File.separator + "java-windows" + File.separator + "bin" + File.separator + "java.exe\""; // Caminho para Windows
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            javaCommand = "\"" + basePath + File.separator + "java-linux" + File.separator + "bin" + File.separator + "java\""; // Caminho para Linux
        }

        return String.format("\"%s\" -Xms1024M -Xmx4096M -Djava.library.path=\"%s\" -cp \"%s\\*;%s\" net.minecraft.client.main.Main --width 854 --height 480 --username _ZinhoZin --version 1.8.8 --gameDir %s --assetsDir %s --assetsIndex 1.8.8 --uuid N/A --accessToken aeef7bc935f9420eb6314dea7ad7e1e5 --userType mojang",
                javaCommand,
                basePath + File.separator + "natives",
                basePath + File.separator + "libraries",
                jar.getAbsolutePath(),
                minecraftDirectory.getAbsolutePath(),
                minecraftAssets.getAbsolutePath()
        );
    }

    /**
     * Registra a saída e os erros de um processo fornecido.
     *
     * @param process O processo para registrar.
     * @throws IOException se ocorrer um erro durante a leitura.
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
