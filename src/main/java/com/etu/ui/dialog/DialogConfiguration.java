package com.etu.ui.dialog;

import com.etu.infrastructure.localization.LocalizationService;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.etu.infrastructure.localization.LocalizationConstants.DIALOG_ID_NAME_CONTENT_TEXT_PROPERTY_NAME;
import static com.etu.infrastructure.localization.LocalizationConstants.DIALOG_SCHEMA_NAME_CONTENT_TEXT_PROPERTY_NAME;

@Configuration
public class DialogConfiguration {

    @Autowired
    private LocalizationService localizationService;

    @Bean
    @Scope("prototype")
    public TextInputDialog schemaNameTextInputDialog() {
        TextInputDialog dialog = new TextInputDialog("generated_schema");

        dialog.setContentText(localizationService.getLocalizedString(DIALOG_SCHEMA_NAME_CONTENT_TEXT_PROPERTY_NAME));

        ObservableList<ButtonType> buttonTypes = dialog.getDialogPane().getButtonTypes();
        buttonTypes.clear();

        ButtonType okButtonType = ButtonType.OK;
        buttonTypes.addAll(okButtonType);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);

        dialog.getEditor().textProperty().addListener(
                (observable, oldValue, newValue) -> okButton.setDisable(newValue.trim().isEmpty())
        );

        return dialog;
    }

    @Bean
    @Scope("prototype")
    public TextInputDialog idInputDialog() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setContentText(localizationService.getLocalizedString(DIALOG_ID_NAME_CONTENT_TEXT_PROPERTY_NAME));

        ObservableList<ButtonType> buttonTypes = dialog.getDialogPane().getButtonTypes();
        buttonTypes.clear();

        ButtonType okButtonType = ButtonType.OK;
        buttonTypes.addAll(okButtonType);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);

        dialog.getEditor().textProperty().addListener(
                (observable, oldValue, newValue) -> okButton.setDisable(newValue.trim().isEmpty())
        );

        return dialog;
    }
}
