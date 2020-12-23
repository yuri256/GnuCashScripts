package com.github.yuri256.gnucashscripts.job.ing;

import com.github.yuri256.gnucashscripts.job.ing.model.IngRecord;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class IngCsvFileReader {
    public static List<IngRecord> readFile(Path inPath) throws FileNotFoundException {
        // https://stackabuse.com/libraries-for-reading-and-writing-csvs-in-java/
        FileReader reader = new FileReader(inPath.toFile());
        List<IngRecord> parse = new CsvToBeanBuilder<IngRecord>(reader)
                .withSeparator(';')
                .withType(IngRecord.class).build().parse();
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parse;
    }
}
