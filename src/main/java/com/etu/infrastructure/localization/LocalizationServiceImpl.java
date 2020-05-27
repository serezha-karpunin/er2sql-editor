package com.etu.infrastructure.localization;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static java.lang.String.format;

@Component
public class LocalizationServiceImpl implements LocalizationService {
    private static final String BUNDLE_NAME = "l10n.lang";
    private static final String DEFAULT_LANGUAGE_CODE = "ru";

    private ResourceBundle resourceBundle;

    public LocalizationServiceImpl() {
        setLocale(DEFAULT_LANGUAGE_CODE);
    }

    @Override
    public void setLocale(String languageCode) {
        resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(languageCode), new UTF8Control());
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public String getLocalizedString(String key) {
        return getLocalizedStringFromBundle(key);
    }

    private String getLocalizedStringFromBundle(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException ex) {
            System.out.println(format("Couldn't find localized value for [%s] locale and [%s] key",
                    resourceBundle.getLocale(), key));
        }
        return key;
    }
}
