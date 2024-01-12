package com.github.yuri256.gnucashscripts.impl.ing.fileconvertor;

import com.github.yuri256.gnucashscripts.impl.ing.model.DebitCredit;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

class IngCsvFileReaderTest {

    @Test
    public void testRead() throws URISyntaxException, FileNotFoundException {
        List<IngRecord> ingRecords = IngCsvFileReader.readFile(Paths.get(getClass().getResource("ingMultilineFile.csv").toURI()));
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, ingRecords.size()),

                // record1
                () -> Assertions.assertEquals("20200307", ingRecords.get(0).getDate()),
                () -> Assertions.assertEquals("JUMBO Bla NLD", ingRecords.get(0).getNameDescription()),
                () -> Assertions.assertEquals("NL03INGB0123456789", ingRecords.get(0).getAccount()),
                () -> Assertions.assertNull(ingRecords.get(0).getCounterparty()), // not empty string!
                () -> Assertions.assertEquals("BA", ingRecords.get(0).getCode()),
                () -> Assertions.assertEquals(DebitCredit.Debit, ingRecords.get(0).getDebitCredit()),
                () -> Assertions.assertEquals("12,34", ingRecords.get(0).getAmountEUR()),
                () -> Assertions.assertEquals("Payment terminal", ingRecords.get(0).getTransactionType()),
                () -> Assertions.assertEquals("Card sequence no.: 001 07/03/2020 09:34 Transaction: A111B2 Term: AB1234 Value date: 07-03-2020", ingRecords.get(0).getNotifications()),
                () -> Assertions.assertEquals("23,45", ingRecords.get(0).getResultingBalance()),
                () -> Assertions.assertEquals("tag1", ingRecords.get(0).getTag()),

                // record 2
                () -> Assertions.assertEquals("20200101", ingRecords.get(1).getDate()),
                () -> Assertions.assertEquals("22,22", ingRecords.get(1).getAmountEUR()),
                () -> Assertions.assertEquals("Card sequence no.: 001 31/12/2019 19:10 Transaction: A222B3 Term: C5D678 Value date: 01-01-2020", ingRecords.get(1).getNotifications()),
                () -> Assertions.assertEquals("33,33", ingRecords.get(1).getResultingBalance()),
                () -> Assertions.assertEquals("tag2", ingRecords.get(1).getTag())
        );
    }
}