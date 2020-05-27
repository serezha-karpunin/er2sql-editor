package com.etu.ui.ermodel.relation.editor;

import com.etu.infrastructure.localization.LocalizationService;
import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.ui.AbstractView;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.etu.ui.util.UiUtils.preventColumnReordering;

@Component
@Scope("prototype")
public class ERModelRelationEditorView extends AbstractView {
    @FXML
    private AnchorPane ermRelationEditorViewRootAnchorPane;
    @FXML
    private VBox ermRelationEditorViewRootVBox;
    @FXML
    private TextField relationNameTextField;
    @FXML
    private Label relationTypeLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TableView<ERModelRelationSide> relationSideTableView;
    @FXML
    private TableColumn<ERModelRelationSide, String> relationSideEntityTableColumn;
    @FXML
    private TableColumn<ERModelRelationSide, Boolean> relationSideMandatoryTableColumn;

    @Autowired
    private ERModelRelationEditorMediator erModelRelationEditorMediator;
    @Autowired
    private LocalizationService localizationService;

    @Override
    protected void configureChildren() {
        ermRelationEditorViewRootVBox.prefHeightProperty().bind(ermRelationEditorViewRootAnchorPane.heightProperty());
        ermRelationEditorViewRootVBox.prefWidthProperty().bind(ermRelationEditorViewRootAnchorPane.widthProperty());

        relationSideTableView.setEditable(true);
        relationSideTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        preventColumnReordering(relationSideTableView);

        relationSideEntityTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        relationSideEntityTableColumn.setCellValueFactory(cellData -> cellData.getValue().getEntity().nameProperty());

        relationSideEntityTableColumn.setEditable(false);
        relationSideEntityTableColumn.setSortable(false);

        relationSideMandatoryTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(relationSideMandatoryTableColumn));
        relationSideMandatoryTableColumn.setCellValueFactory(cellData -> cellData.getValue().mandatoryProperty());
        relationSideMandatoryTableColumn.setSortable(false);
    }

    @Override
    protected void configureBindings() {
        relationNameTextField.textProperty().bindBidirectional(getMediator().relationNameProperty());
        relationSideTableView.itemsProperty().bindBidirectional(getMediator().relationSidesProperty());

        relationSideTableView.prefHeightProperty().bind(Bindings.size(relationSideTableView.getItems()).add(1).multiply(25));

        getMediator().relationTypeProperty().addListener(((observable, oldValue, newValue) -> {
            relationTypeLabel.setText(localizationService.getLocalizedString(newValue.name()));
            switch (newValue) {
                case ONE_TO_ONE:
                case ONE_TO_MANY:
                case MANY_TO_MANY:
                    relationSideMandatoryTableColumn.setVisible(true);
                    break;
                default:
                    relationSideMandatoryTableColumn.setVisible(false);
            }
        }));
    }

    @FXML
    private void save() {
        getMediator().save();
    }

    @FXML
    private void remove() {
        getMediator().remove();
    }

    @FXML
    private void cancel() {
        getMediator().close();
    }

    @Override
    public Parent getRootNode() {
        return ermRelationEditorViewRootAnchorPane;
    }

    public ERModelRelationEditorMediator getMediator() {
        return erModelRelationEditorMediator;
    }
}
