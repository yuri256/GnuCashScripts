package com.github.yuri256.gnucashscripts.impl.bunq.fileconvertor;

import com.github.yuri256.gnucashscripts.impl.bunq.model.BunqRecord;
import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.MyMt940Record;

import java.util.Objects;
import java.util.function.Function;

public class BunqMyMt940Converter implements Function<BunqRecord, MyMt940Record> {

    private final BunqDescriptionConverter descriptionConverter;

    public BunqMyMt940Converter(BunqDescriptionConverter descriptionConverter) {
        this.descriptionConverter = descriptionConverter;
    }

    @Override
    public MyMt940Record apply(BunqRecord modelRecord) {
        Objects.requireNonNull(modelRecord);

        // simple approach
        String accountNumber = modelRecord.getAccount();
        String statementLine = getStatementLine(modelRecord);
        String description = descriptionConverter.apply(modelRecord);
        return new MyMt940Record(accountNumber, description, statementLine);
    }

    private static String getStatementLine(BunqRecord record) {
        StringBuilder sb = new StringBuilder();
        // Date in format YYMMDD
        sb.append(record.getDate(), 2, 4);
        sb.append(record.getDate(), 5, 7);
        sb.append(record.getDate(), 8, 10);

        // D/C: debit/credit
        boolean isNegative = record.getAmount().charAt(0) == '-';
        sb.append(isNegative ? "D" : "C");

        // Amount
        sb.append(record.getAmount().replace("-", "").replace(",", "").replace(".", ","));

        sb.append("NTRF"); // SWIFT Transaction code. 'Transfer transaction' just to have type
        sb.append("NONREF"); // Bank reference.
        return sb.toString();
    }
}
