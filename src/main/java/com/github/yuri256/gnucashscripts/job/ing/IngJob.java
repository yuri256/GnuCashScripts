package com.github.yuri256.gnucashscripts.job.ing;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.job.FileConverter;
import com.github.yuri256.gnucashscripts.job.FileJob;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class IngJob extends FileJob {

    private final FileConverter fileConverter;

    public IngJob(Config config) {
        this(config.get(Property.BASE_DIR), config.get(Property.ING_JOB_DIR_NAME), config.get(Property.GNU_CASH_DIR_NAME), Arrays.stream(config.get(Property.ING_DESCRIPTION_SKIP_FIELDS).trim().split(",")).collect(Collectors.toSet()));
    }

    public IngJob(String baseDir, String jobDirName, String nextJobDirName, Set<String> descriptionRemoveFieldKeys) {
        super(baseDir, jobDirName, nextJobDirName);
        fileConverter = new IngFileConverter(descriptionRemoveFieldKeys);
    }

    public static void main(String[] args) {
        new IngJob(Config.load()).run();
    }

    @Override
    public void processFile(Path inPath, Path outPath) throws IOException {
        fileConverter.apply(inPath, outPath);
    }

}
