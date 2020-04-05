package lt.donatas.inventi.model;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.util.Date;


public class CsvModel {
    @CsvBindByPosition(position = 0, required = true)
    private String accountNumber;
    @CsvBindByPosition(position = 1, required = true)
    @CsvDate(value = "yyyy-MM-dd")
    private Date operationDate;
    @CsvBindByPosition(position = 2, required = true)
    private String beneficiary;
    @CsvBindByPosition(position = 3, required = true)
    private Double amount;
    @CsvBindByPosition(position = 4, required = true)
    private String currency;
    @CsvBindByPosition(position = 5)
    private String comment;

    public CsvModel(String accountNumber, Date operationDate, String beneficiary, Double amount, String currency, String comment) {
        this.accountNumber = accountNumber;
        this.operationDate = operationDate;
        this.beneficiary = beneficiary;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
    }

    public CsvModel() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
