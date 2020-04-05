package lt.donatas.inventi.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public interface BankStatementsService {

     void importCSVFile(MultipartFile file) throws Exception;

     void exportCSVFile(HttpServletResponse response, Date dateFrom, Date dateTo);

     double calculateAccountBalance(String accountNumber, Date dateFrom, Date dateTo);

}
