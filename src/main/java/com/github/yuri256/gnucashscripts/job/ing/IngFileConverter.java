package com.github.yuri256.gnucashscripts.job.ing;

import com.github.yuri256.gnucashscripts.job.FileConverter;
import com.github.yuri256.gnucashscripts.job.common.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.job.ing.model.IngConstants;
import com.github.yuri256.gnucashscripts.job.ing.model.IngRecord;
import com.github.yuri256.gnucashscripts.model.SimpleMt940Record;
import com.prowidesoftware.swift.io.RJEWriter;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IngFileConverter implements FileConverter {

    private final Set<String> descriptionRemoveFieldKeys;

    public IngFileConverter(Set<String> descriptionRemoveFieldKeys) {
        this.descriptionRemoveFieldKeys = descriptionRemoveFieldKeys;
    }

    @Override
    public void apply(Path inPath, Path outPath) throws IOException {
        List<IngRecord> ingRecords = CsvFileReader.readFile(inPath);
        SimpleMt940Converter converter = new SimpleMt940Converter(new DescriptionConverter(new DescriptionFilterFunction(descriptionRemoveFieldKeys, IngConstants.DEFAULT_REMOVE_KEY_KEYS)));
        List<SimpleMt940Record> mtRecords = ingRecords.stream().map(converter).collect(Collectors.toList());
        List<MT940> finalRecords = mtRecords.stream().map(SimpleMt940Record::getMT940).collect(Collectors.toList());

        StringWriter stringWriter = new StringWriter();
        RJEWriter writer = new RJEWriter(stringWriter);
        for (MT940 record : finalRecords) {
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
