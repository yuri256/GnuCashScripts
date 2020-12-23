package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.job.FileConverter;
import com.github.yuri256.gnucashscripts.job.SimpleFileJob;
import com.github.yuri256.gnucashscripts.job.abn.AbnFileConverter;
import com.github.yuri256.gnucashscripts.job.abn.model.AbnConstants;
import com.github.yuri256.gnucashscripts.job.common.DescriptionFilterFunction;
import picocli.CommandLine;

@CommandLine.Command(name = "abnfile", description = "Convert ABN MT940 file to MT940 file accepted by GnuCash")
public class AbnFile implements Runnable {

    @CommandLine.Option(names = {"-f", "--file"}, description = "File to convert", required = true)
    private String file;

    @Override
    public void run() {
        DescriptionFilterFunction filterFunction = new DescriptionFilterFunction(AbnConstants.DEFAULT_REMOVE_FIELD_KEYS, AbnConstants.DEFAULT_REMOVE_KEY_KEYS);
        FileConverter fileConverter = new AbnFileConverter(filterFunction);
        SimpleFileJob fileJob = new SimpleFileJob(fileConverter);
        fileJob.run(file);
    }
}
