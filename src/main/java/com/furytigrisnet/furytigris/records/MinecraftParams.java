package com.furytigrisnet.furytigris.records;

/**
 * MinecraftParams: Parametriza os argumentos necessários para executar o cliente Minecraft.
 * @author Anderson Andrade Dev
 * @Data de Criação 10/01/2025
 */
public record MinecraftParams(
        String xms,
        String xmx,
        String libraryPath,
        String classpathLibraries,
        String classpathJar,
        String gameDir,
        String assetsDir,
        String width,
        String height,
        String username,
        String version,
        String assetsIndex,
        String uuid,
        String accessToken,
        String userType
) {
    /**
     * Gera o comando para executar o Minecraft.
     * @return Comando formatado como String.
     */
    public String buildMinecraftCommand() {
        String classpathSeparator = System.getProperty("os.name").toLowerCase().contains("win") ? ";" : ":";

        return String.format(
                "java -Xms%s -Xmx%s -Djava.library.path=\"%s\" -cp \"%s%s%s\" net.minecraft.client.main.Main " +
                        "--width %s --height %s --username %s --version %s --gameDir \"%s\" " +
                        "--assetsDir \"%s\" --assetsIndex %s --uuid %s --accessToken %s --userType %s",
                xms, xmx, libraryPath, classpathLibraries, classpathSeparator, classpathJar,
                width, height, username, version, gameDir, assetsDir, assetsIndex, uuid, accessToken, userType
        );
    }

    /**
     * Valida os parâmetros principais para evitar erros em tempo de execução.
     * @throws IllegalArgumentException Se algum parâmetro for inválido.
     */
    public void validateParams() {
        if (xms == null || xmx == null || !xms.matches("\\d+[MG]") || !xmx.matches("\\d+[MG]")) {
            throw new IllegalArgumentException("Valores de memória (xms, xmx) inválidos! Use '512M', '1G', etc.");
        }
        if (libraryPath == null || classpathLibraries == null || classpathJar == null) {
            throw new IllegalArgumentException("Caminhos (libraryPath, classpathLibraries, classpathJar) não podem ser nulos!");
        }
        if (gameDir == null || assetsDir == null || username == null || version == null) {
            throw new IllegalArgumentException("Parâmetros obrigatórios ausentes!");
        }
    }
}
