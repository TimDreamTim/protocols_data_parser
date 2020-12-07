import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class App extends Application {

    private static Stage pStage;

    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        App.pStage = pStage;
    }

    private void testDataParamsNames() {
        CollectionParamsNames paramsNames = new CollectionParamsNames();
        TestDataNames testDataNames = new TestDataNames();
        testDataNames.setParameterName("test");

        paramsNames.getTestDataNames().add(testDataNames);
    }

    public static void main(String[] args) {
        Application.launch();
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/mainScene.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setTitle("ProtocolsDataParser");
        InputStream iconStream = getClass().getResourceAsStream("/parse-pdf-files.jpg");
        primaryStage.getIcons().add(new Image(iconStream));
        primaryStage.show();
    }
}
