package com.github.yuri256.gnucashscripts.impl.revolut.job.fileconverter;

import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.MyMt940Record;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RevolutCsvRecordConverterTest {

    @Test
    void testRevertedStatus() {
        RevolutCsvRecord input = new RevolutCsvRecord();
        input.setState("REVERTED");
        RevolutCsvRecordConverter converter = new RevolutCsvRecordConverter();
        MyMt940Record result = converter.apply(input);
        assertNull(result, "The output should be null when the status is REVERTED");
    }

    @Test
    void testAccountNumberConcatenation() {
        RevolutCsvRecord input = new RevolutCsvRecord();
        input.setProduct("Product123");
        input.setCurrency("USD");
        input.setAmount("-1.23");
        input.setFee("0");
        input.setState("COMPLETED");
        input.setStartedDate("2024-09-04 15:54:39");
        input.setCompletedDate("2024-09-05 12:07:52");

        RevolutCsvRecordConverter converter = new RevolutCsvRecordConverter();
        MyMt940Record result = converter.apply(input);
        assertNotNull(result, "The output record should not be null for a valid state.");
        assertEquals("Product123_USD", result.getMT940().getField25().getValue(),
                "The account number should be a concatenation of product and currency fields.");
    }

    @Test
    void testDescriptionContainsType() {
        RevolutCsvRecord input = new RevolutCsvRecord();
        input.setType("TestType");
        input.setDescription("Original description.");
        input.setState("COMPLETED");
        input.setProduct("Product123");
        input.setCurrency("USD");
        input.setAmount("-1.23");
        input.setFee("0");
        input.setStartedDate("2024-09-04 15:54:39");
        input.setCompletedDate("2024-09-05 12:07:52");


        RevolutCsvRecordConverter converter = new RevolutCsvRecordConverter();
        MyMt940Record result = converter.apply(input);

        assertNotNull(result, "The output record should not be null for a valid state.");
        assertTrue(result.getMT940().getField86().get(0).getValue().contains("TestType"),
                "The description should contain the source type field.");
    }

    @Test
    void testFeeAddedToAmount() {
        RevolutCsvRecord input = new RevolutCsvRecord();
        input.setAmount("-100.50");
        input.setFee("2.50");
        input.setState("COMPLETED");
        input.setProduct("ProductA");
        input.setCurrency("EUR");
        input.setStartedDate("2024-09-04 15:54:39");
        input.setCompletedDate("2024-09-05 12:07:52");

        RevolutCsvRecordConverter converter = new RevolutCsvRecordConverter();
        MyMt940Record result = converter.apply(input);

        assertThat(result.getMT940().getField61().get(0).getValue()).contains("D103,00");
    }

    @Test
    void testSmallAmount() {
        RevolutCsvRecord input = new RevolutCsvRecord();
        input.setAmount("0.25");
        input.setFee("0");
        input.setState("COMPLETED");
        input.setProduct("ProductA");
        input.setCurrency("EUR");
        input.setStartedDate("2024-09-04 15:54:39");
        input.setCompletedDate("2024-09-05 12:07:52");

        RevolutCsvRecordConverter converter = new RevolutCsvRecordConverter();
        MyMt940Record result = converter.apply(input);

        assertThat(result.getMT940().getField61().get(0).getValue()).contains("C0,25");
    }

    @Test

    void testStartedDateInOutputCompletedDateNot() {
        RevolutCsvRecord input = new RevolutCsvRecord();
        input.setStartedDate("2023-05-01 16:01:41");
        input.setCompletedDate("2023-05-02 17:02:42");
        input.setAmount("250.75");
        input.setState("COMPLETED");
        input.setProduct("ProductX");
        input.setCurrency("GBP");
        input.setAmount("-1.23");
        input.setFee("0");

        RevolutCsvRecordConverter converter = new RevolutCsvRecordConverter();
        MyMt940Record result = converter.apply(input);

        assertNotNull(result, "The output record should not be null for a valid state.");
        assertThat(result.getMT940().getField61().get(0).getValue()).startsWith("230501");
        assertThat(result.getMT940().getField61().get(0).getValue()).doesNotContain("230502");
    }

    @Test
    public void testOutputFields() {
        RevolutCsvRecord input = new RevolutCsvRecord();
        input.setStartedDate("2023-05-01 16:01:41");
        input.setCompletedDate("2023-05-02 17:02:42");
        input.setAmount("250.75");
        input.setDescription("New Shoes");
        input.setType("CARD_PAYMENT");
        input.setState("COMPLETED");
        input.setProduct("ProductX");
        input.setCurrency("GBP");
        input.setAmount("-1.23");
        input.setFee("0");

        RevolutCsvRecordConverter converter = new RevolutCsvRecordConverter();
        MyMt940Record result = converter.apply(input);

        Assertions.assertEquals("ProductX_GBP", result.getMT940().getField25().getValue());
        Assertions.assertEquals("New Shoes CARD_PAYMENT", result.getMT940().getField86().get(0).getValue());
        Assertions.assertEquals("230501D1,23NTRFNONREF", result.getMT940().getField61().get(0).getValue());
    }
}