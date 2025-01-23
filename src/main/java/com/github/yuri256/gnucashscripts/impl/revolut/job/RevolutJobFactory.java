package com.github.yuri256.gnucashscripts.impl.revolut.job;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.revolut.job.fileconverter.RevolutFileConverter;
import com.github.yuri256.gnucashscripts.job.CompleteJob;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RevolutJobFactory {

    public static CompleteJob createCompleteJob(Config config) {
        String baseDir = config.get(Property.BASE_DIR);
        String jobDirName = config.get(Property.REVOLUT_JOB_DIR_NAME);
        String nextJobDirName = config.get(Property.GNU_CASH_DIR_NAME);
        Set<String> removeFieldKeys = Arrays.stream(config.get(Property.REVOLUT_SKIP_FIELD_KEYS).trim().split(",")).collect(Collectors.toSet());
        Set<String> removeKeyKeys = Arrays.stream(config.get(Property.REVOLUT_SKIP_KEY_KEYS).trim().split(",")).collect(Collectors.toSet());
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        RevolutFileConverter fileConverter = new RevolutFileConverter();
        return new CompleteJob(baseDir, jobDirName, nextJobDirName, fileConverter);
    }

}
