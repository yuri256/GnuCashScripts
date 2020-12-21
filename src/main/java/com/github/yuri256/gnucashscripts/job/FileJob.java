package com.github.yuri256.gnucashscripts.job;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileJob {
    private static final String IN = "in";
    private static final String OUT = "out";
    private static final String DONE = "done";

    private final String baseDir;
    private final String jobDirName;
    private final String nextJobDirName;

    public FileJob(String baseDir, String jobDirName, String nextJobDirName) {
        this.baseDir = baseDir;
        this.jobDirName = jobDirName;
        this.nextJobDirName = nextJobDirName;
    }

    protected abstract void processFile(Path inPath, Path outDir) throws IOException;

    public void run() {


        final File jobDir = getJobDir();

        final File inDir = getInDir(jobDir);
        final File outDir = getOutDir(jobDir);
        final File doneDir = getDoneDir(jobDir);

        try {
            Files.list(inDir.toPath()).forEach(inPath -> {
                try {
                    System.out.println("Processing file " + inPath.toString());

                    final Path outPath = outDir.toPath().resolve(inPath.getFileName());
                    final Path donePath = doneDir.toPath().resolve(inPath.getFileName());
                    if(donePath.toFile().exists()) {
                        System.out.println("File already processed, will skip. " + outPath.getFileName());
                        return;
                    }
                    processFile(inPath, outPath);

                    Files.move(inPath, donePath);

                    copyResultToNextJobInDir(outPath);
                } catch (IOException e) {
                    // TODO FIX exception handling in streams
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getJobDir() {
        File baseDirFile = new File(baseDir);
        checkExists(baseDirFile);
        return getExistingDir(baseDirFile, jobDirName);
    }

    private void copyResultToNextJobInDir(Path outPath) {
        if (nextJobDirName == null) {
            return;
        }
        Path nextJobDirPath = Paths.get(baseDir, nextJobDirName);
        checkExists(nextJobDirPath.toFile());

        File nextJobInDir = getInDir(nextJobDirPath.toFile());
        try {
            Files.copy(outPath, nextJobInDir.toPath().resolve(outPath.getFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkExists(File jobDir) {
        if (!jobDir.exists()) {
            throw new RuntimeException("Dir does not exist: " + jobDir);
        }
    }

    static File getOutDir(File jobDir) {
        return getExistingDir(jobDir, OUT);
    }

    public static File getDoneDir(File jobDir) {
        return getExistingDir(jobDir, DONE);
    }

    public static File getInDir(File jobDir) {
        return getExistingDir(jobDir, IN);
    }

    public File getInDir() {
        return getExistingDir(getJobDir(), IN);
    }

    static File getInDirPath(File jobDir) {
        return getExistingDir(jobDir, IN);
    }

    static File getExistingDir(File jobDir, String subDir) {
        final File inDir = new File(jobDir, subDir);
        if (!inDir.exists() && !inDir.mkdir()) {
            throw new RuntimeException("Could not create dir: " + inDir);
        }
        return inDir;
    }
}
