package com.etu;

import com.etu.infrastructure.PrimaryStageProvider;
import com.etu.ui.UiFactory;
import com.etu.ui.main.MainView;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class JavaFxStageReadyApplicationEventListener implements ApplicationListener<JavaFxStageReadyApplicationEvent> {

    @Autowired
    private PrimaryStageProvider primaryStageProvider;
    @Autowired
    private UiFactory uiFactory;

    @Override
    public void onApplicationEvent(JavaFxStageReadyApplicationEvent event) {
        Stage stage = event.getStage();

        registerStageInContext(stage);
        configureStage(stage);
        showStage(stage);
    }

    private void registerStageInContext(Stage stage) {
        primaryStageProvider.setPrimaryStage(stage);
    }

    private void configureStage(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        MainView view = uiFactory.createView(MainView.class);
        stage.setScene(new Scene(view.getRootNode()));
    }

    private void showStage(Stage stage) {
        stage.show();
    }
}
