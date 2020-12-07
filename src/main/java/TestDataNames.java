import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestDataNames {
    private final StringProperty parameterName;

    public TestDataNames() {
        this(null);
    }

    public TestDataNames(String parameterName) {
        this.parameterName = new SimpleStringProperty(parameterName);
    }

    public String getParameterName() {
        return parameterName.get();
    }

    public StringProperty parameterNameProperty() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName.set(parameterName);
    }
}
