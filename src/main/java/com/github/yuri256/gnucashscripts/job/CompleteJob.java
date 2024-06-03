package com.github.yuri256.gnucashscripts.job;

import com.github.yuri256.gnucashscripts.fileconvertor.FileConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompleteJob {
    private static final String IN = "in";
    private static final String OUT = "out";
    private static final String DONE = "done";

    private final String baseDir;
    private final String jobDirName;
    private final String nextJobDirName;
    private final FileConverter fileConverter;

    public CompleteJob(String baseDir, String jobDirName, String nextJobDirName, FileConverter fileConverter) {
        this.baseDir = baseDir;
        this.jobDirName = jobDirName;
        this.nextJobDirName = nextJobDirName;
        this.fileConverter = fileConverter;
    }

    public void run() {
        final File jobDir = getJobDir();

        final File inDir = getInDir(jobDir);
        final File doneDir = getDoneDir(jobDir);
        final File outDir = getOutDir(jobDir, nextJobDirName, baseDir);

        try {
            Files.list(inDir.toPath()).forEach(inPath -> {
                try {
                    System.out.println("Processing file " + inPath.toString());

                    final Path donePath = doneDir.toPath().resolve(inPath.getFileName());
                    if (donePath.toFile().exists()) {
                        System.out.println("File already processed, will skip. (File " + donePath.getFileName() + " exists)");
                        return;
                    }

                    String fileName = inPath.getFileName().toString();
                    var fileNameNoExt = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
                    final Path outPath = outDir.toPath().resolve(fileNameNoExt + ".mt940");
                    fileConverter.apply(inPath, outPath);
                    System.out.println("Output written to " + outPath);

                    Files.move(inPath, donePath);
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

    public static void checkExists(File jobDir) {
        if (!jobDir.exists()) {
            throw new RuntimeException("Dir does not exist: " + jobDir);
        }
    }

    public static File getOutDir(File jobDir, String nextJobDirName1, String baseDir1) {
        if (nextJobDirName1 == null) {
            return getExistingDir(jobDir, OUT);
        }

        Path nextJobDirPath = Paths.get(baseDir1, nextJobDirName1);
        checkExists(nextJobDirPath.toFile());

        return getInDir(nextJobDirPath.toFile());
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

    static File getExistingDir(File jobDir, String subDir) {
        final File inDir = new File(jobDir, subDir);
        if (!inDir.exists() && !inDir.mkdir()) {
            throw new RuntimeException("Could not create dir: " + inDir);
        }
        return inDir;
    }
}
