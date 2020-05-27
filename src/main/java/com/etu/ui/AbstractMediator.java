package com.etu.ui;

import javax.annotation.PostConstruct;

public abstract class AbstractMediator implements Mediator {
    @PostConstruct
    public void postConstruct() {
        registerListeners();
    }

    protected void registerListeners() {
    }
}
