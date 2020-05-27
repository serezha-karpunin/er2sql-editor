package com.etu.infrastructure;

import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class PrimaryStageProvider {
    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
