package com.github.yuri256.gnucashscripts.impl.ing.job;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.IngDescriptionConverter;
import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.IngFileConverter;
import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.IngMyMt940Converter;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngConstants;
import com.github.yuri256.gnucashscripts.job.CompleteJob;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class IngJob extends CompleteJob {

    public static IngJob create(Config config) {
        String baseDir = config.get(Property.BASE_DIR);
        String jobDirName = config.get(Property.ING_JOB_DIR_NAME);
        String nextJobDirName = config.get(Property.GNU_CASH_DIR_NAME);
        Set<String> removeFieldKeys = Arrays.stream(config.get(Property.ING_DESCRIPTION_SKIP_FIELDS).trim().split(",")).collect(Collectors.toSet());
        Set<String> removeKeyKeys = IngConstants.DEFAULT_REMOVE_KEY_KEYS;
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        IngFileConverter fileConverter = new IngFileConverter(new IngMyMt940Converter(new IngDescriptionConverter(descriptionFilterFunction)));
        return new IngJob(baseDir, jobDirName, nextJobDirName, fileConverter);
    }

    public IngJob(String baseDir, String jobDirName, String nextJobDirName, IngFileConverter fileConverter) {
        super(baseDir, jobDirName, nextJobDirName, fileConverter);
    }

}
