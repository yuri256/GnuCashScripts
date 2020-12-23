package com.github.yuri256.gnucashscripts.model;

import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;

/**
 * Minimal Mt940 record
 */
public class MyMt940Record {
// https://web.archive.org/web/20160725042101/http://www.societegenerale.rs/fileadmin/template/main/pdf/SGS%20MT940.pdf
// https://www.paiementor.com/swift-mt940-customer-statement-detailed-analysis/


//    Field :20: Transaction Reference Number
//    Field :25: Account Number
    String accountNumber;
//    Field :28C: Statement Number/Sequence Number
//    Field :60F: Opening Balance
//    Field :61: Statement Line
    String statementLine;
//    Field :86: Description
    String description;

//    Field :62F: Closing Balance
//    Field :64: Closing Available Balance
//    Field :65: Forward Value Balance

    public MyMt940Record(String accountNumber, String description, String statementLine) {
        this.accountNumber = accountNumber;
        this.description = description;
        this.statementLine = statementLine;
    }

    @Override
    public String toString() {
        return "Mt940Record{" +
                "accountNumber='" + accountNumber + '\'' +
                ", statementLine='" + statementLine + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public MT940 getMT940() {
        final MT940 m = new MT940();
//        m.append(new SwiftTagListBlock());
//        m.append(new Tag("abc"));
//        m.setSender("Sender");
//        m.setReceiver("Receiver");
//        m.addField(new Field20("trRefNum"));
        m.addField(new Field25(accountNumber));
//        m.addField(new Field28("statementNumSeqNum"));
//        m.addField(new Field60F("openingBalance"));
        m.addField(new Field61(statementLine));
        m.addField(new Field86(description));
        return m;
    }
}
