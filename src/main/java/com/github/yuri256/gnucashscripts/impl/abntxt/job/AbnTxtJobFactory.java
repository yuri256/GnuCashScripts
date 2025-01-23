package com.github.yuri256.gnucashscripts.impl.abntxt.job;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.fileconvertor.FileConverter;
import com.github.yuri256.gnucashscripts.impl.abn.fileconvertor.AbnFileConverter;
import com.github.yuri256.gnucashscripts.impl.abn.model.AbnConstants;
import com.github.yuri256.gnucashscripts.job.CompleteJob;
import com.github.yuri256.gnucashscripts.job.SimpleFileJob;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AbnTxtJobFactory {

    public static CompleteJob createCompleteJob(Config config) {
        String baseDir = config.get(Property.BASE_DIR);
        String jobDirName = config.get(Property.ABN_TXT_JOB_DIR_NAME);
        String nextJobDirName = config.get(Property.GNU_CASH_DIR_NAME);
        Set<String> removeFieldKeys = Arrays.stream(config.get(Property.ABN_SKIP_FIELD_KEYS).trim().split(",")).collect(Collectors.toSet());
        Set<String> removeKeyKeys = Arrays.stream(config.get(Property.ABN_SKIP_KEY_KEYS).trim().split(",")).collect(Collectors.toSet());

        // TODO implement correct convertor
        DescriptionFilterFunction filterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        AbnFileConverter fileConverter = new AbnFileConverter(filterFunction);
        return new CompleteJob(baseDir, jobDirName, nextJobDirName, fileConverter);
    }

    public static SimpleFileJob createSimpleFileJob() {
        Set<String> removeFieldKeys = AbnConstants.DEFAULT_SKIP_FIELD_KEYS;
        Set<String> removeKeyKeys = AbnConstants.DEFAULT_SKIP_KEY_KEYS;
        DescriptionFilterFunction filterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        FileConverter fileConverter = new AbnFileConverter(filterFunction);
        return new SimpleFileJob(fileConverter);
    }

}
