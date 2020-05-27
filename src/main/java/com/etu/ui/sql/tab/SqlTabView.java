package com.etu.ui.sql.tab;

import com.etu.infrastructure.state.dto.runtime.sql.DBMS;
import com.etu.ui.AbstractView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static javafx.collections.FXCollections.observableArrayList;

@Component
@Scope("prototype")
public class SqlTabView extends AbstractView {

    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TextArea sqlTextArea;
    @FXML
    private ComboBox<DBMS> targetDbmsComboBox;

    @Autowired
    private SqlTabMediator sqlTabMediator;

    @Override
    protected void configureChildren() {
        targetDbmsComboBox.setItems(observableArrayList(DBMS.values()));
        targetDbmsComboBox.getSelectionModel().select(0);

        targetDbmsComboBox.valueProperty().addListener((observable, oldValue, newValue )-> {
            sqlTabMediator.selectDbms(newValue);
        });
    }

    @Override
    protected void configureBindings() {
        sqlTextArea.textProperty().bind(sqlTabMediator.generatedSqlProperty());
    }

    @Override
    public Parent getRootNode() {
        return rootAnchorPane;
    }
}
