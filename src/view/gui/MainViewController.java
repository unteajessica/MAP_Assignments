package view.gui;

import controller.Controller;
import exceptions.MyException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PrgState;
import model.adt.*;
import model.statements.IStmt;
import model.types.Type;
import model.values.Value;
import repository.IRepo;
import repository.Repo;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainViewController {

    @FXML private TextField nrPrgStatesField;
    @FXML private ListView<Integer> PrgStatesIDs;
    @FXML private TableView<HeapRow> heapTableView;
    @FXML private TableColumn<HeapRow, Number> heapAddressCol;
    @FXML private TableColumn<HeapRow, String> heapValueCol;

    @FXML private TableView<SymRow> symTableView;
    @FXML private TableColumn<SymRow, String> symNameCol;
    @FXML private TableColumn<SymRow, String> symValueCol;

    @FXML private ListView<String> exeStackList;
    @FXML private ListView<String> outList;
    @FXML private ListView<String> fileTableList;

    @FXML private Button runOneStepButton;

    private Controller controller;

    public static class HeapRow {
        private final SimpleIntegerProperty address = new SimpleIntegerProperty();
        private final SimpleStringProperty value = new SimpleStringProperty();

        public HeapRow(int address, String value) {
            this.address.set(address);
            this.value.set(value);
        }
        public int getAddress() { return address.get(); }
        public String getValue() { return value.get(); }
    }

    public static class SymRow {
        private final SimpleStringProperty name = new SimpleStringProperty();
        private final SimpleStringProperty value = new SimpleStringProperty();

        public SymRow(String name, String value) {
            this.name.set(name);
            this.value.set(value);
        }
        public String getName() { return name.get(); }
        public String getValue() { return value.get(); }
    }

    @FXML
    public void initialize() {
        // only if controls exist in FXML (aka JavaFX found these nodes in the FXML file)
        if (heapAddressCol != null) heapAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        if (heapValueCol != null) heapValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        if (symNameCol != null) symNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (symValueCol != null) symValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        // when user selects a program state id -> update symTable + exeStack for that prg
        if (PrgStatesIDs != null) {
            // selectedItemProperty() -> observable property that changes whenever the user selects another item
            // obs -> property being observed
            // oldV -> previously selected ID
            // newV -> newly selected ID
            PrgStatesIDs.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
                if (newV != null) refreshSelectedPrgStateDetails(newV);
            });
        }
    }

    public void setProgram(IStmt selectedProgram) {
        try {
            // 1) typecheck
            MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
            selectedProgram.typeCheck(typeEnv);

            // 2) create initial program state
            var exeStack = new MyStack<IStmt>();
            var symTable = new MyDictionary<String, Value>();
            var out = new MyList<Value>();
            var heap = new MyHeap();
            var fileTable = new MyFileTable();

            PrgState prg = new PrgState(exeStack, symTable, out, selectedProgram, fileTable, heap);

            // 3) repo + controller
            IRepo repo = new Repo(prg, "log.txt");
            this.controller = new Controller(repo);

            // 4) initial UI
            refreshAll();

        } catch (MyException exp) {
            showError("TypeCheck Error", exp.getMessage());
        }
    }

    // --------- button handlers (hook these from FXML onAction) ---------

    @FXML
    private void onRunOneStepClicked() {
        if (controller == null) {
            showWarning("No program loaded", "Select a program first.");
            return;
        }

        try {
            // runs one step for all programs
            var prgList = controller.removeCompletedPrg(controller.getRepo().getProgramsList());
            if (prgList.isEmpty()) {
                // program finished
                showWarning("Done", "Program has finished execution.");
                return;
            }
            try {
                controller.oneStepForAllPrg(controller.getRepo().getProgramsList());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            // after step, remove completed again and store
            prgList = controller.removeCompletedPrg(controller.getRepo().getProgramsList());
            controller.getRepo().setProgramsList(prgList);

            // update GUI after step
            refreshAll();

        } catch (Exception e) {
            e.printStackTrace(); // shows real error in console

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Execution Error");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }

    }

    // --------- refresh methods ---------

    private void refreshAll() {
        // get current program states from controller/repo
        List<PrgState> prgStates = controller.getRepo().getProgramsList();

        // number of prg states
        if (nrPrgStatesField != null) {
            nrPrgStatesField.setText("Number of Program States: " + prgStates.size());
        }

        // prg state ids list
        if (PrgStatesIDs != null) {
            List<Integer> ids = prgStates.stream()
                    .map(PrgState::getId)
                    .sorted()
                    .collect(Collectors.toList());

            PrgStatesIDs.setItems(FXCollections.observableArrayList(ids));

            // auto-select first if none selected
            if (!ids.isEmpty() && PrgStatesIDs.getSelectionModel().getSelectedItem() == null) {
                PrgStatesIDs.getSelectionModel().select(ids.getFirst());
            }
        }

        // heap + out + file table are usually shared (take from first prg)
        if (!prgStates.isEmpty()) {
            PrgState first = prgStates.getFirst();

            refreshHeap(first);
            refreshOut(first);
            refreshFileTable(first);
        }

        // refresh symTable + exeStack for currently selected id
        Integer selectedId = (PrgStatesIDs != null) ? PrgStatesIDs.getSelectionModel().getSelectedItem() : null;
        if (selectedId != null) {
            refreshSelectedPrgStateDetails(selectedId);
        }
    }

    private void refreshHeap(PrgState prg) {
        if (heapTableView == null) return;

        ObservableList<HeapRow> rows = FXCollections.observableArrayList();

        for (Map.Entry<Integer, Value> e : prg.getHeap().getHeap().entrySet()) {
            rows.add(new HeapRow(e.getKey(), e.getValue().toString()));
        }

        rows.sort(Comparator.comparingInt(HeapRow::getAddress));
        heapTableView.setItems(rows);
    }

    private void refreshOut(PrgState prg) {
        if (outList == null) return;

        ObservableList<String> outItems = FXCollections.observableArrayList();
        for (Value v : prg.getOut().getList()) { // adjust if your MyIList API differs
            outItems.add(v.toString());
        }
        outList.setItems(outItems);
    }

    private void refreshFileTable(PrgState prg) {
        if (fileTableList == null) return;

        ObservableList<String> fileItems = FXCollections.observableArrayList();
        fileItems.addAll(prg.getFileTable().getContent().keySet());
        fileTableList.setItems(fileItems);
    }

    private void refreshSelectedPrgStateDetails(int prgId) {
        List<PrgState> prgStates = controller.getRepo().getProgramsList();
        PrgState selected = prgStates.stream()
                .filter(p -> p.getId() == prgId)
                .findFirst()
                .orElse(null);

        if (selected == null) return;

        refreshSymTable(selected);
        refreshExeStack(selected);
    }

    private void refreshSymTable(PrgState prg) {
        if (symTableView == null) return;

        ObservableList<SymRow> rows = FXCollections.observableArrayList();
        for (var e : prg.getSymTable().getContent().entrySet()) {
            rows.add(new SymRow(e.getKey(), e.getValue().toString()));
        }

        symTableView.setItems(rows);
    }

    private void refreshExeStack(PrgState prg) {
        if (exeStackList == null) return;

        ObservableList<String> items = FXCollections.observableArrayList();

        for (IStmt stmt : prg.getStack().toList()) {
            items.add(stmt.toString());
        }

        exeStackList.setItems(items);
    }

    // --------- alerts ---------

    private void showWarning(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
