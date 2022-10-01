package clientUtils.dictionaryComponent;

import application.MainApplicationController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class DictionaryComponentController {

    @FXML public VBox rootVBox;
    @FXML private TableView<String> wordsTableView;

    @FXML private TableColumn<String, String> wordColumn;

    @FXML private TextField filterField;

    private final ObservableList<String> dataList = FXCollections.observableArrayList();

    private MainApplicationController mainApplicationController;
    private String myStyleSheet = "dictionary";

    @FXML
    public void initialize(){
        wordColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));

        FilteredList<String> filteredData = new FilteredList<>(dataList, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(word -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String upperCaseFilter = newValue.toUpperCase();

                return word.toUpperCase().contains(upperCaseFilter);
            });
        });

        SortedList<String> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(wordsTableView.comparatorProperty());
        wordsTableView.setItems(sortedData);

        wordsTableView.setRowFactory( tv -> {
            TableRow<String> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    String rowData = row.getItem();
                    //System.out.println(rowData);
                    mainApplicationController.injectWordToProcess(rowData);
                }
            });
            return row;
        });
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void fillDictionaryTable(List<String> words){
        dataList.clear();
        dataList.addAll(words);
    }

    public void changeTheme(Object cssPrefix) {
        rootVBox.getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            rootVBox.getStylesheets()
                    .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
        }
    }
}

