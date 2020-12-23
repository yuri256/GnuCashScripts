package com.github.yuri256.gnucashscripts.job;

import java.io.IOException;
import java.nio.file.Path;

@FunctionalInterface
public interface FileConverter {
    void apply(Path inPath, Path outPath) throws IOException;
}
