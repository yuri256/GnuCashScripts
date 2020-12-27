package com.github.yuri256.gnucashscripts.impl.ing.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngConstants;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngRecord;

import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.Function;

public class IngDescriptionConverter implements Function<IngRecord, String> {

    private final DescriptionFilterFunction descriptionFilterFunction;

    public IngDescriptionConverter(DescriptionFilterFunction descriptionFilterFunction) {
        this.descriptionFilterFunction = descriptionFilterFunction;
    }

    @Override
    public String apply(IngRecord ingRecord) {
        String description = createDescription(ingRecord);

        // do final filtering
        return descriptionFilterFunction.apply(description);
    }

    private String createDescription(IngRecord ingRecord) {
        StringBuilder sb = new StringBuilder();

        // Add NaamOmschrijving only if there is no Naam in Medelingen, otherwise it will be duplicated
        if (!ingRecord.getMededelingen().contains(IngConstants.NAAM + ":")) {
            sb.append(IngConstants.NAAM_OMSCHRIJVING + ": ").append(ingRecord.getNaamOmschrijving()).append(" ");
        }

        sb.append(getDescriptionFromMedelingen(ingRecord));

        sb.append(" TegenRekening: ").append(ingRecord.getTegenRekening());
        sb.append(" MutatieSoort: ").append(ingRecord.getMutatieSoort());
        return sb.toString();
    }

    private static String getDescriptionFromMedelingen(IngRecord ingRecord) {
        if (IngConstants.MUTATIESOORT_BETAALAUTOMAAT.equals(ingRecord.getMutatieSoort()) || IngConstants.MUTATIESOORT_GELDAUTOMAAT.equals(ingRecord.getMutatieSoort())) {
            return getDescriptionTerminalPayment(ingRecord.getMededelingen());
        }
        return getDescriptionGeneral(ingRecord.getMededelingen());
    }

    private static String getDescriptionGeneral(String description) {
        Objects.requireNonNull(description);
        StringBuilder sb = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(description, " ");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (IngConstants.VALUTADATUM.equals(token)) {
                // skip, not interesting. We already have a date
                tokenizer.nextToken();
                continue;
            }
            sb.append(token).append(" ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static String getDescriptionTerminalPayment(String description) {
        Objects.requireNonNull(description);
        // Terminal event description, example:
        //  Pasvolgnr: 001 07-03-2020 09:34 Transactie: A111B2 Term: AB1234 Valutadatum: 07-03-2020
        StringBuilder sb = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(description, " ");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            switch (token) {
                case "Pasvolgnr:":
                    String threeCharacterNumber = tokenizer.nextToken();
                    if (threeCharacterNumber.length() != 3) {
                        throw new RuntimeException("Invalid Pasvolgnr format, not 3 char first token: " + description);
                    }
                    String date = tokenizer.nextToken();
                    if (date.length() != 10) {
                        throw new RuntimeException("Bad date format: " + description);
                    }
                    String time = tokenizer.nextToken();
                    if (time.length() != 5) {
                        throw new RuntimeException("Bad time format: " + description);
                    }
                    sb.append(IngConstants.DATUM_TIJD + ": ").append(date).append(" ").append(time).append(" ");
                    break;
                // skip those, no extra information
                case "Transactie:":
                case "Term:":
                case IngConstants.VALUTADATUM:
                    tokenizer.nextToken();
                    break;
                default:
                    sb.append(token).append(" ");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
