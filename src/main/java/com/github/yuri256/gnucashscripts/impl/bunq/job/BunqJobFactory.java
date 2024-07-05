package com.github.yuri256.gnucashscripts.impl.bunq.job;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.bunq.fileconvertor.BunqDescriptionConverter;
import com.github.yuri256.gnucashscripts.impl.bunq.fileconvertor.BunqFileConverter;
import com.github.yuri256.gnucashscripts.impl.bunq.fileconvertor.BunqMyMt940Converter;
import com.github.yuri256.gnucashscripts.job.CompleteJob;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BunqJobFactory {

    public static CompleteJob createCompleteJob(Config config) {
        String baseDir = config.get(Property.BASE_DIR);
        String jobDirName = config.get(Property.BUNQ_JOB_DIR_NAME);
        String nextJobDirName = config.get(Property.GNU_CASH_DIR_NAME);
        Set<String> removeFieldKeys = Arrays.stream(config.get(Property.BUNQ_SKIP_FIELD_KEYS).trim().split(",")).collect(Collectors.toSet());
        Set<String> removeKeyKeys = Arrays.stream(config.get(Property.BUNQ_SKIP_KEY_KEYS).trim().split(",")).collect(Collectors.toSet());
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        BunqFileConverter fileConverter = new BunqFileConverter(new BunqMyMt940Converter(new BunqDescriptionConverter(descriptionFilterFunction)));
        return new CompleteJob(baseDir, jobDirName, nextJobDirName, fileConverter);
    }

}
