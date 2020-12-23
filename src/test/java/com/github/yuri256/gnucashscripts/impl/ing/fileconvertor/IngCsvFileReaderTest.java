package com.github.yuri256.gnucashscripts.impl.ing.fileconvertor;

import com.github.yuri256.gnucashscripts.impl.ing.model.AfBij;
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
                () -> Assertions.assertEquals("20200307", ingRecords.get(0).getDatum()),
                () -> Assertions.assertEquals("JUMBO Bla NLD", ingRecords.get(0).getNaamOmschrijving()),
                () -> Assertions.assertEquals("NL03INGB0123456789", ingRecords.get(0).getRekening()),
                () -> Assertions.assertNull(ingRecords.get(0).getTegenRekening()), // not empty string!
                () -> Assertions.assertEquals("BA", ingRecords.get(0).getCode()),
                () -> Assertions.assertEquals(AfBij.Af, ingRecords.get(0).getAfBij()),
                () -> Assertions.assertEquals("12,34", ingRecords.get(0).getBedragEUR()),
                () -> Assertions.assertEquals("Betaalautomaat", ingRecords.get(0).getMutatieSoort()),
                () -> Assertions.assertEquals("Pasvolgnr: 001 07-03-2020 09:34 Transactie: A111B2 Term: AB1234 Valutadatum: 07-03-2020", ingRecords.get(0).getMededelingen()),

                // record 2
                () -> Assertions.assertEquals("20200101", ingRecords.get(1).getDatum()),
                () -> Assertions.assertEquals("22,22", ingRecords.get(1).getBedragEUR()),
                () -> Assertions.assertEquals("Pasvolgnr: 001 31-12-2019 19:10 Transactie: A222B3 Term: C5D678 Valutadatum: 01-01-2020", ingRecords.get(1).getMededelingen())
        );
    }
}