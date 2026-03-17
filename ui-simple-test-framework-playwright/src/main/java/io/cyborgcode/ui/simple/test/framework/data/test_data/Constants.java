package io.cyborgcode.ui.simple.test.framework.data.test_data;

import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * Canonical test data for the Zero Bank demo UI application.
 *
 * <p>This class centralizes stable labels and expected values used across UI tests
 *
 * <p>All constants map directly to the Zero Bank demo site and serve as a single source of truth for
 * assertions to keep tests readable and maintainable.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UtilityClass
public class Constants {

   public static final class AppLinks {
      private AppLinks() {
      }

      public static final String FIND_TRANSACTIONS = "Find Transactions";
      public static final String PAY_BILLS = "Pay Bills";
      public static final String PURCHASE_FOREIGN_CURRENCY = "Purchase Foreign Currency";
      public static final String PAY_SAVED_PAYEE = "Pay Saved Payee";
   }

   public static final class OnlineTransfer {
      private OnlineTransfer() {
      }

      public static final String ONLINE_TRANSFER_REFERENCE = "ONLINE TRANSFER REF #UKKSDRQG6L";
      public static final String TRANSFER_DATE_1 = "2012-09-06";
      private static final String TRANSFER_DATE_2 = "2012-09-01";
      private static final String TRANSFER_SUM_1 = "984.3";
      private static final String TRANSFER_SUM_2 = "1000";
      private static final String EMPTY_VALUE = "";

      public static final List<List<String>> ONLINE_TRANSFERS_EXPECTED_TABLE = List.of(
            List.of(TRANSFER_DATE_1, ONLINE_TRANSFER_REFERENCE, TRANSFER_SUM_1, EMPTY_VALUE),
            List.of(TRANSFER_DATE_2, ONLINE_TRANSFER_REFERENCE, TRANSFER_SUM_2, EMPTY_VALUE)
      );

      public static final List<String> ROW_VALUES_NOT_CONTAINED =
            List.of("random", "TEST", "222.2", EMPTY_VALUE);
   }

   public static final class Transactions {
      private Transactions() {
      }

      public static final String TRANSACTION_DESCRIPTION_ONLINE = "ONLINE";
      public static final String TRANSACTION_DESCRIPTION_OFFICE_SUPPLY = "OFFICE SUPPLY";
      public static final String TRANSACTION_DESCRIPTION_TELECOM = "TELECOM";
      public static final String TRANSACTION_FROM_DATE = "2012-01-01";
      public static final String TRANSACTION_TO_DATE = "2012-12-31";
      public static final String TRANSACTION_AMOUNT_1 = "1";
      public static final String TRANSACTION_AMOUNT_100 = "100";
      public static final String TRANSACTION_AMOUNT_1000 = "1000";
      public static final String TRANSACTION_TYPE_DEPOSIT = "Deposit";
      public static final String TRANSACTION_TYPE_ANY = "Any";
      public static final String TRANSACTION_REPORT_DATE = "09/25/2012";
   }

   public static final class Category {
      private Category() {
      }

      public static final String RETAIL = "Retail";
      public static final String TRANSPORTATION = "Transportation";
      public static final String CHECKS_WRITTEN = "Checks Written";
   }

   public static final class Purchase {
      private Purchase() {
      }

      public static final String CURRENCY_PESO = "Mexico (peso)";
      public static final String SUCCESSFUL_PURCHASE_MESSAGE = "Foreign currency cash was successfully purchased.";
   }

   public static final class TransferFunds {
      private TransferFunds() {
      }

      public static final String LOAN_ACCOUNT = "Loan(Avail. balance = $ 780)";
      public static final String CREDIT_CARD_ACCOUNT = "Credit Card(Avail. balance = $ -265)";
      public static final String SAVINGS_ACCOUNT = "Savings(Avail. balance = $ 1000)";
      public static final String CHECKING_ACCOUNT = "Checking(Avail. balance = $ -500.2)";
      public static final String BROKERAGE_ACCOUNT = "Brokerage(Avail. balance = $ 197)";
      public static final String TRANSFER_LOAN_TO_CREDIT_CARD = "Transfer Amount from Loan to Credit Card";
      public static final String TRANSFER_SAVING_TO_CHECKING = "Transfer Amount from Savings to Checking";
      public static final String TRANSFER_BROKERAGE_TO_CHECKING = "Transfer Amount from Brokerage to Checking";
      public static final String SUCCESSFUL_TRANSFER_MESSAGE = "You successfully submitted your transaction.";
   }

   public static final class PayBills {
      private PayBills() {
      }

      public static final String PAYEE_SPRINT = "Sprint";
      public static final String PAYEE_SPRINT_PLACEHOLDER = "For 12119415161214 Sprint account";
      public static final String PAYEE_BANK_OF_AMERICA = "Bank of America";
      public static final String PAYEE_BANK_OF_AMERICA_PLACEHOLDER = "For 47844181491040 Bank of America account";
      public static final String PAYEE_APPLE = "Apple";
      public static final String PAYEE_APPLE_PLACEHOLDER = "For 48944145651315 Apple account";
      public static final String PAYEE_WELLS_FARGO = "Wells Fargo";
      public static final String PAYEE_WELLS_FARGO_PLACEHOLDER = "For 98480498848942 Wells Fargo account";
      public static final String PAYEE_CHECKING_ACCOUNT = "Checking";
      public static final String PAYEE_AMOUNT = "1000";
      public static final String PAYEE_DATE = "2025-01-27";
      public static final String PAYEE_DESCRIPTION = "Description Example";
      public static final String SUCCESSFUL_PAYMENT_MESSAGE = "The payment was successfully submitted.";

   }


}
