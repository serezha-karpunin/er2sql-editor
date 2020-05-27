package com.etu.infrastructure.export;

import javafx.scene.layout.Region;

import java.io.File;

public interface ExportService {
    void exportAsImage(Region region, File file);

    void exportSql(String text, File file);
}
