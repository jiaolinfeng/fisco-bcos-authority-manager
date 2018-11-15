package com.esunny.ui.util;

import java.io.File;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogUtils {
    public static void alert(String msg) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void info(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static File chooseFile(String title, String filterName, String filterContent, Stage parent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        if (!filterName.isEmpty()) {
            fileChooser.getExtensionFilters().add(new ExtensionFilter(filterName, filterContent));
        }
        return fileChooser.showOpenDialog(parent);
    }
}
