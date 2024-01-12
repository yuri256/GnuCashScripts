package com.github.yuri256.gnucashscripts.fileconvertor;

import com.github.yuri256.gnucashscripts.impl.abn.model.AbnConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Splits string into key-value pairs, filters out skipFields, removes predefined keys (retains values)
 */
public class DescriptionFilterFunction implements Function<String, String> {

    private static final Pattern TIME_SEC_PATTERN = Pattern.compile("(\\s[0-2]\\d:[0-5]\\d):[0-5]\\d");
    private static final Pattern TIME_MIN_PATTERN = Pattern.compile("(\\s[0-2]\\d):([0-5]\\d)");
    private static final Pattern TIME_MIN_REPLACEMENT_PATTERN = Pattern.compile("(\\s[0-2]\\d)_([0-5]\\d)");

    private final Set<String> removeFieldKeys;
    private final Set<String> removeKeyKeys;
    private final Set<String> keysWithSpace;

    public DescriptionFilterFunction(Set<String> removeFieldKeys, Set<String> removeKeyKeys) {
        this.removeFieldKeys = Collections.unmodifiableSet(removeFieldKeys);
        keysWithSpace = Stream.concat(
                        AbnConstants.KNOWN_KEYS_WITH_SPACE.stream(),
                this.removeFieldKeys.stream().filter(it -> it.contains(" "))
        ).collect(Collectors.toUnmodifiableSet());
        this.removeKeyKeys = removeKeyKeys;
    }

    @Override
    public String apply(String input) {
        if (input == null) {
            return null;
        }

        // remove seconds
        input = TIME_SEC_PATTERN.matcher(input.trim()).replaceAll(matchResult -> matchResult.group(1));

        // replace colon with underscore for time
        input = TIME_MIN_PATTERN.matcher(input.trim()).replaceAll(matchResult -> matchResult.group(1) + "_" + matchResult.group(2));

        String[] split = input.trim().split(": ");
        if (split.length <= 1) {
            return input;
        }

        StringBuilder sb = new StringBuilder();
        String key = null;
        for (int i = 0; i < split.length; i++) {
            String token = split[i].trim();
            if (i == 0) {
                key = token;
                continue;
            }

            boolean lastToken = (i == split.length - 1);
            if (lastToken) {
                addPair(sb, key, token);
                break;
            }

            final String nextKey;
            final String value;
            Optional<String> keyWithSpace = keysWithSpace.stream().filter(token::endsWith).findFirst();
            if (keyWithSpace.isPresent()) {
                nextKey = keyWithSpace.get();
                value = token.substring(0, token.length() - keyWithSpace.get().length() - 1);
            } else {
                String[] parts = token.split(" ");
                nextKey = parts[parts.length - 1];
                if (parts.length == 1) { // this happens in case values contains ': ' separated key values
                    value = "";
                } else {
                    value = Arrays.stream(parts, 0, parts.length - 1).collect(Collectors.joining(" "));
                }
            }
            addPair(sb, key, value);
            key = nextKey;
        }
        sb.deleteCharAt(sb.length() - 1);

        // restore time format
        return TIME_MIN_REPLACEMENT_PATTERN.matcher(sb.toString()).replaceAll(matchResult -> matchResult.group(1) + ":" + matchResult.group(2));
    }

    private void addPair(StringBuilder sb, String key, String value) {
        if (removeFieldKeys.contains(key)) {
            return;
        }
        if (!removeKeyKeys.contains(key)) {
            sb.append(key).append(": ");
        }
        sb.append(value);
        if (!value.isEmpty()) {
            sb.append(" ");
        }
    }
}
