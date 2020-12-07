import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private ProgressBar startProgressBar;

    @FXML
    private TextField stepNameField;

    @FXML
    private TextArea fileNameInput;

    @FXML
    public Button fileNameChooseButton;

    @FXML
    TableView<TestDataNames> tableParamsNames;

    @FXML
    TableColumn<TestDataNames, String> columnParamsNames;

//    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final Model model = new Model();

    private StartTask startTask;

    private final CollectionParamsNames collectionParamsNames = new CollectionParamsNames();

    @FXML
    private void initialize() {
        columnParamsNames.setCellValueFactory(new PropertyValueFactory<TestDataNames, String>("parameterName"));
        columnParamsNames.setCellFactory(TextFieldTableCell.forTableColumn());
        collectionParamsNames.fillTestData();
        tableParamsNames.setItems(collectionParamsNames.getTestDataNames());
    }

    public File getFileNameToSave() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        if (model.getLastDirectoryToSave() != null) {
            fileChooser.setInitialDirectory(new File(model.getLastDirectoryToSave()));
        }
        File file = fileChooser.showSaveDialog(App.getPrimaryStage());
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
//                    logger.info("Файл успешно создан " + file);
                    System.out.println("Файл успешно создан " + file);
                }
            } catch (IOException e) {
//                logger.error("Файл не создан.Неверно указан путь до файла " + file);
                System.out.println("Файл не создан.Неверно указан путь до файла " + file);
                e.printStackTrace();
            }
        } else {
            if (file.delete()) {
                try {
                    if (file.createNewFile()) {
//                        logger.info("Файл очищен " + file);
                        System.out.println("Файл очищен " + file);
                    }
                } catch (IOException e) {
//                    logger.warn("Не удалось очистить файл " + file);
                    System.out.println("Не удалось очистить файл " + file);
                    e.printStackTrace();
                }
            }
        }
        model.setLastDirectoryToSave(file.getParent());
        return file;
    }

    public void filesChoose(ActionEvent actionEvent) {
        fileNameInput.clear();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите протоколы");
        if (model.getLastDirectorySelect() != null) {
            fileChooser.setInitialDirectory(new File(model.getLastDirectorySelect()));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF files", "*.pdf")
        );
        model.setFiles(fileChooser.showOpenMultipleDialog(App.getPrimaryStage()));
        if (model.getFiles() == null || model.getFiles().isEmpty()) {
            return;
        }
        for (File file : model.getFiles()) {
            fileNameInput.appendText(file.getName() + "\n");
        }
        model.setLastDirectorySelect(model.getFiles().get(model.getFiles().size() - 1).getParent());
    }

    public void parseStart(ActionEvent actionEvent) {

        if (fileNameInput.getText().isEmpty() || stepNameField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(App.getPrimaryStage());
            alert.setTitle("Warning!");

            if (fileNameInput.getText().isEmpty()) {
                alert.setHeaderText("Протоколы не выбраны");
                alert.setContentText("Пожалуйста, выберите протоколы нажав кнопку \"Выбрать протоколы\"");
                alert.showAndWait();
                return;
            }
            if (stepNameField.getText().isEmpty()) {
                alert.setHeaderText("Не выбрано название шага");
                alert.setContentText("Пожалуйста, укажите название шага, заполнив поле \"Название шага с данными\"");
                alert.showAndWait();
                return;
            }
        }

        File fileNameToSave = getFileNameToSave();

        if (fileNameInput == null || fileNameToSave == null) {
            return;
        }

        startTask = new StartTask(model.getFiles(), stepNameField.getText(), model, fileNameToSave,
                collectionParamsNames.getTestDataNames());

        Thread threadStart = new Thread(startTask);
        threadStart.setDaemon(true);
        threadStart.start();

        startProgressBar.progressProperty().bind(startTask.progressProperty());

        startButton.disableProperty().bind(startTask.runningProperty());
        stopButton.disableProperty().bind(startTask.runningProperty().not());
    }

    public void parseStop(ActionEvent actionEvent) {
        if (startTask != null) {
            startTask.cancel();
        }
    }
}
