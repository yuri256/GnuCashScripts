package com.github.yuri256.gnucashscripts.job.ing;

import com.github.yuri256.gnucashscripts.job.common.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.job.ing.model.AfBij;
import com.github.yuri256.gnucashscripts.job.ing.model.IngConstants;
import com.github.yuri256.gnucashscripts.job.ing.model.IngRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class DescriptionConverterTest {
    @Test
    public void testTerminalPayment() {
        String record = createConverter().apply(new IngRecord("20010304", "Test Name-Desc", "TestRekening", "TestTegenRekening", "BA", AfBij.Af, "12,34", "Betaalautomaat", "Pasvolgnr: 001 07-03-2020 09:34 Transactie: A111B2 Term: AB1234 Valutadatum: 07-03-2020"));
        Assertions.assertEquals("Test Name-Desc Datum/Tijd: 07-03-2020 09:34", record);
    }
    @Test
    public void testGeldautomaatWithdrawal() {
        String record = createConverter().apply(new IngRecord("20010304", "Test Name-Desc", "TestRekening", "TestTegenRekening", "BA", AfBij.Af, "12,34", "Geldautomaat", "Pasvolgnr: 001 07-03-2020 09:34 Transactie: A111B2 Term: AB1234 Valutadatum: 07-03-2020"));
        Assertions.assertEquals("Test Name-Desc Datum/Tijd: 07-03-2020 09:34 MutatieSoort: Geldautomaat", record);
    }
    @Test
    public void testInternalTransfer() {
        String record = createConverter().apply(new IngRecord("20010304", "Hr J DOE", "NL03TestRekening", "", "GT", AfBij.Bij, "12,34", "Online bankieren", "Van Spaarrekening 123456789 the description Valutadatum: 12-03-2020"));
        Assertions.assertEquals("Hr J DOE Van Spaarrekening 123456789 the description MutatieSoort: Online bankieren", record);
    }

    @Test
    public void testIncasso() {
        String record = createConverter().apply(new IngRecord("20010304", "The Company Name", "TestRekening", "TheCompanyRekening", "IC", AfBij.Af, "12,34", "Incasso", "Naam: TheCompany Omschrijving: Fact 0001 desc IBAN: NL18TestIban2 Kenmerk: 12345678 Machtiging ID: 1234567 Incassant ID: 123456 Doorlopende incasso Valutadatum: 06-03-2020"));
        Assertions.assertEquals("TheCompany Fact 0001 desc IBAN: NL18TestIban2 MutatieSoort: Incasso", record);
    }

    @Test
    public void testIncassoCreditCard() {
        String record = createConverter().apply(new IngRecord("20010304", "INCASSO CREDITCARD ACCOUNTNR XXX", "TestRekening", "", "DV", AfBij.Af, "12,34", "Diversen", " ZIE REKENINGOVERZICHT 01-01-2020 Valutadatum: 02-01-2020"));
        Assertions.assertEquals("INCASSO CREDITCARD ACCOUNTNR XXX ZIE REKENINGOVERZICHT 01-01-2020 MutatieSoort: Diversen", record);
    }

    @Test
    public void testOnlinePurchase() {
        String record = createConverter().apply(new IngRecord("20010304", "Merchant1", "TestRekening", "TestTegenRekening", "GT", AfBij.Af, "12,34", "Online bankieren", "Naam: Merchant1 Omschrijving: REF1 ID1 Order ORDER1 DESC1 IBAN: TestTegenRekening Kenmerk: 04-03-2020 08:21 KENMERK1 Valutadatum: 04-03-2020"));
        Assertions.assertEquals("Merchant1 REF1 ID1 Order ORDER1 DESC1 IBAN: TestTegenRekening MutatieSoort: Online bankieren", record);
    }

    @Test
    public void testPaymentRequest() {
        String record = createConverter().apply(new IngRecord("20010304", "ING Bank NV Betaalverzoek", "TestRekening", "TestTegenRekening", "GT", AfBij.Bij, "12,34", "Online bankieren", "Naam: ING Bank NV Betaalverzoek Omschrijving: Betaling van J DOE NL03TestIban the description IBAN: NL03TestIban2 Kenmerk: theKenmerk Valutadatum: 13-03-2020"));
        Assertions.assertEquals("ING Bank NV Betaalverzoek Betaling van J DOE NL03TestIban the description IBAN: NL03TestIban2 MutatieSoort: Online bankieren", record);
    }

    @Test
    public void testBankTransferToAccount() {
        String record = createConverter().apply(new IngRecord("20010304", "J DOE", "TestRekening", "TestTegenRekening", "OV", AfBij.Bij, "12,34", "Overschrijving", "Naam: J DOE IBAN: NL03TestIban Datum/Tijd: 01-01-2020 12:34:56 Valutadatum: 01-01-2020"));
        Assertions.assertEquals("J DOE IBAN: NL03TestIban Datum/Tijd: 01-01-2020 12:34 TegenRekening: TestTegenRekening MutatieSoort: Overschrijving", record);
    }

    @Test
    public void testBankTransferFromAccount() {
        String record = createConverter().apply(new IngRecord("20010304", "J DOE", "TestRekening", "TestTegenRekening", "GT", AfBij.Af, "12,34", "Online bankieren", "Naam: J DOE IBAN: TestRekening Omschrijving: For Abn recurring payments IBAN: NL03TestIban Valutadatum: 01-01-2020"));
        Assertions.assertEquals("J DOE IBAN: TestRekening For Abn recurring payments IBAN: NL03TestIban MutatieSoort: Online bankieren", record);
    }

    private DescriptionConverter createConverter() {
        return new DescriptionConverter(new DescriptionFilterFunction(Set.of("Kenmerk", "Incassant ID", "Machtiging ID"), Set.of(IngConstants.NAAM_OMSCHRIJVING, IngConstants.NAAM, IngConstants.OMSCHRIJVING)));
    }

}