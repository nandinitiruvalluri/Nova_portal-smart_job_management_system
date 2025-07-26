package Springboot.example.SmartJobPortal.utils;

import java.awt.Desktop;
import java.net.URI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BrowserLauncher implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        } else {
            System.out.println("Desktop is not supported. Please open http://localhost:8080 manually.");
        }
    }
}
