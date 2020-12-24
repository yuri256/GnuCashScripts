package com.github.yuri256.gnucashscripts.impl.abn.job;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.fileconvertor.FileConverter;
import com.github.yuri256.gnucashscripts.impl.abn.fileconvertor.AbnFileConverter;
import com.github.yuri256.gnucashscripts.impl.abn.model.AbnConstants;
import com.github.yuri256.gnucashscripts.job.CompleteJob;
import com.github.yuri256.gnucashscripts.job.SimpleFileJob;

import java.util.Set;

public class AbnJobFactory {

    public static CompleteJob createCompleteJob(Config config) {
        String baseDir = config.get(Property.BASE_DIR);
        String jobDirName = config.get(Property.ABN_JOB_DIR_NAME);
        String nextJobDirName = config.get(Property.GNU_CASH_DIR_NAME);
        Set<String> removeFieldKeys = AbnConstants.DEFAULT_REMOVE_FIELD_KEYS;
        Set<String> removeKeyKeys = AbnConstants.DEFAULT_REMOVE_KEY_KEYS;
        DescriptionFilterFunction filterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        AbnFileConverter fileConverter = new AbnFileConverter(filterFunction);
        return new CompleteJob(baseDir, jobDirName, nextJobDirName, fileConverter);
    }

    public static SimpleFileJob createSimpleFileJob() {
        Set<String> removeFieldKeys = AbnConstants.DEFAULT_REMOVE_FIELD_KEYS;
        Set<String> removeKeyKeys = AbnConstants.DEFAULT_REMOVE_KEY_KEYS;
        DescriptionFilterFunction filterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        FileConverter fileConverter = new AbnFileConverter(filterFunction);
        return new SimpleFileJob(fileConverter);
    }

}
