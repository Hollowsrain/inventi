package lt.donatas.inventi.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lt.donatas.inventi.model.CsvModel;
import lt.donatas.inventi.utils.ApplicationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BankStatementsLogic implements BankStatementsService {

    @Override
    public void importCSVFile(MultipartFile file) throws Exception {
        ApplicationUtils.validateFile(file);
        List<CsvModel> csvList = mapCsvFile(file);
        for (CsvModel csvStatement : Objects.requireNonNull(csvList)) {
            ApplicationUtils.getCsvModelList().add(csvStatement);
        }
    }

    @Override
    public void exportCSVFile(HttpServletResponse response, Date dateFrom, Date dateTo) {
        String filename = "bankStatement.csv";
        handleEmptyDateParams(dateFrom, dateTo);
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        List<CsvModel> csvListSortedByDate = getSortedCsvList(dateFrom, dateTo);
        writeSortedList(response, csvListSortedByDate);
    }

    @Override
    public double calculateAccountBalance(String accountNumber, Date dateFrom, Date dateTo) {
        handleEmptyDateParams(dateFrom, dateTo);
       return ApplicationUtils.getCsvModelList().stream()
               .filter(csvModel -> csvModel.getAccountNumber().equals(accountNumber))
               .mapToDouble(csvModel -> csvModel.getAmount()).sum();
    }

    private void writeSortedList(HttpServletResponse response, List<CsvModel> csvListSortedByDate) {
        StatefulBeanToCsv<CsvModel> writer = null;
        try {
            writer = new StatefulBeanToCsvBuilder<CsvModel>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(false)
                    .build();
        } catch (IOException e) {
            System.out.println("Failed To build csv writer: " + e);
        }
        try {
            writer.write(csvListSortedByDate);
        } catch (CsvDataTypeMismatchException e) {
            System.out.println("Incompatible data type: " + e);
        } catch (CsvRequiredFieldEmptyException e) {
            System.out.println("Field marked as required, but empty: " + e);
        }
    }

    private List<CsvModel> getSortedCsvList(Date dateFrom, Date dateTo) {
        return ApplicationUtils.getCsvModelList()
                .stream()
                .filter(csvModel -> csvModel.getOperationDate().after(dateFrom))
                .filter(csvModel -> csvModel.getOperationDate().before(dateTo))
                .collect(Collectors.toList());
    }

    private List<CsvModel> mapCsvFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<CsvModel> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(CsvModel.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                return csvToBean.parse();
            } catch (IOException e) {
                System.out.println("Cannot read uploaded file: " + e);
            }
        }
        return null;
    }

    private static boolean isDateProvided(Date date) {
        return date != null;
    }

    private static boolean isDateRangeProvided(Date dateFrom, Date dateTo) {
        return isDateProvided(dateFrom) && isDateProvided(dateTo);
    }

    private static void initializeDateRange(Date dateFrom, Date dateTo) {
        int oneDayInMili = 86400000;
        if (isDateProvided(dateFrom)) {
            dateFrom.setTime(Calendar.getInstance().getTimeInMillis());
        }
        if (isDateProvided(dateTo)) {
            dateTo.setTime(Calendar.getInstance().getTimeInMillis() + oneDayInMili);
        }
    }

    public static void handleEmptyDateParams(Date dateFrom, Date dateTo) {
        if (!isDateRangeProvided(dateFrom, dateTo)) {
            initializeDateRange(dateFrom, dateTo);
        }
    }

}