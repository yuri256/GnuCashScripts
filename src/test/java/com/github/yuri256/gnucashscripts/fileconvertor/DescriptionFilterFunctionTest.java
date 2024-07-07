package com.github.yuri256.gnucashscripts.fileconvertor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class DescriptionFilterFunctionTest {

    @Test
    public void testNameKeyRemovedValueRemains() {
        Assertions.assertEquals("The Name", createFilterFunction().apply("Name: The Name"));
    }

    @Test
    public void testNameAndDescriptionRemain() {
        Assertions.assertEquals("The Name desc1", createFilterFunction().apply("Name: The Name Description: desc1"));
    }

    @Test
    public void testReferenceRemovedFromLastPosition() {
        Assertions.assertEquals("Name1 desc1", createFilterFunction().apply("Name: Name1 Description: desc1 Reference: 00000123"));
    }

    @Test
    public void testReferenceRemovedFromMiddlePosition() {
        Assertions.assertEquals("Name1 desc1", createFilterFunction().apply("Name: Name1 Reference: 00000123 Description: desc1"));
    }

    @Test
    public void testReferenceRemovedFromFirstPosition() {
        Assertions.assertEquals("Name1 desc1", createFilterFunction().apply("Reference: 00000123 Name: Name1 Description: desc1"));
    }

    @Test
    public void testDateAndTimeAreNotSplit() {
        Assertions.assertEquals("DateTime: 09-04-2020 17:42", createFilterFunction().apply("DateTime: 09-04-2020 17:42"));
    }

    @Test
    public void testDateAndTimeAreNotSplitInLastPosition() {
        Assertions.assertEquals("The Name desc1 DateTime: 09-04-2020 17:42", createFilterFunction().apply("Name: The Name Description: desc1 DateTime: 09-04-2020 17:42"));
    }

    @Test
    public void testDateAndTimeAreNotSplitInFirstPosition() {
        Assertions.assertEquals("DateTime: 09-04-2020 17:42 The Name desc1", createFilterFunction().apply("DateTime: 09-04-2020 17:42 Name: The Name Description: desc1"));
    }

    @Test
    public void testDateAndTimeAreNotSplitInMiddlePosition() {
        Assertions.assertEquals("The Name DateTime: 09-04-2020 17:42 desc1", createFilterFunction().apply("Name: The Name DateTime: 09-04-2020 17:42 Description: desc1"));
    }

    @Test
    public void testSecondsRemovedFromDateTimeAndTimeIsNotSplit() {
        Assertions.assertEquals("Date/time: 09-04-2020 11:12", createFilterFunction().apply("Date/time: 09-04-2020 11:12:13"));
    }

    @Test
    public void testSkipFieldWithSpaceInKeyRemoved() {
        Assertions.assertEquals("The Name", createFilterFunction().apply("Name: The Name Creditor ID: 0001"));
    }

    @Test
    public void testValueWithSemicolonsNoSpaceRemains() {
        Assertions.assertEquals("The Name value:with semi:colons", createFilterFunction().apply("Name: The Name Description: value:with semi:colons"));
    }

    @Test
    public void testValueWithSemicolonsAndSpaceRemains() {
        Assertions.assertEquals("The Name key1: value1 key2: value2", createFilterFunction().apply("Name: The Name Description: key1 : value1 key2 : value2"));
    }

    @Test
    public void testRemoveKeyIsRemovedValueRemains() {
        Assertions.assertEquals("value 1", createFilterFunction().apply("Name/Description: value 1"));
    }

    @Test
    public void testRemoveKeyIsRemovedValueRemainsLastPosition() {
        Assertions.assertEquals("Key1: value one Name desc 1", createFilterFunction().apply("Key1: value one Name/Description: Name desc 1"));
    }

    @Test
    public void testRemoveKeyIsRemovedValueRemainsMiddlePosition() {
        Assertions.assertEquals("Key1: value one value two Key3: value three", createFilterFunction().apply("Key1: value one Name/Description: value two Key3: value three"));
    }

    @Test
    public void testRemoveKeyIsRemovedValueRemainsMiddlePosition11() {
        Assertions.assertEquals("Some text without key some description", createFilterFunction().apply("Some text without key Description: some description"));
    }

    private DescriptionFilterFunction createFilterFunction() {
        return new DescriptionFilterFunction(Set.of("Reference", "Creditor ID"), Set.of("Name", "Description", "Name/Description"));
    }

}