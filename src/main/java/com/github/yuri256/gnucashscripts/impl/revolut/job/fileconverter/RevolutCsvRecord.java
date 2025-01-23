package com.github.yuri256.gnucashscripts.impl.revolut.job.fileconverter;

import com.opencsv.bean.CsvBindByName;

// Type,	Product,	Started Date,	Completed Date,	Description,	Amount,	Fee,	Currency,	State,	Balance
public class RevolutCsvRecord {
    @CsvBindByName(column = "Type", required = true)
    private String type;

    @CsvBindByName(column = "Product", required = true)
    private String product;

    @CsvBindByName(column = "Started Date", required = true)
    private String startedDate;

    // Can be empty for reverted transactions
    @CsvBindByName(column = "Completed Date")
    private String completedDate;

    @CsvBindByName(column = "Description", required = true)
    private String description;

    @CsvBindByName(column = "Amount", required = true)
    private String amount;


    @CsvBindByName(column = "Fee", required = true)
    private String fee;
    @CsvBindByName(column = "Currency", required = true)
    private String currency;
    @CsvBindByName(column = "State", required = true)
    private String state;

    // Can be empty for reverted transactions
    @CsvBindByName(column = "Balance")
    private String balance;

    public RevolutCsvRecord() {
        // deserializer
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


}
