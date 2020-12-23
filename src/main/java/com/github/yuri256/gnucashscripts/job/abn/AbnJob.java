package com.github.yuri256.gnucashscripts.job.abn;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.job.FileConverter;
import com.github.yuri256.gnucashscripts.job.CompleteJob;
import com.github.yuri256.gnucashscripts.job.abn.model.AbnConstants;
import com.github.yuri256.gnucashscripts.job.common.DescriptionFilterFunction;

import java.io.*;
import java.nio.file.Path;

public class AbnJob extends CompleteJob {

    public AbnJob(Config config) {
        this(config.get(Property.BASE_DIR), config.get(Property.ABN_JOB_DIR_NAME), config.get(Property.GNU_CASH_DIR_NAME), new DescriptionFilterFunction(AbnConstants.DEFAULT_REMOVE_FIELD_KEYS, AbnConstants.DEFAULT_REMOVE_KEY_KEYS));
    }

    public AbnJob(String baseDir, String jobDirName, String nextJobDirName, DescriptionFilterFunction filterFunction) {
        super(baseDir, jobDirName, nextJobDirName, new AbnFileConverter(filterFunction));
    }

    public static void main(String[] args) {
        new AbnJob(Config.load()).run();
    }

}
