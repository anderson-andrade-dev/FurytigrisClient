package com.furytigrisnet.furytigris.services;

import com.furytigrisnet.furytigris.util.UnzipUtility;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 09/01/2025
 */

@Service
public class InstallerService {

    private static final String NATIVE_URL = "https://download1584.mediafire.com/ohxueeoflwagXrAzSP5dUXqcdDZtVQky4NnsO6Reoc6Cw5VYqnlSGg5L1yahPMmW7RcBKhiWjDUtlnR069Y5q6Rz5wlscrqm4DKrZHV_u0A5vLdJWGUxNKBm97z0WnRNvynBhZXazf4YnzABJ_Q0_WX9WzCdFfsrKAJ3aN3TsBoFygI/mzr3tnutaj1izv4/natives.zip";
    private static final String LIBRARY_URL = "https://download1500.mediafire.com/dw65rqhv7pkgv0HdSXdn6MtI60ScB-zyZmygYdyroa39MQsYozv_TpcTxiaxJjZZGVYfzQ9K0HwHIwFESyefURmdHS5iKMvvmEt0QondTB5jhq2oxlkR1y2srdf_QQALWTKlwlAzGQtVVvdDKoJJI3kbxDLoQixwiJ7vcuPsP0gihXA/h5z12385dpsjl2p/libraries.zip";
    private static final String JAR_URL = "https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar";

    private final String basePath = System.getProperty("user.dir");



    public void downloadFiles() throws Exception {
        File natives = new File(basePath + File.separator + "natives.zip");
        File libraries = new File(basePath + File.separator + "libraries.zip");
        File jar = new File(basePath + File.separator + "client.jar");

        downloadFile(NATIVE_URL, natives);
        downloadFile(LIBRARY_URL, libraries);
        downloadFile(JAR_URL, jar);
    }

    private void downloadFile(String url, File destination) throws Exception {
        FileUtils.copyURLToFile(new URL(url), destination);
    }

    public void unzipFiles() throws Exception {
        File natives = new File(basePath + File.separator + "natives.zip");
        File libraries = new File(basePath + File.separator + "libraries.zip");

        UnzipUtility unzipper = new UnzipUtility();
        unzipper.unzip(natives.toString(), basePath + File.separator + "natives");
        natives.delete();

        unzipper.unzip(libraries.toString(), basePath + File.separator + "libraries");
        libraries.delete();
    }

    public boolean checkJavaInstallation() {
        File javaExe = new File(basePath + File.separator + "../data/java/jdk1.8.0_202/bin/java.exe");
        return javaExe.exists();
    }
}
