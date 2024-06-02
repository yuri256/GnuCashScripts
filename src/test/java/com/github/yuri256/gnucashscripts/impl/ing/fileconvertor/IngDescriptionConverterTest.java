package com.github.yuri256.gnucashscripts.impl.ing.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.ing.model.DebitCredit;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class IngDescriptionConverterTest {
    @Test
    public void testTerminalPayment() {
        IngRecord record = new IngRecord("20010304", "Test Name-Desc", "TestRekening", "TestTegenRekening", "BA", DebitCredit.Debit, "12,34", "Payment terminal", "Card sequence no.: 001 07-03-2020 09:34 Transaction: A111B2 Term: AB1234 Value date: 07-03-2020", "");
        String description = createConverter().apply(record);
        Assertions.assertEquals("Test Name-Desc DateTime: 07-03-2020 09:34 TransactionType: Payment terminal", description);
    }

    @Test
    public void testCashMachineWithdrawal() {
        String description = createConverter().apply(new IngRecord("20010304", "Test Name-Desc", "TestRekening", "TestTegenRekening", "BA", DebitCredit.Debit, "12,34", "Cash machine", "Card sequence no.: 001 07-03-2020 09:34 Transaction: A111B2 Term: AB1234 Value date: 07-03-2020", ""));
        Assertions.assertEquals("Test Name-Desc DateTime: 07-03-2020 09:34 TransactionType: Cash machine", description);
    }

    @Test
    public void testInternalTransfer() {
        String description = createConverter().apply(new IngRecord("20010304", "Hr J DOE", "NL03TestRekening", "", "GT", DebitCredit.Credit, "12,34", "Online Banking", "Van Spaarrekening 123456789 the description Value date: 12-03-2020", ""));
        Assertions.assertEquals("Hr J DOE Van Spaarrekening 123456789 the description TransactionType: Online Banking", description);
    }

    @Test
    public void testShortPassvolgNr() {
        String description = createConverter().apply(new IngRecord("20010304", "Hr J DOE", "NL03TestRekening", "", "GT", DebitCredit.Credit, "12,34", "Payment terminal", "Card sequence no.: 00  27-02-2022 11:07 Transaction: 05MABC Term: C108 Valuta: 492,38 SEK Koers: 0,0960555 Opslag: 0,66 EUR Value date: 18-08-2021", ""));
        Assertions.assertEquals("Hr J DOE DateTime: 27-02-2022 11:07 Valuta: 492,38 SEK Koers: 0,0960555 Opslag: 0,66 EUR TransactionType: Payment terminal", description);
    }

    @Test
    public void testSepaDirectDebit() {
        String description = createConverter().apply(new IngRecord("20010304", "The Company Name", "TestRekening", "TheCompanyRekening", "IC", DebitCredit.Debit, "12,34", "SEPA direct debit", "Name: TheCompany Description: Fact 0001 desc IBAN: NL18TestIban2 Reference: 12345678 Mandate ID: 1234567 Creditor ID: 123456 Doorlopende incasso Value date: 06-03-2020", ""));
        Assertions.assertEquals("TheCompany Fact 0001 desc IBAN: NL18TestIban2 TransactionType: SEPA direct debit", description);
    }

    @Test
    public void testSepaDirectDebitCreditCard() {
        String description = createConverter().apply(new IngRecord("20010304", "INCASSO CREDITCARD ACCOUNTNR XXX", "TestRekening", "", "DV", DebitCredit.Debit, "12,34", "Various", " ZIE REKENINGOVERZICHT 01-01-2020 Value date: 02-01-2020", ""));
        Assertions.assertEquals("INCASSO CREDITCARD ACCOUNTNR XXX ZIE REKENINGOVERZICHT 01-01-2020 TransactionType: Various", description);
    }

    @Test
    public void testOnlinePurchase() {
        String description = createConverter().apply(new IngRecord("20010304", "Merchant1", "TestRekening", "TestTegenRekening", "GT", DebitCredit.Debit, "12,34", "Online Banking", "Name: Merchant1 Description: REF1 ID1 Order ORDER1 DESC1 IBAN: TestTegenRekening Reference: 04-03-2020 08:21 KENMERK1 Value date: 04-03-2020", ""));
        Assertions.assertEquals("Merchant1 REF1 ID1 Order ORDER1 DESC1 IBAN: TestTegenRekening TransactionType: Online Banking", description);
    }

    @Test
    public void testPaymentRequest() {
        String description = createConverter().apply(new IngRecord("20010304", "ING Bank NV Betaalverzoek", "TestRekening", "TestTegenRekening", "GT", DebitCredit.Credit, "12,34", "Online Banking", "Name: ING Bank NV Betaalverzoek Description: Betaling van J DOE NL03TestIban the description IBAN: NL03TestIban2 Reference: theKenmerk Value date: 13-03-2020", ""));
        Assertions.assertEquals("ING Bank NV Betaalverzoek Betaling van J DOE NL03TestIban the description IBAN: NL03TestIban2 TransactionType: Online Banking", description);
    }

    @Test
    public void testInboundBankTransfer() {
        String description = createConverter().apply(new IngRecord("20010304", "J DOE", "TestRekening", "TestTegenRekening", "OV", DebitCredit.Credit, "12,34", "Transfer", "Name: J DOE IBAN: NL03TestIban DateTime: 01-01-2020 12:34:56 Value date: 01-01-2020", ""));
        Assertions.assertEquals("J DOE IBAN: NL03TestIban DateTime: 01-01-2020 12:34 TransactionType: Transfer", description);
    }

    @Test
    public void testOutboundBankTransfer() {
        String description = createConverter().apply(new IngRecord("20010304", "J DOE", "TestRekening", "TestTegenRekening", "GT", DebitCredit.Debit, "12,34", "Online Banking", "Name: J DOE IBAN: TestRekening Description: For Abn recurring payments IBAN: NL03TestIban Value date: 01-01-2020", ""));
        Assertions.assertEquals("J DOE IBAN: TestRekening For Abn recurring payments IBAN: NL03TestIban TransactionType: Online Banking", description);
    }

    @Test
    public void testTag() {
        IngRecord record = new IngRecord("20010304", "Test Name-Desc", "TestRekening", "TestTegenRekening", "BA", DebitCredit.Debit, "12,34", "Payment terminal", "Card sequence no.: 001 07-03-2020 09:34 Transaction: A111B2 Term: AB1234 Value date: 07-03-2020", "tag1");
        String description = createConverter().apply(record);
        Assertions.assertEquals("Test Name-Desc DateTime: 07-03-2020 09:34 TransactionType: Payment terminal Tag: tag1", description);
    }

    private IngDescriptionConverter createConverter() {
        Set<String> removeFieldKeys = Set.of("Reference", "CreditorID", "MandateID", "Counterparty", "ValueDate", "Term", "Transaction", "CardSequenceNo.");
        Set<String> removeKeyKeys = Set.of("NameDescription", "Name", "Description");
        DescriptionFilterFunction filterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        return new IngDescriptionConverter(filterFunction);
    }

}