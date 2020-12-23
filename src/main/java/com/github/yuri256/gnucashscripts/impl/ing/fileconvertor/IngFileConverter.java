package com.github.yuri256.gnucashscripts.impl.ing.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.FileConverter;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngRecord;
import com.prowidesoftware.swift.io.RJEWriter;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class IngFileConverter implements FileConverter {

    private final IngMyMt940Converter myMt940Converter;

    public IngFileConverter(IngMyMt940Converter myMt940Converter) {
        this.myMt940Converter = myMt940Converter;
    }

    @Override
    public void apply(Path inPath, Path outPath) throws IOException {
        List<IngRecord> ingRecords = IngCsvFileReader.readFile(inPath);
        List<MyMt940Record> myMt940Records = ingRecords.stream().map(myMt940Converter).collect(Collectors.toList());
        List<MT940> mt940Records = myMt940Records.stream().map(MyMt940Record::getMT940).collect(Collectors.toList());

        StringWriter stringWriter = new StringWriter();
        RJEWriter writer = new RJEWriter(stringWriter);
        for (MT940 record : mt940Records) {
            writer.write(record);
        }
        writer.close();

        // remove { and } lines as gnuCash doesn't like them
        List<String> filtered = stringWriter.toString().lines().filter(l -> !l.startsWith("{") && !l.endsWith("}")).collect(Collectors.toList());

        BufferedWriter bw = new BufferedWriter(new FileWriter(outPath.toFile()));
        filtered.forEach(l -> {
            try {
                bw.write(l);
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        bw.close();
    }
}
