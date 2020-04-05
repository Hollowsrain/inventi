package lt.donatas.inventi.api;

import lt.donatas.inventi.service.BankStatementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class BankStatementsController {

    @Autowired
    public BankStatementsService bankStatementsService;

    @PostMapping("/import-bank-statements")
    public void importCSVFile(@RequestParam("file") MultipartFile file) throws Exception {
        bankStatementsService.importCSVFile(file);
    }

    @GetMapping("/export-bank-statements")
    public void exportCSVFile(HttpServletResponse response,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo) {
        bankStatementsService.exportCSVFile(response, dateFrom, dateTo);
    }

    @GetMapping("/calculate-balance")
    public double calculateAccountBalance(@RequestParam String accountNumber,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo) {
        return bankStatementsService.calculateAccountBalance(accountNumber, dateFrom, dateTo);
    }
}
