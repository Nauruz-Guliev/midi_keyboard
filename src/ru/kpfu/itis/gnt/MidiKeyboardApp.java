package ru.kpfu.itis.gnt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Objects;


public class MidiKeyboardApp extends Application {
    private Controller controller;
    private final String XML_FILE_PATH = "UI/Main.fxml";
    private final String CSS_FILE_PATH = "UI/appStyling.css";
    private final String APP_TITLE = "MidiKeyboard";
    private final String ICON_PATH = "ru/kpfu/itis/gnt/UI/img.jpg";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(XML_FILE_PATH));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_FILE_PATH)).toExternalForm());
        scene.getRoot().requestFocus();
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(ICON_PATH))));
        handleClicks(scene, root);
    }


    public void handleClicks(Scene scene, Parent root) {
        scene.setOnKeyPressed(event -> {
            controller.onKeyPressed(event);
            if (searchForButton(event)) {
                root.requestFocus();
            }
        });
        scene.setOnKeyReleased(event -> {
            controller.onKeyReleased(event);
            if (searchForButton(event)) {
                root.requestFocus();
            }
        });
    }

    public boolean searchForButton(KeyEvent event) {
        for (KeyCode[] numButton : MidiNotesEntity.numButtons) {
            for (int i = 0; i < MidiNotesEntity.numButtons[i].length; i++) {
                if (numButton[i].equals(event.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }
}
