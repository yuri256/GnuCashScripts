package com.github.yuri256.gnucashscripts.impl.ing.model;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class IngRecord {
    @CsvBindByName(column = "Date", required = true)
    private String date;

    @CsvBindByName(column = "Name / Description", required = true)
    private String nameDescription;

    @CsvBindByName(column = "Account", required = true)
    private String account;

    @CsvBindByName(column = "Counterparty")
    private String counterparty;

    @CsvBindByName(column = "Code", required = true)
    private String code; // GT, BA, IC

    @CsvCustomBindByName(converter = DebitCreditBeanField.class,column = "Debit/credit", required = true)
    private DebitCredit debitCredit;

    @CsvBindByName(column = "Amount (EUR)", required = true)
    private String amountEUR;

    @CsvBindByName(column = "Transaction type", required = true)
    private String transactionType; // Online Banking, Payment terminal, SEPA direct debit, Various, iDEAL

    @CsvBindByName(column = "Notifications", required = true)
    private String notifications; // statement, notice

    @CsvBindByName(column = "Resulting balance", required = true)
    private String resultingBalance;

    @CsvBindByName(column = "Tag", required = true)
    private String tag;

    public IngRecord() {
        // deserializer
    }

    public IngRecord(String date, String nameDescription, String account, String counterparty, String code, DebitCredit debitCredit, String amountEUR, String transactionType, String notifications) {
        this.date = date;
        this.nameDescription = nameDescription;
        this.account = account;
        this.counterparty = counterparty;
        this.code = code;
        this.debitCredit = debitCredit;
        this.amountEUR = amountEUR;
        this.transactionType = transactionType;
        this.notifications = notifications;
    }

    public String getDate() {
        return date;
    }

    public String getNameDescription() {
        return nameDescription;
    }

    public String getAccount() {
        return account;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public String getCode() {
        return code;
    }

    public DebitCredit getDebitCredit() {
        return debitCredit;
    }

    public String getAmountEUR() {
        return amountEUR;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getNotifications() {
        return notifications;
    }

    public String getResultingBalance() {
        return resultingBalance;
    }

    public String getTag() {
        return tag;
    }

    public static class DebitCreditBeanField extends AbstractBeanField<DebitCredit> {

        @Override
        protected DebitCredit convert(String value) throws CsvDataTypeMismatchException {
            if (value == null) {
                return null;
            }
            try {
                return DebitCredit.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new CsvDataTypeMismatchException(value, DebitCredit.class,"Could not convert value '" + value + "' to enum");
            }
        }

    }
    @Override
    public String toString() {
        return "Record{" +
                "date='" + date + '\'' +
                ", nameDescription='" + nameDescription + '\'' +
                ", account='" + account + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", code='" + code + '\'' +
                ", debitCredit='" + debitCredit + '\'' +
                ", amountEUR='" + amountEUR + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", notifications='" + notifications + '\'' +
                ", resultingBalance='" + resultingBalance + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
