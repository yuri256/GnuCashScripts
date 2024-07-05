package com.github.yuri256.gnucashscripts.impl.bunq.model;

import com.opencsv.bean.CsvBindByName;

// "Date","Interest Date","Amount","Account","Counterparty","Name","Description"
public class BunqRecord {
    @CsvBindByName(column = "Date", required = true)
    private String date;

    @CsvBindByName(column = "Interest Date", required = true)
    private String interestDate;

    @CsvBindByName(column = "Amount", required = true)
    private String amount;

    @CsvBindByName(column = "Account", required = true)
    private String account;

    @CsvBindByName(column = "Counterparty")
    private String counterparty;

    @CsvBindByName(column = "Name", required = true)
    private String name;

    @CsvBindByName(column = "Description")
    private String description;

    public BunqRecord() {
        // deserializer
    }

    public BunqRecord(String date, String interestDate, String amount, String account, String counterparty, String name, String description) {
        this.date = date;
        this.interestDate = interestDate;
        this.amount = amount;
        this.account = account;
        this.counterparty = counterparty;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "BunqRecord{" +
                "date='" + date + '\'' +
                ", interestDate='" + interestDate + '\'' +
                ", amount='" + amount + '\'' +
                ", account='" + account + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(String interestDate) {
        this.interestDate = interestDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
