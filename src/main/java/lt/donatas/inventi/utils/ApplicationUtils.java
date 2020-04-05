package lt.donatas.inventi.utils;

import lt.donatas.inventi.model.CsvModel;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ApplicationUtils {

    private static List<CsvModel> inMemoryDB = new ArrayList<>();
    private static boolean isInitialized = false;

    private static void initDB() {
        Date date = new Date();
        date.setTime(Calendar.getInstance().getTimeInMillis());
        Date date1 = new Date();
        date1.setTime(2590000000L);
        inMemoryDB.add(new CsvModel("asd", date, "aaa", 50.0, "cas", "asd"));
        inMemoryDB.add(new CsvModel("www", date, "bbb", -50.0, "awaw", "a"));
        inMemoryDB.add(new CsvModel("www", date1, "bbb", 50.0, "awaw", "a"));
        inMemoryDB.add(new CsvModel("TESTING", date1, "bbb", 50.0, "awaw", "a"));
        isInitialized = true;
    }

    public static List<CsvModel> getCsvModelList() {
        if (!isInitialized) {
            initDB();
        }
        return getInMemoryDB();
    }

    private static List<CsvModel> getInMemoryDB() {
        return inMemoryDB;
    }

    public static void validateFile(MultipartFile file) throws Exception {
        if (file == null) {
            throw new NullPointerException("File doesn't exist, please check the file");
        }
        if (file.isEmpty()) {
            throw new FileUploadException("File is empty, please check file content");
        }
        if (!file.getOriginalFilename().endsWith(".csv")){
            throw new FileUploadException("File is not of type CSV, please check the files extension");
        }
    }
}
