package com.github.yuri256.gnucashscripts.impl.ingsavings.job;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.fileconvertor.FileConverter;
import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.IngDescriptionConverter;
import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.IngFileConverter;
import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.IngMyMt940Converter;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngConstants;
import com.github.yuri256.gnucashscripts.job.CompleteJob;
import com.github.yuri256.gnucashscripts.job.SimpleFileJob;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class IngSavingsJobFactory {

    public static CompleteJob createCompleteJob(Config config) {
        String baseDir = config.get(Property.BASE_DIR);
        String jobDirName = config.get(Property.ING_SAVINGS_JOB_DIR_NAME);
        String nextJobDirName = config.get(Property.GNU_CASH_DIR_NAME);
        Set<String> removeFieldKeys = Arrays.stream(config.get(Property.ING_SAVINGS_SKIP_FIELD_KEYS).trim().split(",")).collect(Collectors.toSet());
        Set<String> removeKeyKeys = Arrays.stream(config.get(Property.ING_SAVINGS_SKIP_KEY_KEYS).trim().split(",")).collect(Collectors.toSet());
        // TODO implement
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        IngFileConverter fileConverter = new IngFileConverter(new IngMyMt940Converter(new IngDescriptionConverter(descriptionFilterFunction)));
        return new CompleteJob(baseDir, jobDirName, nextJobDirName, fileConverter);
    }

    public static SimpleFileJob createSimpleFileJob() {
        Set<String> removeFieldKeys = IngConstants.DEFAULT_SKIP_FIELD_KEYS;
        Set<String> removeKeyKeys = IngConstants.DEFAULT_SKIP_KEY_KEYS;
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        IngDescriptionConverter descriptionConverter = new IngDescriptionConverter(descriptionFilterFunction);
        IngMyMt940Converter myMt940Converter = new IngMyMt940Converter(descriptionConverter);
        FileConverter fileConverter = new IngFileConverter(myMt940Converter);
        return new SimpleFileJob(fileConverter);

    }
}
