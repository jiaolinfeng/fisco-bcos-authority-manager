package com.esunny.controller;

import java.io.File;

import com.esunny.ui.util.DialogUtils;
import com.esunny.util.FormatUtils; 

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ImportGroupDialogController {

    @FXML
    private TextField groupField;

    private String groupAddress; 

    private Stage dialogStage;
    
    private boolean confirmed = false;

    @FXML
    private void handleConfirm(ActionEvent event) {

        groupAddress = groupField.getText().trim();
        
        if (!FormatUtils.isAddress(groupAddress)) {
            DialogUtils.alert("用户组地址格式不正确");
            return;
        }

        confirmed = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public String getGroupAddress() {
        return groupAddress;
    }

}
