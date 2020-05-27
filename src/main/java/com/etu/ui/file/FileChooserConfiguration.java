package com.etu.ui.file;

import com.etu.infrastructure.localization.LocalizationService;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.etu.infrastructure.localization.LocalizationConstants.*;

@Configuration
public class FileChooserConfiguration {

    @Autowired
    private LocalizationService localizationService;

    @Bean
    @Scope("prototype")
    public FileChooser addNewProjectFileChooser() {
        String title = localizationService.getLocalizedString(ADD_NEW_PROJECT_FILE_CHOOSER_TITLE_PROPERTY_NAME);
        return buildJsonFileChooser(title);
    }

    @Bean
    @Scope("prototype")
    public FileChooser loadProjectFileChooser() {
        String title = localizationService.getLocalizedString(LOAD_PROJECT_FILE_CHOOSER_TITLE_PROPERTY_NAME);
        return buildJsonFileChooser(title);
    }

    @Bean
    @Scope("prototype")
    public FileChooser saveProjectAsFileChooser() {
        String title = localizationService.getLocalizedString(SAVE_PROJECT_AS_FILE_CHOOSER_TITLE_PROPERTY_NAME);
        return buildJsonFileChooser(title);
    }

    @Bean
    @Scope("prototype")
    public FileChooser exportERModelFileChooser() {
        String title = localizationService.getLocalizedString(EXPORT_ERM_FILE_CHOOSER_TITLE_PROPERTY_NAME);
        return buildPngFileChooser(title);
    }

    @Bean
    @Scope("prototype")
    public FileChooser exportRelationalModelFileChooser() {
        String title = localizationService.getLocalizedString(EXPORT_RM_FILE_CHOOSER_TITLE_PROPERTY_NAME);
        return buildPngFileChooser(title);
    }

    @Bean
    @Scope("prototype")
    public FileChooser exportSqlFileChooser() {
        String title = localizationService.getLocalizedString(EXPORT_SQL_FILE_CHOOSER_TITLE_PROPERTY_NAME);
        return buildSqlFileChooser(title);
    }


    private FileChooser buildJsonFileChooser(String title) {
        String extFilterDescription = localizationService.getLocalizedString(MENU_FILE_JSON_EXTENSION_FILTER_DESCRIPTION_PROPERTY_NAME);
        String extFilter = localizationService.getLocalizedString(MENU_FILE_JSON_EXTENSION_FILTER_PROPERTY_NAME);

        return buildFileChooser(title, extFilterDescription, extFilter);
    }

    private FileChooser buildPngFileChooser(String title) {
        String extFilterDescription = localizationService.getLocalizedString(MENU_FILE_PNG_EXTENSION_FILTER_DESCRIPTION_PROPERTY_NAME);
        String extFilter = localizationService.getLocalizedString(MENU_FILE_PNG_EXTENSION_FILTER_PROPERTY_NAME);

        return buildFileChooser(title, extFilterDescription, extFilter);
    }

    private FileChooser buildSqlFileChooser(String title) {
        String extFilterDescription = localizationService.getLocalizedString(MENU_FILE_SQL_EXTENSION_FILTER_DESCRIPTION_PROPERTY_NAME);
        String extFilter = localizationService.getLocalizedString(MENU_FILE_SQL_EXTENSION_FILTER_PROPERTY_NAME);

        return buildFileChooser(title, extFilterDescription, extFilter);
    }

    private FileChooser buildFileChooser(String title, String extFilterDescription, String extFilter) {
        return new FileChooserBuilder()
                .withTitle(title)
                .withExtensionFilter(extFilterDescription, extFilter)
                .build();
    }
}
