package com.etu.ui.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class RemoveTableRowCellFactory<ITEM> implements Callback<TableColumn<ITEM, Void>, TableCell<ITEM, Void>> {
    @Override
    public TableCell<ITEM, Void> call(TableColumn<ITEM, Void> param) {
        return new TableCell<ITEM, Void>() {
            private Button removeButton = new Button();

            {
                removeButton.setGraphic(getIconGraphic());
                removeButton.setOnAction((ActionEvent event) -> getTableView().getItems().remove(getIndex()));
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }

            private Node getIconGraphic() {
                FontAwesomeIconView icon = new FontAwesomeIconView();
                icon.setGlyphName("TRASH");
                icon.setSize("12px");
                icon.setGlyphStyle("-fx-fill: black;");

                return icon;
            }
        };
    }
}
