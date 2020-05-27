package com.etu.ui.ermodel.entity.editor;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelEntityAttribute;
import com.etu.ui.AbstractView;
import com.etu.ui.util.EditorTableRowFactory;
import com.etu.ui.util.RemoveTableRowCellFactory;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
public class ERModelEntityEditorView extends AbstractView {
    @FXML
    private AnchorPane ermEntityEditorViewRootAnchorPane;
    @FXML
    private VBox ermEntityEditorViewRootVBox;
    @FXML
    private TextField entityNameTextField;
    @FXML
    private TableView<ERModelEntityAttribute> attributeTableView;
    @FXML
    private TableColumn<ERModelEntityAttribute, Boolean> attributeKeyTableColumn;
    @FXML
    private TableColumn<ERModelEntityAttribute, String> attributeNameTableColumn;
    @FXML
    private TableColumn<ERModelEntityAttribute, Void> attributeRemoveTableColumn;

    @Autowired
    private ERModelEntityEditorMediator erModelEntityEditorMediator;

    @Override
    protected void configureChildren() {
        ermEntityEditorViewRootVBox.prefHeightProperty().bind(ermEntityEditorViewRootAnchorPane.heightProperty());
        ermEntityEditorViewRootVBox.prefWidthProperty().bind(ermEntityEditorViewRootAnchorPane.widthProperty());

        attributeTableView.setEditable(true);
        attributeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        preventColumnReordering(attributeTableView);
        attributeTableView.setRowFactory(new EditorTableRowFactory<>(attributeTableView));

        attributeKeyTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(attributeKeyTableColumn));
        attributeKeyTableColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        attributeKeyTableColumn.setSortable(false);

        attributeNameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        attributeNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        attributeNameTableColumn.setSortable(false);

        attributeRemoveTableColumn.setCellFactory(new RemoveTableRowCellFactory<>());
        attributeRemoveTableColumn.setSortable(false);
    }

    @Override
    protected void configureBindings() {
        entityNameTextField.textProperty().bindBidirectional(getMediator().entityNameProperty());
        attributeTableView.itemsProperty().bindBidirectional(getMediator().entityAttributesProperty());
    }

    @FXML
    private void addAttribute() {
        getMediator().addAttribute();
    }

    @FXML
    private void confirm() {
        getMediator().confirm();
    }

    @FXML
    private void cancel() {
        getMediator().close();
    }

    @Override
    public Parent getRootNode() {
        return ermEntityEditorViewRootAnchorPane;
    }

    public ERModelEntityEditorMediator getMediator() {
        return erModelEntityEditorMediator;
    }
}
