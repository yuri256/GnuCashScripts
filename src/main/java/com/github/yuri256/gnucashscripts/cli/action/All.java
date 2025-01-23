package com.github.yuri256.gnucashscripts.cli.action;

import picocli.CommandLine;

@CommandLine.Command(name = "all", description = "Run all the commands of typical cycle: move, abn, ing, bunq")
public class All implements Runnable {

    @Override
    public void run() {
        new Move().run();
        new Abn().run();
        new Ing().run();
        new Bunq().run();
        new Revolut().run();
    }
}
