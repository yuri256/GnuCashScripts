package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.job.SimpleFileJob;
import com.github.yuri256.gnucashscripts.job.ing.IngFileConverter;
import com.github.yuri256.gnucashscripts.job.ing.model.IngConstants;
import picocli.CommandLine;

@CommandLine.Command(name = "ingfile", description = "Convert ING CSV file to MT940 format accepted by GnuCash")
public class IngFile implements Runnable {

    @CommandLine.Option(names = {"-f", "--file"}, description = "File to convert", required = true)
    private String file;

    @Override
    public void run() {
        IngFileConverter ingFileConverter = new IngFileConverter(IngConstants.DEFAULT_REMOVE_FIELDS_KEYS);
        SimpleFileJob fileJob = new SimpleFileJob(ingFileConverter);
        fileJob.run(file);
    }

}
