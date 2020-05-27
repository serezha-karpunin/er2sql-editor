package com.etu.ui.util;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

public class EditorTableRowFactory<ITEM> implements Callback<TableView<ITEM>, TableRow<ITEM>> {
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private TableView<ITEM> tableView;

    public EditorTableRowFactory(TableView<ITEM> tableView) {
        this.tableView = tableView;
    }

    @Override
    public TableRow<ITEM> call(TableView<ITEM> tv) {
        TableRow<ITEM> row = new TableRow<>();

        row.setOnDragDetected(event -> {
            if (!row.isEmpty()) {
                Integer index = row.getIndex();

                ClipboardContent cc = new ClipboardContent();
                cc.put(SERIALIZED_MIME_TYPE, index);

                Dragboard dragboard = row.startDragAndDrop(TransferMode.MOVE);
                dragboard.setDragView(row.snapshot(null, null));

                dragboard.setContent(cc);
                event.consume();
            }
        });

        row.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
                if (row.getIndex() != (Integer) dragboard.getContent(SERIALIZED_MIME_TYPE)) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            }
        });

        row.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {

                int draggedIndex = (Integer) dragboard.getContent(SERIALIZED_MIME_TYPE);
                ITEM draggedItem = tableView.getItems().remove(draggedIndex);

                int dropIndex;

                if (row.isEmpty()) {
                    dropIndex = tableView.getItems().size();
                } else {
                    dropIndex = row.getIndex();
                }

                tableView.getItems().add(dropIndex, draggedItem);

                event.setDropCompleted(true);
                tableView.getSelectionModel().select(dropIndex);
                event.consume();
            }
        });

        return row;
    }
}