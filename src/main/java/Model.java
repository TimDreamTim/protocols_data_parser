import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class  Model {

    private List<File> files = new ArrayList<File>();

    String lastDirectorySelect;
    String lastDirectoryToSave;

    public String getLastDirectorySelect() {
        return lastDirectorySelect;
    }

    public void setLastDirectorySelect(String lastDirectorySelect) {
        this.lastDirectorySelect = lastDirectorySelect;
    }

    public String getLastDirectoryToSave() {
        return lastDirectoryToSave;
    }

    public void setLastDirectoryToSave(String lastDirectoryToSave) {
        this.lastDirectoryToSave = lastDirectoryToSave;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
