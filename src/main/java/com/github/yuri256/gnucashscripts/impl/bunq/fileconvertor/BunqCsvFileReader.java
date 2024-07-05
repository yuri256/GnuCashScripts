package com.github.yuri256.gnucashscripts.impl.bunq.fileconvertor;

import com.github.yuri256.gnucashscripts.impl.bunq.model.BunqRecord;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BunqCsvFileReader {
    public static List<BunqRecord> readFile(Path inPath) throws FileNotFoundException {
        // https://stackabuse.com/libraries-for-reading-and-writing-csvs-in-java/
        FileReader reader = new FileReader(inPath.toFile());
        char separator = getSeparator(inPath);
        List<BunqRecord> parse = new CsvToBeanBuilder<BunqRecord>(reader)
                .withSeparator(separator)
                .withType(BunqRecord.class).build().parse();
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parse;
    }

    private static char getSeparator(Path path) throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line = reader.readLine();
            // no header is weird, but let it fail when parsing
            if (line == null) {
                throw new RuntimeException("File " + path + " is empty");
            }
            return line.contains("\";\"") ? ';' : ',';
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
