package com.etu.ui;

import com.etu.infrastructure.localization.LocalizationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

public abstract class AbstractView {
    private static final String DEFAULT_VIEW_SUFFIX = "View";

    @PostConstruct
    public void postConstruct() {
        loadFxml();
    }

    @Autowired
    private LocalizationService localizationService;

    @FXML
    public void initialize() {
        configureChildren();
        configureBindings();
        configureLocalization();
    }

    protected void configureChildren() {
    }

    protected void configureBindings() {
    }

    protected void configureLocalization() {
    }

    private void loadFxml() {
        try {
            URL url = getClass().getResource(determineViewName() + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setResources(localizationService.getResourceBundle());
            fxmlLoader.setControllerFactory((clazz) -> this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String determineViewName() {
        String className = getClass().getSimpleName();
        int suffixIndex = className.lastIndexOf(DEFAULT_VIEW_SUFFIX);
        return className.substring(0, suffixIndex);
    }

    public abstract Parent getRootNode();
}
