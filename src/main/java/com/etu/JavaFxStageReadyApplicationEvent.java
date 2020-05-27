package com.etu;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

import static java.util.Optional.ofNullable;

public class JavaFxStageReadyApplicationEvent extends ApplicationEvent {
    public JavaFxStageReadyApplicationEvent(Stage stage) {
        super(stage);
    }

    public Stage getStage() {
        return ofNullable(getSource())
                .filter(Stage.class::isInstance)
                .map(Stage.class::cast)
                .orElseThrow(IllegalStateException::new);
    }
}
