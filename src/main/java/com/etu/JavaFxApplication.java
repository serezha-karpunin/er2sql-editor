package com.etu;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaFxApplication extends Application {
    private ApplicationContext context;

    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(JavaFxApplicationSpringConfiguration.class);
    }

    @Override
    public void start(Stage stage) {
        context.publishEvent(new JavaFxStageReadyApplicationEvent(stage));
    }
}