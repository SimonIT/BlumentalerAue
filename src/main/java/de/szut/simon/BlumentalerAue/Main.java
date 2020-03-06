package de.szut.simon.BlumentalerAue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class Main extends Application {

    private File windowSettingsDir = Paths.get(System.getProperty("user.home"), "BlumentalerAue").toFile();
    private File windowSettingsFile = new File(windowSettingsDir, "settings.properties");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage stage) throws Exception {
        stage.setTitle("Prüfungsvorbereitung");

        if (windowSettingsFile.exists() && windowSettingsFile.canRead()) {
            Properties windowProperties = new Properties();
            windowProperties.load(new FileReader(windowSettingsFile));
            if (Boolean.parseBoolean(windowProperties.getProperty("isFullScreen", "false"))) {
                stage.setFullScreen(true);
            } else if (Boolean.parseBoolean(windowProperties.getProperty("isMaximized", "false"))) {
                stage.setMaximized(true);
            } else {
                stage.setX(Double.parseDouble(windowProperties.getProperty("X")));
                stage.setY(Double.parseDouble(windowProperties.getProperty("Y")));
                stage.setHeight(Double.parseDouble(windowProperties.getProperty("Height")));
                stage.setWidth(Double.parseDouble(windowProperties.getProperty("Width")));
            }
            String dbPath = windowProperties.getProperty("databasePath", null);
            if (dbPath != null)
                DBController.getInstance().setDbPath(new File(dbPath));
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("style.fxml"));
        Parent root = loader.load();
        ((GuiController) loader.getController()).stage = stage;
        stage.setScene(new Scene(root));
        stage.show();
        stage.setOnHidden(windowEvent -> {
            Stage s = ((Stage) windowEvent.getSource());

            Properties windowProperties = new Properties();
            if (!windowSettingsDir.exists()) {
                windowSettingsDir.mkdirs();
            }
            if (windowSettingsFile.exists() && windowSettingsFile.canRead()) {
                try {
                    windowProperties.load(new FileReader(windowSettingsFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            windowProperties.setProperty("isFullScreen", String.valueOf(s.isFullScreen()));
            windowProperties.setProperty("isMaximized", String.valueOf(s.isMaximized()));
            windowProperties.setProperty("X", String.valueOf(s.getX()));
            windowProperties.setProperty("Y", String.valueOf(s.getY()));
            windowProperties.setProperty("Height", String.valueOf(s.getHeight()));
            windowProperties.setProperty("Width", String.valueOf(s.getWidth()));
            windowProperties.setProperty("databasePath", DBController.getInstance().getDbPath().getAbsolutePath());

            try {
                if (windowSettingsDir.canWrite()) {
                    if (windowSettingsFile.exists() && !windowSettingsFile.canWrite())
                        return;
                    windowProperties.store(new FileWriter(windowSettingsFile), null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
