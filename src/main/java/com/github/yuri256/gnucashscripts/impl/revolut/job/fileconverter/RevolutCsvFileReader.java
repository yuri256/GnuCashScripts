package com.github.yuri256.gnucashscripts.impl.revolut.job.fileconverter;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class RevolutCsvFileReader {
    public static List<RevolutCsvRecord> readFile(Path inPath) throws FileNotFoundException {
        // https://stackabuse.com/libraries-for-reading-and-writing-csvs-in-java/
        FileReader reader = new FileReader(inPath.toFile());
        List<RevolutCsvRecord> parse = new CsvToBeanBuilder<RevolutCsvRecord>(reader)
                .withSeparator(',')
                .withType(RevolutCsvRecord.class).build().parse();
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parse;
    }

}
