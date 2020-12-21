package com.github.yuri256.gnucashscripts.cli;

import com.github.yuri256.gnucashscripts.cli.action.*;
import picocli.CommandLine;

@CommandLine.Command(subcommands = {
        Move.class
        , Ing.class
        , IngFile.class
        , Abn.class
        , AbnFile.class
        , Done.class
})
public class Dispatcher implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Dispatcher()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        // Sub-command should be used, so just printing the usage page
        spec.commandLine().usage(System.out);
    }
}
