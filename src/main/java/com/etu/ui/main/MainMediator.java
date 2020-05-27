package com.etu.ui.main;

import com.etu.infrastructure.event.EventFacade;
import com.etu.infrastructure.export.ExportService;
import com.etu.infrastructure.localization.LocalizationService;
import com.etu.infrastructure.state.ProjectStateService;
import com.etu.infrastructure.state.dto.runtime.project.ProjectState;
import com.etu.infrastructure.workflow.service.WorkflowService;
import com.etu.infrastructure.workflow.service.WorkflowType;
import com.etu.ui.AbstractMediator;
import javafx.beans.property.*;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.etu.infrastructure.event.dto.EventTypes.*;
import static com.etu.infrastructure.localization.LocalizationConstants.PRIMARY_STAGE_TITLE_PROPERTY_NAME;
import static com.etu.ui.main.MainMediator.MainMediatorActiveTab.RM;
import static com.etu.ui.main.MainMediator.MainMediatorActiveTab.SQL;

@Component
@Scope("prototype")
public class MainMediator extends AbstractMediator {
    @Autowired
    private EventFacade eventFacade;
    @Autowired
    private LocalizationService localizationService;
    @Autowired
    private ProjectStateService projectStateService;
    @Autowired
    private ExportService exportService;
    @Autowired
    private WorkflowService workflowService;

    private StringProperty primaryStageTitle = new SimpleStringProperty();
    private ObjectProperty<MainMediatorState> mainViewState = new SimpleObjectProperty<>(MainMediatorState.START_SCREEN);
    private BooleanProperty locked = new SimpleBooleanProperty(false);

    private ObjectProperty<MainMediatorActiveTab> activeTab = new SimpleObjectProperty<>(MainMediatorActiveTab.ERM);

    @Override
    protected void registerListeners() {
        eventFacade.addEventListener(PROJECT_OPENED, this::handleProjectOpened);
        eventFacade.addEventListener(PROJECT_CLOSED, this::handleProjectClosed);

        eventFacade.addEventListener(ERM_RELATION_CREATION_STARTED, this::lock);
        eventFacade.addEventListener(ERM_RELATION_CREATION_FINISHED, this::unlock);

        eventFacade.addEventListener(RM_GENERATED, this::openRmTab);
        eventFacade.addEventListener(SQL_GENERATED, this::openSqlTab);
    }

    public void addNewProject() {
        workflowService.startProjectWorkflow(WorkflowType.ADD_NEW_PROJECT);
    }

    public void loadProject() {
        workflowService.startProjectWorkflow(WorkflowType.LOAD_PROJECT);
    }

    public void saveProject() {
        workflowService.startProjectWorkflow(WorkflowType.SAVE_PROJECT);
    }

    public void saveProjectAs() {
        workflowService.startProjectWorkflow(WorkflowType.SAVE_PROJECT_AS);
    }

    public void closeProject() {
        workflowService.startProjectWorkflow(WorkflowType.CLOSE_PROJECT);
    }

    public void exportErModel() {
        workflowService.startProjectWorkflow(WorkflowType.EXPORT_ERM);
    }

    public void exportRelationalModel() {
        workflowService.startProjectWorkflow(WorkflowType.EXPORT_RM);
    }

    public void exportGeneratedSql() {
        workflowService.startProjectWorkflow(WorkflowType.EXPORT_GENERATED_SQL);
    }

    private void handleProjectOpened(Event event) {
        ProjectState projectState = projectStateService.getProjectState();
        String stageTitle = localizationService.getLocalizedString(PRIMARY_STAGE_TITLE_PROPERTY_NAME) + " - " + projectState.getProjectName();

        primaryStageTitle.set(stageTitle);
        mainViewState.set(MainMediatorState.EDITOR_SCREEN);
    }

    private void handleProjectClosed(Event event) {
        primaryStageTitle.set("");
        mainViewState.set(MainMediatorState.START_SCREEN);
    }

    private void lock(Event event) {
        locked.set(true);
    }

    private void unlock(Event event) {
        locked.set(false);
    }

    private void openRmTab(Event event) {
        activeTab.set(RM);
    }

    private void openSqlTab(Event event) {
        activeTab.set(SQL);
    }

    public StringProperty primaryStageTitleProperty() {
        return primaryStageTitle;
    }

    public BooleanProperty lockedProperty() {
        return locked;
    }

    public ObjectProperty<MainMediatorState> stateProperty() {
        return mainViewState;
    }

    public ObjectProperty<MainMediatorActiveTab> activeTabProperty() {
        return activeTab;
    }

    public enum MainMediatorState {
        START_SCREEN,
        EDITOR_SCREEN
    }

    public enum MainMediatorActiveTab {
        ERM,
        RM,
        SQL
    }
}
