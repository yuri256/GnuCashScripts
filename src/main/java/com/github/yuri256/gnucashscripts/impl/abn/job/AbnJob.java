package com.github.yuri256.gnucashscripts.impl.abn.job;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.abn.fileconvertor.AbnFileConverter;
import com.github.yuri256.gnucashscripts.impl.abn.model.AbnConstants;
import com.github.yuri256.gnucashscripts.job.CompleteJob;

public class AbnJob extends CompleteJob {

    public static AbnJob create(Config config) {
        return new AbnJob(config.get(Property.BASE_DIR), config.get(Property.ABN_JOB_DIR_NAME), config.get(Property.GNU_CASH_DIR_NAME), new AbnFileConverter(new DescriptionFilterFunction(AbnConstants.DEFAULT_REMOVE_FIELD_KEYS, AbnConstants.DEFAULT_REMOVE_KEY_KEYS)));
    }

    public AbnJob(String baseDir, String jobDirName, String nextJobDirName, AbnFileConverter fileConverter) {
        super(baseDir, jobDirName, nextJobDirName, fileConverter);
    }

}
