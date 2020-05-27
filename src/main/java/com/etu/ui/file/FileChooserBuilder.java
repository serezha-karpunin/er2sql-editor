package com.etu.ui.file;

import javafx.stage.FileChooser;

import java.io.File;

public class FileChooserBuilder {
    private FileChooser fileChooser;

    public FileChooserBuilder() {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/downloads"));
    }

    public FileChooserBuilder withTitle(String title) {
        fileChooser.setTitle(title);

        return this;
    }

    public FileChooserBuilder withExtensionFilter(String description, String... extensions) {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.getExtensionFilters().add(extFilter);

        return this;
    }

    public FileChooser build() {
        return fileChooser;
    }
}
