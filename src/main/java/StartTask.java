import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartTask extends Task<Void> {

//    public static Logger logger = LoggerFactory.getLogger(StartTask.class);

    final List<File> files;
    final String stepName;
    final Model model;
    final File file;
    final ObservableList<TestDataNames> paramsList;


    public StartTask(List<File> files, String stepName, Model model, File file,
                     ObservableList<TestDataNames> paramsList) {
        this.files = files;
        this.stepName = stepName;
        this.model = model;
        this.file = file;
        this.paramsList = paramsList;
    }

    @Override
    protected Void call() {
        parsePdf();
        return null;
    }

    private void parsePdf() {
        String parsedText;
        PDDocument pdDoc;
        double count = 1;
        for (File filePath : files) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));) {
                updateProgress(count / files.size(), 1);
                RandomAccessFile randomAccessFile = new RandomAccessFile(filePath.getAbsoluteFile(), "r");
                PDFParser parser = new PDFParser(randomAccessFile);
                parser.parse();

                COSDocument cosDoc = parser.getDocument();
                PDFTextStripper pdfTextStripper = new PDFTextStripper();
                pdDoc = new PDDocument(cosDoc);
                parsedText = pdfTextStripper.getText(pdDoc);

                BufferedReader reader = new BufferedReader(new StringReader(parsedText));
                String line;
                String lineStep;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (line.contains(stepName)) {
                        reader.readLine();
                        while (!(lineStep = reader.readLine()).contains("●")) {
                            sb.append(lineStep);
                        }
                        break;
                    }
                }

                ArrayList<String> result = new ArrayList<String>();
                List<String> stringArrayList = Arrays.asList(sb.toString().split(","));

                for (int i = 0; i < paramsList.size(); i++) {
                    for (int j = 0; j < stringArrayList.size(); j++) {
                        if (stringArrayList.get(j).contains(paramsList.get(i).getParameterName())) {
                            result.add(paramsList.get(i).getParameterName() + stringArrayList.get(j).split(paramsList.get(i).getParameterName())[1]);
                        }
                    }
                }
                writer.write(filePath.getName());
                writer.write("\r\n");
                for (int i = 0; i < result.size(); i++) {
                    writer.write(result.get(i));
                    writer.write("\r\n");
                }
                writer.write("\r\n\n");
                writer.flush();
                reader.close();
                pdDoc.close();
                count++;
                if (isCancelled()) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            if (isCancelled()) {
                updateMessage("Работа прервана");
                return;
            }
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException e) {
//            logger.error("Невозможно открыть файл " + file);
            System.out.println("Невозможно открыть файл " + file);
            e.printStackTrace();
        }
        updateMessage("Работа завершена");
    }
}
