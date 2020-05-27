package com.etu.ui.alert;

import com.etu.infrastructure.localization.LocalizationService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.etu.infrastructure.localization.LocalizationConstants.*;

@Configuration
public class AlertConfiguration {
    @Autowired
    private LocalizationService localizationService;

    @Bean
    @Scope("prototype")
    public Alert saveOnCloseAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(localizationService.getLocalizedString(ALERT_SAVE_ON_CLOSE_TITLE_PROPERTY_NAME));
        alert.setHeaderText(localizationService.getLocalizedString(ALERT_SAVE_ON_CLOSE_HEADER_TEXT_PROPERTY_NAME));

        ButtonType saveButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_SAVE_ON_CLOSE_YES_PROPERTY_NAME), ButtonData.YES);
        ButtonType doNotSaveButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_SAVE_ON_CLOSE_NO_PROPERTY_NAME), ButtonData.NO);
        ButtonType cancelButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_CANCEL_PROPERTY_NAME), ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveButtonType, doNotSaveButtonType, cancelButtonType);
        return alert;
    }

    @Bean
    @Scope("prototype")
    public Alert removeErmEntityAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(localizationService.getLocalizedString(ALERT_REMOVE_ERM_ENTITY_TITLE_PROPERTY_NAME));
        alert.setHeaderText(localizationService.getLocalizedString(ALERT_REMOVE_ERM_ENTITY_HEADER_TEXT_PROPERTY_NAME));

        ButtonType removeButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_REMOVE_ERM_ENTITY_YES_PROPERTY_NAME), ButtonData.YES);
        ButtonType cancelButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_CANCEL_PROPERTY_NAME), ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(removeButtonType, cancelButtonType);
        return alert;
    }

    @Bean
    @Scope("prototype")
    public Alert removeErmRelationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(localizationService.getLocalizedString(ALERT_REMOVE_ERM_RELATION_TITLE_PROPERTY_NAME));
        alert.setHeaderText(localizationService.getLocalizedString(ALERT_REMOVE_ERM_RELATION_HEADER_TEXT_PROPERTY_NAME));

        ButtonType removeButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_REMOVE_ERM_RELATION_YES_PROPERTY_NAME), ButtonData.YES);
        ButtonType cancelButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_CANCEL_PROPERTY_NAME), ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(removeButtonType, cancelButtonType);
        return alert;
    }

    @Bean
    @Scope("prototype")
    public Alert oneToOneRelationDecisionAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("1-1 what to do");
        alert.setHeaderText("1-1");

        ButtonType mergeButtonType = new ButtonType("merge", ButtonData.YES);
        ButtonType doNotMergeButtonType = new ButtonType("dont merge", ButtonData.NO);

        alert.getButtonTypes().setAll(mergeButtonType, doNotMergeButtonType);
        return alert;
    }


    @Bean
    @Scope("prototype")
    public Alert shouldCreateId() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        ButtonType yesButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_BUTTON_YES_PROPERTY_NAME), ButtonData.YES);
        ButtonType noButtonType = new ButtonType(localizationService.getLocalizedString(ALERT_BUTTON_NO_PROPERTY_NAME), ButtonData.NO);

        alert.getButtonTypes().setAll(yesButtonType, noButtonType);
        return alert;
    }
}