package com.github.yuri256.gnucashscripts.job.ing;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.job.FileJob;
import com.github.yuri256.gnucashscripts.job.common.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.job.ing.model.IngConstants;
import com.github.yuri256.gnucashscripts.job.ing.model.IngRecord;
import com.github.yuri256.gnucashscripts.model.SimpleMt940Record;
import com.prowidesoftware.swift.io.RJEWriter;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IngJob extends FileJob {

    private final Set<String> descriptionRemoveFieldKeys;

    public IngJob(Config config) {
        this(config.get(Property.BASE_DIR), config.get(Property.ING_JOB_DIR_NAME), config.get(Property.GNU_CASH_DIR_NAME), Arrays.stream(config.get(Property.ING_DESCRIPTION_SKIP_FIELDS).trim().split(",")).collect(Collectors.toSet()));
    }

    public IngJob(String baseDir, String jobDirName, String nextJobDirName, Set<String> descriptionRemoveFieldKeys) {
        super(baseDir, jobDirName, nextJobDirName);
        this.descriptionRemoveFieldKeys = descriptionRemoveFieldKeys;

    }

    public static void main(String[] args) {
        new IngJob(Config.load()).run();
    }

    @Override
    public void processFile(Path inPath, Path outPath) throws IOException {
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
