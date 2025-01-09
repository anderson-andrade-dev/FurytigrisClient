package furytigris;

import furytigris.ui.LauncherFrame;
import furytigris.ui.LoadingFrame;
import furytigris.util.OSHelper;
import furytigris.util.UnzipUtility;
import org.apache.commons.io.FileUtils;
import org.lwjgl.Sys;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        new LoadingFrame();
    }

    public static void launch() {

        File minecraftDirectory = new File(OSHelper.getOS().getMc());
        File minecraftAssets = new File(minecraftDirectory.toString() + "assets");

        File natives = new File(System.getProperty("user.dir") + File.separator + "natives.zip");
        File libraries = new File(System.getProperty("user.dir") + File.separator + "libraries.zip");
        File jar = new File(System.getProperty("user.dir") + File.separator + "client.jar");

        try {
            FileUtils.copyURLToFile(new URL("https://download1584.mediafire.com/ohxueeoflwagXrAzSP5dUXqcdDZtVQky4NnsO6Reoc6Cw5VYqnlSGg5L1yahPMmW7RcBKhiWjDUtlnR069Y5q6Rz5wlscrqm4DKrZHV_u0A5vLdJWGUxNKBm97z0WnRNvynBhZXazf4YnzABJ_Q0_WX9WzCdFfsrKAJ3aN3TsBoFygI/mzr3tnutaj1izv4/natives.zip"), natives);
            FileUtils.copyURLToFile(new URL("https://download1500.mediafire.com/dw65rqhv7pkgv0HdSXdn6MtI60ScB-zyZmygYdyroa39MQsYozv_TpcTxiaxJjZZGVYfzQ9K0HwHIwFESyefURmdHS5iKMvvmEt0QondTB5jhq2oxlkR1y2srdf_QQALWTKlwlAzGQtVVvdDKoJJI3kbxDLoQixwiJ7vcuPsP0gihXA/h5z12385dpsjl2p/libraries.zip"), libraries);
            FileUtils.copyURLToFile(new URL("https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar"), jar);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UnzipUtility unzipper = new UnzipUtility();

        try {
            unzipper.unzip(natives.toString(), System.getProperty("user.dir") + File.separator + "natives");
            natives.delete();

            unzipper.unzip(libraries.toString(), System.getProperty("user.dir") + File.separator + "libraries");
            natives.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Process process = Runtime.getRuntime().exec("java -"
                    + "Xms1024M "
                    + "-Xmx4096M "
                    + "-Djava.library.path=\"" + System.getProperty("user.dir") + File.separator + "natives" + "\" "
                    + "-cp \"" + System.getProperty("user.dir") + File.separator + "libraries" + File.separator + "*" + ";" + jar.toString() + "\" "
                    + "net.minecraft.client.main.Main "
                    + "--width 854 "
                    + "--height 480 "
                    + "--username _ZinhoZin "
                    + "--version 1.8.8 "
                    + "--gameDir " + minecraftDirectory.toString() + " "
                    + "--assetsDir " + minecraftAssets.toString() + " "
                    + "assetsIndex 1.8.8 "
                    + "uuid N/A "
                    + "--accessToken aeef7bc935f9420eb6314dea7ad7e1e5 "
                    + "--userType mojang");
            System.out.println("[FuryTigris Client 1.8.8] Inicializando Client");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s = null;
            while((s = stdInput.readLine()) != null) {
                 System.out.println(s);
            }
            while((s = stdError.readLine()) != null) {
                 System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}