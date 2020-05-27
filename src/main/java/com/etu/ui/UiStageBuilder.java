package com.etu.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UiStageBuilder {
    private Stage stage;

    public UiStageBuilder() {
        stage = new Stage();
    }

    public UiStageBuilder withView(Parent view) {
        Scene scene = new Scene(view);
        stage.setScene(scene);

        return this;
    }

    public UiStageBuilder withModality(Modality modality) {
        stage.initModality(modality);

        return this;
    }

    public UiStageBuilder withTitle(String title) {
        stage.setTitle(title);

        return this;
    }

    public Stage build() {
        return stage;
    }
}
