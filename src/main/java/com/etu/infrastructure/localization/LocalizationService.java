package com.etu.infrastructure.localization;

import java.util.ResourceBundle;

public interface LocalizationService {
    void setLocale(String languageCode);

    ResourceBundle getResourceBundle();

    String getLocalizedString(String key);
}
