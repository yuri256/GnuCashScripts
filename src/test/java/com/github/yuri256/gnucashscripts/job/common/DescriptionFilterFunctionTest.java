package com.github.yuri256.gnucashscripts.job.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class DescriptionFilterFunctionTest {

    @Test
    public void testNaamRemainsKeyRemoved() {
        Assertions.assertEquals("The Name", createFilterFunction().apply("Naam: The Name"));
    }

    @Test
    public void testNameDescRemains() {
        Assertions.assertEquals("The Name desc1", createFilterFunction().apply("Naam: The Name Omschrijving: desc1"));
    }

    @Test
    public void testKenmerkSkippedLast() {
        Assertions.assertEquals("naam1 desc1", createFilterFunction().apply("Naam: naam1 Omschrijving: desc1 Kenmerk: 00000123"));
    }

    @Test
    public void testKenmerkSkippedMiddle() {
        Assertions.assertEquals("naam1 desc1", createFilterFunction().apply("Naam: naam1 Kenmerk: 00000123 Omschrijving: desc1"));
    }

    @Test
    public void testKenmerkSkippedFirst() {
        Assertions.assertEquals("naam1 desc1", createFilterFunction().apply("Kenmerk: 00000123 Naam: naam1 Omschrijving: desc1"));
    }

    @Test
    public void testTimeIsNotSplit() {
        Assertions.assertEquals("DateTime: 09-04-2020 17:42", createFilterFunction().apply("DateTime: 09-04-2020 17:42"));
    }

    @Test
    public void testTimeIsNotSplitLast() {
        Assertions.assertEquals("The Name desc1 DateTime: 09-04-2020 17:42", createFilterFunction().apply("Naam: The Name Omschrijving: desc1 DateTime: 09-04-2020 17:42"));
    }

    @Test
    public void testTimeIsNotSplitFirst() {
        Assertions.assertEquals("DateTime: 09-04-2020 17:42 The Name desc1", createFilterFunction().apply("DateTime: 09-04-2020 17:42 Naam: The Name Omschrijving: desc1"));
    }

    @Test
    public void testTimeIsNotSplitMiddle() {
        Assertions.assertEquals("The Name DateTime: 09-04-2020 17:42 desc1", createFilterFunction().apply("Naam: The Name DateTime: 09-04-2020 17:42 Omschrijving: desc1"));
    }

    @Test
    public void testDatumTijdSecondsRemovedAndIsNotSplit() {
        Assertions.assertEquals("Datum/Tijd: 09-04-2020 11:12", createFilterFunction().apply("Datum/Tijd: 09-04-2020 11:12:13"));
    }

    @Test
    public void testSkipFieldWithSpaceRemoved() {
        Assertions.assertEquals("The Name", createFilterFunction().apply("Naam: The Name Incassant ID: 0001"));
    }

    @Test
    public void testValueWithSemicolonsNoSpace() {
        Assertions.assertEquals("The Name desc1:with semi:colons", createFilterFunction().apply("Naam: The Name Omschrijving: desc1:with semi:colons"));
    }

    @Test
    public void testValueWithSemicolonsAndSpace() {
        Assertions.assertEquals("The Name key1: value1 key2: value2", createFilterFunction().apply("Naam: The Name Omschrijving: key1 : value1 key2 : value2"));
    }

    @Test
    public void testNaamOmschrijvingRemainsKeyRemoved() {
        Assertions.assertEquals("Name desc 1", createFilterFunction().apply("NaamOmschrijving: Name desc 1"));
    }

    @Test
    public void testNaamOmschrijvingRemainsKeyRemovedLastPosition() {
        Assertions.assertEquals("Key1: value one Name desc 1", createFilterFunction().apply("Key1: value one NaamOmschrijving: Name desc 1"));
    }

    @Test
    public void testNaamOmschrijvingRemainsKeyRemovedMiddlePosition() {
        Assertions.assertEquals("Key1: value one Name desc 1 Key2: value two", createFilterFunction().apply("Key1: value one NaamOmschrijving: Name desc 1 Key2: value two"));
    }

    private DescriptionFilterFunction createFilterFunction() {
        return new DescriptionFilterFunction(Set.of("Kenmerk", "Incassant ID"), Set.of("Naam","Omschrijving", "NaamOmschrijving"));
    }

}