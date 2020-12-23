package com.github.yuri256.gnucashscripts.fileconvertor;

import java.io.IOException;
import java.nio.file.Path;

@FunctionalInterface
public interface FileConverter {
    void apply(Path inPath, Path outPath) throws IOException;
}
