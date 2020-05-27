package com.etu.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@Component
public class UiFactory {
    @Autowired
    private ApplicationContext applicationContext;

    public <T> T createView(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public <T extends Node, N> T createNode(String viewName, Consumer<Node> nodeConsumer, Consumer<N> controllerConsumer) {
        try {
            URL url = getClass().getResource(viewName + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            T node = fxmlLoader.load();

            if (nonNull(nodeConsumer)) {
                nodeConsumer.accept(node);
            }

            if (nonNull(controllerConsumer)) {
                controllerConsumer.accept(fxmlLoader.getController());
            }

            return node;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
