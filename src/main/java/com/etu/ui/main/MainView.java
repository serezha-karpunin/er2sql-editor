package com.etu.ui.main;

import com.etu.infrastructure.PrimaryStageProvider;
import com.etu.ui.AbstractView;
import com.etu.ui.UiFactory;
import com.etu.ui.ermodel.tab.ERModelTabView;
import com.etu.ui.rmodel.tab.RModelTabView;
import com.etu.ui.sql.tab.SqlTabView;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.etu.ui.main.MainMediator.MainMediatorState.EDITOR_SCREEN;
import static com.etu.ui.main.MainMediator.MainMediatorState.START_SCREEN;

@Component
@Scope("prototype")
public class MainView extends AbstractView {
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private VBox mainVBox;
    @FXML
    private VBox startVBox;
    @FXML
    private Button addNewProjectButton;
    @FXML
    private Button loadProjectButton;

    @FXML
    private MenuBar menuBar;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab ermTab;
    @FXML
    private Tab rmTab;
    @FXML
    private Tab sqlTab;

    @Autowired
    private MainMediator mainMediator;
    @Autowired
    private UiFactory uiFactory;
    @Autowired
    private PrimaryStageProvider primaryStageProvider;

    @Override
    protected void configureChildren() {
        mainVBox.prefHeightProperty().bind(mainAnchorPane.heightProperty());
        mainVBox.prefWidthProperty().bind(mainAnchorPane.widthProperty());

        addNewProjectButton.setMaxWidth(Double.MAX_VALUE);
        loadProjectButton.setMaxWidth(Double.MAX_VALUE);

        ermTab.setContent(uiFactory.createView(ERModelTabView.class).getRootNode());
        rmTab.setContent(uiFactory.createView(RModelTabView.class).getRootNode());
        sqlTab.setContent(uiFactory.createView(SqlTabView.class).getRootNode());
    }

    @Override
    protected void configureBindings() {
        primaryStageProvider.getPrimaryStage().titleProperty().bind(mainMediator.primaryStageTitleProperty());

        startVBox.managedProperty().bind(
                Bindings.when(mainMediator.stateProperty().isEqualTo(START_SCREEN))
                        .then(true)
                        .otherwise(false)
        );

        startVBox.visibleProperty().bind(
                Bindings.when(mainMediator.stateProperty().isEqualTo(START_SCREEN))
                        .then(true)
                        .otherwise(false)
        );

        mainTabPane.managedProperty().bind(
                Bindings.when(mainMediator.stateProperty().isEqualTo(EDITOR_SCREEN))
                        .then(true)
                        .otherwise(false)
        );

        mainTabPane.visibleProperty().bind(
                Bindings.when(mainMediator.stateProperty().isEqualTo(EDITOR_SCREEN))
                        .then(true)
                        .otherwise(false)
        );

        menuBar.disableProperty().bind(mainMediator.lockedProperty());
        ermTab.disableProperty().bind(mainMediator.lockedProperty().and(ermTab.selectedProperty().not()));
        rmTab.disableProperty().bind(mainMediator.lockedProperty().and(rmTab.selectedProperty().not()));
        sqlTab.disableProperty().bind(mainMediator.lockedProperty().and(sqlTab.selectedProperty().not()));

        mainMediator.activeTabProperty().addListener(getActiveTabPropertyChangeListener());
    }

    private ChangeListener<? super MainMediator.MainMediatorActiveTab> getActiveTabPropertyChangeListener() {
        return ((observable, oldValue, newValue) -> {
            switch (newValue) {
                case ERM:
                    mainTabPane.getSelectionModel().select(ermTab);
                    break;
                case RM:
                    mainTabPane.getSelectionModel().select(rmTab);
                    break;
                case SQL:
                    mainTabPane.getSelectionModel().select(sqlTab);
            }
        });
    }

    @FXML
    private void addNewProject() {
        mainMediator.addNewProject();
    }

    @FXML
    private void loadProject() {
        mainMediator.loadProject();
    }

    @FXML
    private void saveProject() {
        mainMediator.saveProject();
    }

    @FXML
    private void saveProjectAs() {
        mainMediator.saveProjectAs();
    }

    @FXML
    private void closeProject() {
        mainMediator.closeProject();
    }

    @FXML
    private void exportErModel() {
        mainMediator.exportErModel();
    }

    @FXML
    private void exportRelationalModel() {
        mainMediator.exportRelationalModel();
    }

    @FXML
    private void exportGeneratedSql() {
        mainMediator.exportGeneratedSql();
    }

    @Override
    public Parent getRootNode() {
        return mainAnchorPane;
    }
}
