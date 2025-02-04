package com.github.yuri256.gnucashscripts.impl.abn.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.fileconvertor.FileConverter;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * Abn format has 3 extra lines before each record. Example:
 * ABNANL2A
 * 940
 * ABNANL2A
 * :20:ABN AMRO BANK NV
 * :25:112233445
 * :28:12345/1
 * :60F:C191031EUR123,45
 * :61:1911011101D12,34N247NONREF
 * :86:/TRTP/SEPA INCASSO ALGEMEEN DOORLOPEND/CSID/NL98XXXX1234567890
 * /NAME/ABN AMRO BANK NV/MARF/ST11122223333444455/REMI/TERMIJNBETAL
 * THE DESCRIPTION/IBAN/NL33XXXX1234567890/BIC/ABNANL2
 * A/EREF/1111222233334444
 * :62F:C191101EUR111,11
 * -
 * <p>
 * GnuCash does not like them, so we need to remove them
 */
public class AbnFileConverter implements FileConverter {

    private static final int SKIP_COUNT = 3;
    private static final String STATEMENT_BLOCK_PREFX = ":86:";

    private final DescriptionFilterFunction filterFunction;

    public AbnFileConverter(DescriptionFilterFunction filterFunction) {
        this.filterFunction = filterFunction;
    }

    @Override
    public void apply(Path inPath, Path outPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inPath.toFile()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outPath.toFile()))) {
            String line;
            // first record starts on the third line, so skip the extra lines
            int skipCount = SKIP_COUNT;
            boolean statementBlock = false;
            boolean statementBlockEnded = false;
            List<String> statementLines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (skipCount > 0) {
                    skipCount--;
                    continue;
                }
                if ("-".equals(line)) { // new record starts
                    skipCount = SKIP_COUNT;
                }

                if (statementBlock) {
                    if (line.startsWith(":") || "-".equals(line)) {
                        statementBlock = false;
                        statementBlockEnded = true;
                    } else {
                        statementLines.add(line);
                    }
                }

                if (line.startsWith(STATEMENT_BLOCK_PREFX)) {
                    statementBlock = true;
                    statementLines.add(line.substring(4));
                }

                if (statementBlockEnded) {
                    writer.write(STATEMENT_BLOCK_PREFX);
                    String statement = String.join(" ", statementLines).replaceAll("\\s+", " ").trim();
                    var isTerminalPayment = statement.matches("^BEA,*\\s.*$");
                    if (isTerminalPayment) {
                        statement = patchTerminalPayment(statement);
                    }
                    statement = patchStatement(statement);
                    statement = filterFunction.apply(statement);
                    writer.write(statement);
                    newLine(writer);
                    statementBlockEnded = false;
                    statementLines = new ArrayList<>();
                }
                if (!statementBlock) {
                    writer.write(line);
                    newLine(writer);
                }
            }
        }
    }

    // Parses and removed not deeded fields from terminal payment statement line
    // Example line: "BEA NR:22N6J5 01.12.20/10.33 ALBERT HEIJN 1234,PAS123 CITY"
    // Example line: "BEA, BETAALPAS CCV*CHEMISCH BEMISCH,PAS123 NR:AB123456, 01.01.21/12:01 CITY
    private String patchTerminalPayment(String statement) {
        String line = statement;

        line = line.replaceAll("BEA,*\\s", " ");
        line = line.replaceAll("NR:\\S*\\s", " ");
        line = line.replaceAll(",PAS\\d+", " ");
        line = line.replaceAll("BETAALPAS\\s", " ");

        return line;
    }

    private void newLine(BufferedWriter writer) throws IOException {
        writer.write("\n");
    }

    private String patchStatement(String line) {
        // GnuCash uses bayesian analysis to match account and it doesn't treat slash as separator for tokens,
        // so replacing it with space to separate key-value pairs and colon to separate key and value.
        // This makes it somilar to the ING format
        return line.replace("/TRTP/", " TRTP: ")
                .replace("/CSID/", " CSID: ")
                .replace("/NAME/", " NAME: ")
                .replace("/MARF/", " MARF: ")
                .replace("/REMI/", " REMI: ")
                .replace("/EREF/", " EREF: ")
                .replace("/IBAN/", " IBAN: ")
                .replace("/BIC/", " BIC: ")
                .replaceAll("\\s+", " ")
                .trim()
                ;
    }

}
