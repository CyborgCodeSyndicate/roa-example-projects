package io.cyborgcode.ui.simple.test.framework;

import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.roa.ui.components.table.base.TableField;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ButtonFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.InputFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.LinkFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.ListFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.SelectFields;
import io.cyborgcode.ui.simple.test.framework.ui.elements.Tables;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.AllTransactionEntry;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.CreditAccounts;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.DetailedReport;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.FilteredTransactionEntry;
import io.cyborgcode.ui.simple.test.framework.ui.model.tables.OutFlow;
import io.qameta.allure.Description;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.cyborgcode.roa.ui.config.UiConfigHolder.getUiConfig;
import static io.cyborgcode.roa.ui.storage.DataExtractorsUi.tableRowExtractor;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.ALL_CELLS_CLICKABLE;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.ALL_CELLS_ENABLED;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.ALL_ROWS_ARE_UNIQUE;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.COLUMN_VALUES_ARE_UNIQUE;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.EVERY_ROW_CONTAINS_VALUES;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.NO_EMPTY_CELLS;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.ROW_CONTAINS_VALUES;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.ROW_NOT_EMPTY;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.TABLE_DATA_MATCHES_EXPECTED;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.TABLE_DOES_NOT_CONTAIN_ROW;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.TABLE_NOT_EMPTY;
import static io.cyborgcode.roa.ui.validator.TableAssertionTypes.TABLE_ROW_COUNT;
import static io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget.ROW_VALUES;
import static io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget.TABLE_ELEMENTS;
import static io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget.TABLE_VALUES;
import static io.cyborgcode.ui.simple.test.framework.base.Rings.RING_OF_UI;

@UI
class ZeroBankTableTest extends BaseQuest {


   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Table")
   @Regression
   void tableAssert(Quest quest) {
      List<List<String>> expectedTable = List.of(
            List.of("2012-09-06", "ONLINE TRANSFER REF #UKKSDRQG6L", "984.3", ""),
            List.of("2012-09-01", "ONLINE TRANSFER REF #UKKSDRQG6L", "1000", "")
      );

      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.ACCOUNT_ACTIVITY_LINK)
            .list().select(ListFields.ACCOUNT_ACTIVITY_TABS, "Find Transactions")
            .input().insert(InputFields.AA_DESCRIPTION_FIELD, "ONLINE")
            .input().insert(InputFields.AA_FROM_DATE_FIELD, "2012-01-01")
            .input().insert(InputFields.AA_TO_DATE_FIELD, "2012-12-31")
            .input().insert(InputFields.AA_FROM_AMOUNT_FIELD, "100")
            .input().insert(InputFields.AA_TO_AMOUNT_FIELD, "1000")
            .select().selectOption(SelectFields.AA_TYPE_DDL, "Deposit")
            .button().click(ButtonFields.FIND_SUBMIT_BUTTON)
            .table().readTable(Tables.FILTERED_TRANSACTIONS)
            .validate(() -> {
               Assertions.assertEquals(
                     "1000",
                     retrieve(tableRowExtractor(Tables.FILTERED_TRANSACTIONS, "2012-09-01"), FilteredTransactionEntry.class).getDeposit()
                           .getText(),
                     "Error Message");
            })
            .table().validate(
                  Tables.FILTERED_TRANSACTIONS,
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_ROW_COUNT).expected(2).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(EVERY_ROW_CONTAINS_VALUES).expected(List.of("ONLINE TRANSFER REF #UKKSDRQG6L")).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_DOES_NOT_CONTAIN_ROW).expected(List.of("random", "TEST", "222.2", "")).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(ALL_ROWS_ARE_UNIQUE).expected(true).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(NO_EMPTY_CELLS).expected(false).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(COLUMN_VALUES_ARE_UNIQUE).expected(1).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_DATA_MATCHES_EXPECTED).expected(expectedTable).soft(true).build(),
                  Assertion.builder().target(TABLE_ELEMENTS).type(ALL_CELLS_ENABLED).expected(true).soft(true).build(),
                  Assertion.builder().target(TABLE_ELEMENTS).type(ALL_CELLS_CLICKABLE).expected(true).soft(true).build())
            .table().readRow(Tables.FILTERED_TRANSACTIONS, 1)
            .table().validate(
                  Tables.FILTERED_TRANSACTIONS,
                  Assertion.builder().target(ROW_VALUES).type(ROW_NOT_EMPTY).expected(true).soft(true).build(),
                  Assertion.builder().target(ROW_VALUES).type(ROW_CONTAINS_VALUES).expected(List.of("2012-09-06", "ONLINE TRANSFER REF #UKKSDRQG6L")).soft(true).build())
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Table")
   @Regression
   void tableCertainCellsAssert(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.ACCOUNT_ACTIVITY_LINK)
            .list().select(ListFields.ACCOUNT_ACTIVITY_TABS, "Find Transactions")
            .list().validateIsSelected(ListFields.ACCOUNT_ACTIVITY_TABS, false, "Find Transactions")
            .input().insert(InputFields.AA_FROM_DATE_FIELD, "2012-01-01")
            .input().insert(InputFields.AA_TO_DATE_FIELD, "2012-12-31")
            .input().insert(InputFields.AA_FROM_AMOUNT_FIELD, "1")
            .input().insert(InputFields.AA_TO_AMOUNT_FIELD, "1000")
            .select().selectOption(SelectFields.AA_TYPE_DDL, "Any")
            .button().click(ButtonFields.FIND_SUBMIT_BUTTON)
            .table().readTable(Tables.FILTERED_TRANSACTIONS, TableField.of(FilteredTransactionEntry::setDescription),
                  TableField.of(FilteredTransactionEntry::setWithdrawal))
            .validate(() -> Assertions.assertEquals(
                  "50",
                  retrieve(tableRowExtractor(Tables.FILTERED_TRANSACTIONS, "OFFICE SUPPLY"),
                        FilteredTransactionEntry.class).getWithdrawal().getText(),
                  "Wrong deposit value")
            )
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Table")
   @Regression
   void tableFromToRowsAssert(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            .table().readTable(Tables.OUTFLOW, 3, 5)
            .validate(() -> Assertions.assertEquals(
                  "$375.55",
                  retrieve(tableRowExtractor(Tables.OUTFLOW, "Retail"),
                        OutFlow.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Table")
   @Regression
   void tableFromToRowsCertainCellsAssert(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            .table().readTable(Tables.OUTFLOW, 3, 5, TableField.of(OutFlow::setCategory),
                  TableField.of(OutFlow::setAmount))
            .validate(() -> Assertions.assertEquals(
                  "$375.55",
                  retrieve(tableRowExtractor(Tables.OUTFLOW, "Retail"),
                        OutFlow.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Table")
   @Regression
   void tableWithLinkComponentAssert(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.ACCOUNT_SUMMARY_LINK)
            .table().readTable(Tables.CREDIT_ACCOUNTS)
            .table().clickElementInCell(Tables.CREDIT_ACCOUNTS, 1, TableField.of(CreditAccounts::setAccount))
            .table().readTable(Tables.ALL_TRANSACTIONS)
            .validate(() -> Assertions.assertEquals(
                  "99.6",
                  retrieve(tableRowExtractor(Tables.ALL_TRANSACTIONS, "TELECOM"),
                        AllTransactionEntry.class).getWithdrawal().getText(),
                  "Wrong Balance")
            )
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Table")
   @Regression
   void tableWithButtonCustomServiceAssert(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            .table().readTable(Tables.OUTFLOW)
            .table().clickElementInCell(Tables.OUTFLOW, 4, TableField.of(OutFlow::setDetails))
            .table().readTable(Tables.DETAILED_REPORT)
            .validate(() -> Assertions.assertEquals(
                  "$105.00",
                  retrieve(tableRowExtractor(Tables.DETAILED_REPORT, "09/25/2012"),
                        DetailedReport.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Table")
   @Regression
   void tableWithButtonCustomServiceReadRowWithSearchCriteriaAssert(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            .table().readRow(Tables.OUTFLOW, List.of("Retail"))
            .validate(() -> Assertions.assertEquals(
                  "$375.55",
                  retrieve(tableRowExtractor(Tables.OUTFLOW),
                        OutFlow.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Table")
   @Regression
   void tableWithButtonCustomServiceClickElementWithSearchCriteriaAssert(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, "username")
            .input().insert(InputFields.PASSWORD_FIELD, "password")
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            .table().clickElementInCell(Tables.OUTFLOW, List.of("Checks Written", "$255.00"), TableField.of(OutFlow::setDetails))
            .table().readTable(Tables.DETAILED_REPORT)
            .validate(() -> Assertions.assertEquals(
                  "$105.00",
                  retrieve(tableRowExtractor(Tables.DETAILED_REPORT, "09/25/2012"),
                        DetailedReport.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

}
