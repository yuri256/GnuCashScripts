package com.github.yuri256.gnucashscripts.job.ing;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.job.FileConverter;
import com.github.yuri256.gnucashscripts.job.CompleteJob;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IngJob extends CompleteJob {

    public static IngJob create(Config config) {
        String baseDir = config.get(Property.BASE_DIR);
        String jobDirName = config.get(Property.ING_JOB_DIR_NAME);
        String nextJobDirName = config.get(Property.GNU_CASH_DIR_NAME);
        Set<String> descriptionRemoveFieldKeys = Arrays.stream(config.get(Property.ING_DESCRIPTION_SKIP_FIELDS).trim().split(",")).collect(Collectors.toSet());
        return new IngJob(baseDir, jobDirName, nextJobDirName, descriptionRemoveFieldKeys);
    }

    public IngJob(String baseDir, String jobDirName, String nextJobDirName, Set<String> descriptionRemoveFieldKeys) {
        super(baseDir, jobDirName, nextJobDirName, new IngFileConverter(descriptionRemoveFieldKeys));
    }

}
