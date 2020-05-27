package com.etu.ui.rmodel.relation.editor;

import com.etu.infrastructure.localization.LocalizationService;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttribute;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelationAttributeDataType;
import com.etu.ui.AbstractView;
import com.etu.ui.util.EditorTableRowFactory;
import com.etu.ui.util.RemoveTableRowCellFactory;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.etu.ui.util.UiUtils.preventColumnReordering;


@Component
@Scope("prototype")
public class RModelRelationEditorView extends AbstractView {
    @FXML
    private AnchorPane rmRelationEditorViewRootAnchorPane;
    @FXML
    private VBox rmRelationEditorViewRootVBox;
    @FXML
    private TextField relationNameTextField;
    @FXML
    private TableView<RModelRelationAttribute> attributeTableView;
    @FXML
    private TableColumn<RModelRelationAttribute, Boolean> attributeKeyTableColumn;
    @FXML
    private TableColumn<RModelRelationAttribute, String> attributeNameTableColumn;
    @FXML
    private TableColumn<RModelRelationAttribute, RModelRelationAttributeDataType> attributeTypeTableColumn;
    @FXML
    private TableColumn<RModelRelationAttribute, Void> attributeRemoveTableColumn;

    @Autowired
    private RModelRelationEditorMediator rModelRelationEditorMediator;
    @Autowired
    private LocalizationService localizationService;

    @Override
    protected void configureChildren() {
        rmRelationEditorViewRootVBox.prefHeightProperty().bind(rmRelationEditorViewRootAnchorPane.heightProperty());
        rmRelationEditorViewRootVBox.prefWidthProperty().bind(rmRelationEditorViewRootAnchorPane.widthProperty());

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

        attributeTypeTableColumn.setCellFactory(getAttributeTypeCellFactory());
        attributeTypeTableColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        attributeTypeTableColumn.setSortable(false);

        attributeRemoveTableColumn.setCellFactory(new RemoveTableRowCellFactory<>());
        attributeRemoveTableColumn.setSortable(false);
    }

    private Callback<TableColumn<RModelRelationAttribute, RModelRelationAttributeDataType>, TableCell<RModelRelationAttribute, RModelRelationAttributeDataType>> getAttributeTypeCellFactory() {
        return ComboBoxTableCell.forTableColumn(
                new LocalizedDataTypeStringConverter(dataType -> localizationService.getLocalizedString(dataType.name())),
                RModelRelationAttributeDataType.values()
        );
    }

    @Override
    protected void configureBindings() {
        relationNameTextField.textProperty().bindBidirectional(getMediator().relationNameProperty());
        attributeTableView.itemsProperty().bindBidirectional(getMediator().relationAttributesProperty());
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
        return rmRelationEditorViewRootAnchorPane;
    }

    public RModelRelationEditorMediator getMediator() {
        return rModelRelationEditorMediator;
    }

    private static class LocalizedDataTypeStringConverter extends StringConverter<RModelRelationAttributeDataType> {

        private Function<RModelRelationAttributeDataType, String> localizeFunction;

        private LocalizedDataTypeStringConverter(Function<RModelRelationAttributeDataType, String> localizeFunction) {
            this.localizeFunction = localizeFunction;
        }

        @Override
        public String toString(RModelRelationAttributeDataType dataType) {
            return localizeFunction.apply(dataType);
        }

        @Override
        public RModelRelationAttributeDataType fromString(String string) {
            return Stream.of(RModelRelationAttributeDataType.values())
                    .filter(dataType -> Objects.equals(localizeFunction.apply(dataType), string))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
        }
    }
}
