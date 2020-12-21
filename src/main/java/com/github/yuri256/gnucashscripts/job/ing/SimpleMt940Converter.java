package com.github.yuri256.gnucashscripts.job.ing;

import com.github.yuri256.gnucashscripts.job.ing.model.AfBij;
import com.github.yuri256.gnucashscripts.job.ing.model.IngRecord;
import com.github.yuri256.gnucashscripts.model.SimpleMt940Record;

import java.util.Objects;
import java.util.function.Function;

public class SimpleMt940Converter implements Function<IngRecord, SimpleMt940Record> {

    private final DescriptionConverter descriptionConverter;

    public SimpleMt940Converter(DescriptionConverter descriptionConverter) {
        this.descriptionConverter = descriptionConverter;
    }

    @Override
    public SimpleMt940Record apply(IngRecord ingRecord) {
        Objects.requireNonNull(ingRecord);
        // simple approach
        String accountNumber = ingRecord.getRekening();
        String statementLine = getStatementLine(ingRecord);
        String description = descriptionConverter.apply(ingRecord);
        return new SimpleMt940Record(accountNumber, description, statementLine);
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
