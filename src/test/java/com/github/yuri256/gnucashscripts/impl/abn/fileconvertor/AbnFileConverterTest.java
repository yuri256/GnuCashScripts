package com.github.yuri256.gnucashscripts.impl.abn.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

class AbnFileConverterTest {

    @Test
    void processFile() throws URISyntaxException, IOException {
        // GIVEN
        Path inPath = Paths.get(getClass().getResource("abnExample.mta").toURI());
        Path outPath = Files.createTempFile("gnucash-abn-test-", ".mta");
        Path expectedPath = Paths.get(getClass().getResource("abnExampleExpected.mta").toURI());

        // WHEN
        new AbnFileConverter(new DescriptionFilterFunction(Set.of("BIC"), Set.of())).apply(inPath, outPath);

        // THEN
        Assertions.assertEquals(Files.readString(expectedPath), Files.readString(outPath));
    }

    @Test
    void processMultilineFile() throws URISyntaxException, IOException {
        // GIVEN
        Path inPath = Paths.get(getClass().getResource("abnMultilineExample.mta").toURI());
        Path outPath = Files.createTempFile("gnucash-abn-test-", ".mta");
        Path expectedPath = Paths.get(getClass().getResource("abnMultilineExampleExpected.mta").toURI());

        // WHEN
        new AbnFileConverter(new DescriptionFilterFunction(Set.of("BIC"), Set.of())).apply(inPath, outPath);

        // THEN
        Assertions.assertEquals(Files.readString(expectedPath), Files.readString(outPath));
    }

}