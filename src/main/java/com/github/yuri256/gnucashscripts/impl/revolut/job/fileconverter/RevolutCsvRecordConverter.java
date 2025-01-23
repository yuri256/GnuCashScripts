package com.github.yuri256.gnucashscripts.impl.revolut.job.fileconverter;

import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.MyMt940Record;

import java.util.Locale;
import java.util.function.Function;

public class RevolutCsvRecordConverter implements Function<RevolutCsvRecord, MyMt940Record> {
    @Override
    public MyMt940Record apply(RevolutCsvRecord inputRecord) {
        // REVERTED transactions don't have Completed Date and Balance. Just skipping them.
        if (inputRecord.getState().equals("REVERTED")) {
            return null;
        }
        String accountNumber = inputRecord.getProduct() + "_" + inputRecord.getCurrency();
        String time = inputRecord.getStartedDate().substring(11,16);
        String description = time + " " + inputRecord.getType() + " " + inputRecord.getDescription();
        String statementLine = getStatementLine(inputRecord);
        return new MyMt940Record(accountNumber, description, statementLine);
    }

    private static String getStatementLine(RevolutCsvRecord inputRecord) {
        // for now assuming the currencies we deal with all have two digits after comma.
        int MINOR_UNITS_FACTOR = 100;

        int intAmount = (int) (Float.parseFloat(inputRecord.getAmount()) * MINOR_UNITS_FACTOR);
        int intFee = (int) (Float.parseFloat(inputRecord.getFee()) * MINOR_UNITS_FACTOR);
        // Adding fee to amount, as we don't want to account for it separately. At least now.
        if (intFee != 0) {
            intAmount -= intFee;
        }
        String startDate = inputRecord.getStartedDate();
        String date = startDate.substring(2, 4) + startDate.substring(5, 7) + startDate.substring(8, 10);
        // We need to use comma as separator, hence using German locale
        String amount = String.format(Locale.GERMAN, "%.2f", (float)Math.abs(intAmount) / MINOR_UNITS_FACTOR);
        String debitCredit = intAmount > 0 ? "C" : "D";
        String statementLine = date + debitCredit + amount;
        statementLine += "NTRF"; // SWIFT Transaction code. 'Transfer transaction' just to have type
        statementLine += "NONREF"; // Bank reference.
        return statementLine;
    }

}
