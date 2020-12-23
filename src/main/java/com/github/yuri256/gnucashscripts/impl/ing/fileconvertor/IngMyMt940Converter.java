package com.github.yuri256.gnucashscripts.impl.ing.fileconvertor;

import com.github.yuri256.gnucashscripts.impl.ing.model.AfBij;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngRecord;

import java.util.Objects;
import java.util.function.Function;

public class IngMyMt940Converter implements Function<IngRecord, MyMt940Record> {

    private final IngDescriptionConverter descriptionConverter;

    public IngMyMt940Converter(IngDescriptionConverter descriptionConverter) {
        this.descriptionConverter = descriptionConverter;
    }

    @Override
    public MyMt940Record apply(IngRecord ingRecord) {
        Objects.requireNonNull(ingRecord);
        // simple approach
        String accountNumber = ingRecord.getRekening();
        String statementLine = getStatementLine(ingRecord);
        String description = descriptionConverter.apply(ingRecord);
        return new MyMt940Record(accountNumber, description, statementLine);
    }

    private static String getStatementLine(IngRecord ingRecord) {
        StringBuilder sb = new StringBuilder();
        sb.append(ingRecord.getDatum().substring(2)); // Date YYMMDD
        sb.append(ingRecord.getAfBij() == AfBij.Af ? "D" : "C"); // D/C
        sb.append(ingRecord.getBedragEUR()); // Amount
        sb.append("NTRF"); // SWIFT Transaction code. 'Transfer transaction' just to have type
        sb.append("NONREF"); // Bank reference.
        return sb.toString();
    }
}
