package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.job.SimpleFileJob;
import com.github.yuri256.gnucashscripts.job.common.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.job.ing.IngFileConverter;
import com.github.yuri256.gnucashscripts.job.ing.model.IngConstants;
import picocli.CommandLine;

import java.util.Set;

@CommandLine.Command(name = "ingfile", description = "Convert ING CSV file to MT940 format accepted by GnuCash")
public class IngFile implements Runnable {

    @CommandLine.Option(names = {"-f", "--file"}, description = "File to convert", required = true)
    private String file;

    @Override
    public void run() {
        Set<String> removeFieldKeys = IngConstants.DEFAULT_REMOVE_FIELDS_KEYS;
        Set<String> removeKeyKeys = IngConstants.DEFAULT_REMOVE_KEY_KEYS;
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        IngFileConverter ingFileConverter = new IngFileConverter(descriptionFilterFunction);
        SimpleFileJob fileJob = new SimpleFileJob(ingFileConverter);
        fileJob.run(file);
    }

}
