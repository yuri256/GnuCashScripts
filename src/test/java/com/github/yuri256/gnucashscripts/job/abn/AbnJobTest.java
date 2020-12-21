package com.github.yuri256.gnucashscripts.job.abn;

import com.github.yuri256.gnucashscripts.job.common.DescriptionFilterFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

class AbnJobTest {

    @Test
    void processFile() throws URISyntaxException, IOException {
        // GIVEN
        Path inPath = Paths.get(getClass().getResource("abnExample.mta").toURI());
        Path outPath = Files.createTempFile("gnucash-abn-test-", ".mta");
        Path expectedPath = Paths.get(getClass().getResource("abnExampleExpected.mta").toURI());

        // WHEN
        new AbnJob(null, null, null, new DescriptionFilterFunction(Set.of("BIC"), Set.of())).processFile(inPath, outPath);

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
        new AbnJob(null, null, null, new DescriptionFilterFunction(Set.of("BIC"), Set.of())).processFile(inPath, outPath);

        // THEN
        Assertions.assertEquals(Files.readString(expectedPath), Files.readString(outPath));
    }

}