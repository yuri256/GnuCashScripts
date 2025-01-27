package com.github.yuri256.gnucashscripts.filematcher;


import java.nio.file.Path;
import java.util.Optional;

public interface FileMatcher {
    /**
     * Checks that the supplied file path can be processed by returning the optional target path.
     *
     * @param filePath The file to process.
     * @return An {@code Optional} containing the target path, or {@code Optional.empty()} if the file cannot be handled.
     */
    Optional<Path> getTargetFileIfHandled(Path filePath);
}