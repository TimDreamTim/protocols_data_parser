import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CollectionParamsNames {

    private final ObservableList<TestDataNames> testDataNames = FXCollections.observableArrayList();

    public ObservableList<TestDataNames> getTestDataNames() {
        return testDataNames;
    }

    public void fillTestData() {
            testDataNames.add(new TestDataNames("cardTrack2"));
            testDataNames.add(new TestDataNames("accountNumber"));
            testDataNames.add(new TestDataNames("tranAmount"));
            testDataNames.add(new TestDataNames("tranCode"));
            testDataNames.add(new TestDataNames("tranApprovCode"));
    }
}
