package com.github.yuri256.gnucashscripts.impl.abn.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

class AbnFileConverterTest {

    @ParameterizedTest
    @CsvSource({"abnExample.mta,abnExampleExpected.mta"
            , "abnMultilineExample.mta,abnMultilineExampleExpected.mta"
            , "abnSpaces.mta,abnSpacesExpected.mta"
            , "abnTerminalPayment.mta,abnTerminalPaymentExpected.mta"
            , "abnTerminalPaymentComma.mta,abnTerminalPaymentCommaExpected.mta"
    })
    void processExampleFile(String inputFileName, String expectedFileName) throws URISyntaxException, IOException {
        // GIVEN
        Path inPath = Paths.get(getClass().getResource(inputFileName).toURI());
        Path outPath = Files.createTempFile("gnucash-abn-test-", ".mta");
        Path expectedPath = Paths.get(getClass().getResource(expectedFileName).toURI());

        // WHEN
        DescriptionFilterFunction filterFunction = new DescriptionFilterFunction(Set.of("BIC"), Set.of());
        new AbnFileConverter(filterFunction).apply(inPath, outPath);

        // THEN
        Assertions.assertEquals(Files.readString(expectedPath), Files.readString(outPath));
    }

}