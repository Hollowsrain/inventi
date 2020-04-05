package lt.donatas.inventi;

import lt.donatas.inventi.model.CsvModel;
import lt.donatas.inventi.service.BankStatementsLogic;
import lt.donatas.inventi.utils.ApplicationUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

public class BankStatementsLogicTest {

    BankStatementsLogic bankStatementsLogic = new BankStatementsLogic();

    @Test
    public void canImportCsvFileTest() throws Exception {
        int initialSize = ApplicationUtils.getCsvModelList().size();
        MockMultipartFile file = new MockMultipartFile("file", "bankStatement.csv", "text/csv", "www,2020-04-05,bbb,-50.0,awaw,a".getBytes());
        bankStatementsLogic.importCSVFile(file);
        Assertions.assertTrue(initialSize < ApplicationUtils.getCsvModelList().size());
    }

    @Test
    public void canExportCsvFileTest() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        bankStatementsLogic.exportCSVFile(response, new Date(), new Date());
       boolean containsContentDisposition = Objects.requireNonNull(response.getHeader(HttpHeaders.CONTENT_DISPOSITION)).getBytes().length > 0;
       boolean containsContentType = Objects.requireNonNull(response.getContentType()).contains("text/csv");
        Assertions.assertTrue(containsContentDisposition && containsContentType);
        }


    @Test()
    public void nullFileImportTest() {
        Exception exception = Assertions.assertThrows(NullPointerException.class, () ->{
            ApplicationUtils.validateFile(null);
        });
        String expectedMessage = "File doesn't exist, please check the file";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void emptyFileImportTest(){
        MockMultipartFile file = new MockMultipartFile("file", "bankStatement.csv", "text/csv", "".getBytes());
        Exception exception = Assertions.assertThrows(FileUploadException.class, () ->{
            ApplicationUtils.validateFile(file);
        });
        String expectedMessage = "File is empty, please check file content";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void isInMemoryDbNotEmptyTest(){
        Assertions.assertNotNull(ApplicationUtils.getCsvModelList());
        Assertions.assertTrue(ApplicationUtils.getCsvModelList().size() > 0);
    }

    @Test
    public void calculateAccountBalanceTest() {
        Date date = new Date();
        date.setTime(2590000000L);
        Date dateFrom = new Date();
        Date dateTo = new Date();
        dateTo.setTime(1590000000L);
        dateFrom.setTime(3590000000L);
        CsvModel test = new CsvModel("TESTING", date, "bbb", 50.0, "awaw", "a");
        double size = bankStatementsLogic.calculateAccountBalance("TESTING", dateFrom, dateTo);
        Assertions.assertEquals(test.getAmount(), size, 0.0);
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
}
