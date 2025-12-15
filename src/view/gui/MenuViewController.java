package view.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.adt.*;
import model.statements.IStmt;
import model.types.Type;
import view.Examples;

import java.io.IOException;

public class MenuViewController {
    @FXML private ListView<IStmt> programsList;

    @FXML
    public void initialize() {
        ObservableList<IStmt> programsItems = FXCollections.observableArrayList();

        for (IStmt stmt : Examples.getAllExamples()) {
            try {
                MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
                stmt.typeCheck(typeEnv);
                programsItems.add(stmt);
            } catch(Exception ignored) {}
        }

        programsList.setItems(programsItems);
    }

    @FXML
    public void runButtonClicked() {
        // ListView default SelectionModel = SelectionModel.SINGLE
        // getSelectedItem() -> returns object contained in the selected index position
        IStmt selectedProgram = programsList.getSelectionModel().getSelectedItem();

        if (selectedProgram == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Program Selected");
            alert.setContentText("Please select a Program from the list.");
            alert.showAndWait();
            return;
        }

        try {
            // load MainView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainViewController c = loader.getController();
            c.setProgram(selectedProgram);

            // create and show new window
            Stage mainStage = new Stage();
            mainStage.setTitle("Main Program");
            mainStage.setScene(new Scene(root, 600, 700));
            mainStage.show();

        } catch(IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FXML Error");
            alert.setHeaderText("Could not load MainView.fxml.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
