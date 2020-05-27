package com.etu.infrastructure.export;

import com.etu.infrastructure.file.FileService;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import static javafx.embed.swing.SwingFXUtils.fromFXImage;

@Component
public class ExportServiceImpl implements ExportService {
    @Autowired
    private FileService fileService;

    @Override
    public void exportAsImage(Region region, File file) {
        try {
            WritableImage writableImage = new WritableImage(
                    (int) region.getWidth() + 20,
                    (int) region.getHeight() + 20
            );
            region.snapshot(null, writableImage);

            RenderedImage renderedImage = fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void exportSql(String text, File file) {
        fileService.saveSql(file, text);
    }
}
