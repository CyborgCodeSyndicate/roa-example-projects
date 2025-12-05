package io.cyborgcode.ui.simple.test.framework;

import io.cyborgcode.roa.framework.annotation.Craft;
import io.cyborgcode.roa.framework.annotation.Regression;
import io.cyborgcode.roa.framework.base.BaseQuest;
import io.cyborgcode.roa.framework.quest.Quest;
import io.cyborgcode.roa.ui.annotations.UI;
import io.cyborgcode.roa.ui.components.table.base.TableField;
import io.cyborgcode.roa.ui.validator.TableAssertionTypes;
import io.cyborgcode.roa.ui.validator.UiTablesAssertionTarget;
import io.cyborgcode.roa.validator.core.Assertion;
import io.cyborgcode.ui.simple.test.framework.data.creator.DataCreator;
import io.cyborgcode.ui.simple.test.framework.data.test_data.Data;
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
import org.junit.jupiter.api.DisplayName;
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
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.AppLinks.FIND_TRANSACTIONS;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Category.CHECKS_WRITTEN;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Category.RETAIL;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Category.TRANSPORTATION;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.OnlineTransfer.ONLINE_TRANSFERS_EXPECTED_TABLE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.OnlineTransfer.ONLINE_TRANSFER_REFERENCE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.OnlineTransfer.ROW_VALUES_NOT_CONTAINED;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.OnlineTransfer.TRANSFER_DATE_1;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_AMOUNT_1;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_AMOUNT_100;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_AMOUNT_1000;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_DESCRIPTION_OFFICE_SUPPLY;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_DESCRIPTION_ONLINE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_DESCRIPTION_TELECOM;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_FROM_DATE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_REPORT_DATE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_TO_DATE;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_TYPE_ANY;
import static io.cyborgcode.ui.simple.test.framework.data.test_data.Constants.Transactions.TRANSACTION_TYPE_DEPOSIT;

/**
 * Demonstrates usage of the Table component in the ROA UI framework for
 * reading and validating tabular data.
 *
 * <p>This class provides examples of how to:
 * <ul>
 *   <li>Read an entire table via {@code table().readTable(...)} and validate it with
 *       {@link TableAssertionTypes} and
 *       {@link UiTablesAssertionTarget}.</li>
 *   <li>Read specific rows by index or by search criteria using {@code table().readRow(...)}.</li>
 *   <li>Read subsets of rows (start/end index) and specific columns using {@link TableField} mappings.</li>
 *   <li>Interact with elements inside cells (e.g., click links/buttons) and then assert results.</li>
 *   <li>Validate table- and row-level values using fluent {@link Assertion}-based checks.</li>
 * </ul>
 *
 * <p>Examples target the Zero Bank demo app (via {@code getUiConfig().baseUrl()}),
 * but the patterns are app-agnostic and reusable across projects.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@UI
@DisplayName("Table Component: Read, Interact and Validate Examples")
class TableComponentExampleTests extends BaseQuest {

   @Test
   @Description("Read entire table and validate using table assertion types")
   @Regression
   void readEntireTable_validateWithAssertionTypes(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.ACCOUNT_ACTIVITY_LINK)
            .list().select(ListFields.ACCOUNT_ACTIVITY_TABS, FIND_TRANSACTIONS)
            .input().insert(InputFields.AA_DESCRIPTION_FIELD, TRANSACTION_DESCRIPTION_ONLINE)
            .input().insert(InputFields.AA_FROM_DATE_FIELD, TRANSACTION_FROM_DATE)
            .input().insert(InputFields.AA_TO_DATE_FIELD, TRANSACTION_TO_DATE)
            .input().insert(InputFields.AA_FROM_AMOUNT_FIELD, TRANSACTION_AMOUNT_100)
            .input().insert(InputFields.AA_TO_AMOUNT_FIELD, TRANSACTION_AMOUNT_1000)
            .select().selectOption(SelectFields.AA_TYPE_DDL, TRANSACTION_TYPE_DEPOSIT)
            .button().click(ButtonFields.FIND_SUBMIT_BUTTON)
            // table(): entry point for table component interactions (read/validate/click)
            // readTable(table): reads the entire table into the framework's storage for later assertions
            .table().readTable(Tables.FILTERED_TRANSACTIONS)
            // table().validate(): fluent assertions targeting table values/elements using TableAssertionTypes
            .table().validate(
                  Tables.FILTERED_TRANSACTIONS,
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_NOT_EMPTY).expected(true).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_ROW_COUNT).expected(2).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(EVERY_ROW_CONTAINS_VALUES).expected(List.of(ONLINE_TRANSFER_REFERENCE)).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_DOES_NOT_CONTAIN_ROW).expected(ROW_VALUES_NOT_CONTAINED).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(ALL_ROWS_ARE_UNIQUE).expected(true).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(NO_EMPTY_CELLS).expected(false).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(COLUMN_VALUES_ARE_UNIQUE).expected(1).soft(true).build(),
                  Assertion.builder().target(TABLE_VALUES).type(TABLE_DATA_MATCHES_EXPECTED).expected(ONLINE_TRANSFERS_EXPECTED_TABLE).soft(true).build(),
                  Assertion.builder().target(TABLE_ELEMENTS).type(ALL_CELLS_ENABLED).expected(true).soft(true).build(),
                  Assertion.builder().target(TABLE_ELEMENTS).type(ALL_CELLS_CLICKABLE).expected(true).soft(true).build())
            // readRow(): narrows context to a single row by index for row-level assertions
            .table().readRow(Tables.FILTERED_TRANSACTIONS, 1)
            .table().validate(
                  Tables.FILTERED_TRANSACTIONS,
                  Assertion.builder().target(ROW_VALUES).type(ROW_NOT_EMPTY).expected(true).soft(true).build(),
                  Assertion.builder().target(ROW_VALUES).type(ROW_CONTAINS_VALUES).expected(List.of(TRANSFER_DATE_1, ONLINE_TRANSFER_REFERENCE)).soft(true).build())
            .complete();
   }

   @Test
   @Description("Read table with specific columns and validate target cell value")
   @Regression
   void readTableWithSpecifiedColumns_validateCell(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.ACCOUNT_ACTIVITY_LINK)
            .list().select(ListFields.ACCOUNT_ACTIVITY_TABS, FIND_TRANSACTIONS)
            .list().validateIsSelected(ListFields.ACCOUNT_ACTIVITY_TABS, false, FIND_TRANSACTIONS)
            .input().insert(InputFields.AA_FROM_DATE_FIELD, TRANSACTION_FROM_DATE)
            .input().insert(InputFields.AA_TO_DATE_FIELD, TRANSACTION_TO_DATE)
            .input().insert(InputFields.AA_FROM_AMOUNT_FIELD, TRANSACTION_AMOUNT_1)
            .input().insert(InputFields.AA_TO_AMOUNT_FIELD, TRANSACTION_AMOUNT_1000)
            .select().selectOption(SelectFields.AA_TYPE_DDL, TRANSACTION_TYPE_ANY)
            .button().click(ButtonFields.FIND_SUBMIT_BUTTON)
            // readTable(table, columns...): reads specific columns from the table into the framework's storage
            .table().readTable(Tables.FILTERED_TRANSACTIONS, TableField.of(FilteredTransactionEntry::setDescription),
                  TableField.of(FilteredTransactionEntry::setWithdrawal))
            // validate(): uses a lambda for arbitrary assertions when a built-in assertion type isn't suitable
            .validate(() -> Assertions.assertEquals(
                  "50",
                  retrieve(tableRowExtractor(Tables.FILTERED_TRANSACTIONS, TRANSACTION_DESCRIPTION_OFFICE_SUPPLY),
                        FilteredTransactionEntry.class).getWithdrawal().getText(),
                  "Wrong deposit value")
            )
            .complete();
   }

   @Test
   @Description("Read table with start/end row range and validate target cell value")
   @Regression
   void readTableWithRowRange_validateCell(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // readTable(table, start, end): reads a subset of rows (inclusive indices)
            .table().readTable(Tables.OUTFLOW, 3, 5)
            .validate(() -> Assertions.assertEquals(
                  "$375.55",
                  retrieve(tableRowExtractor(Tables.OUTFLOW, RETAIL),
                        OutFlow.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("Read specific columns within row range and validate target cell value")
   @Regression
   void readTableSpecificColumnsWithRowRange_validateCell(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // readTable(table, start, end, columns...): subset of rows with specific mapped columns
            .table().readTable(Tables.OUTFLOW, 3, 5, TableField.of(OutFlow::setCategory),
                  TableField.of(OutFlow::setAmount))
            .validate(() -> Assertions.assertEquals(
                  "$375.55",
                  retrieve(tableRowExtractor(Tables.OUTFLOW, RETAIL),
                        OutFlow.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("Read a table row by search criteria and validate target cell value")
   @Regression
   void readTableRowBySearchCriteria_validateCell(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // readRow(table, criteria): reads a row matching the provided search values
            .table().readRow(Tables.OUTFLOW, List.of(RETAIL))
            .validate(() -> Assertions.assertEquals(
                  "$375.55",
                  retrieve(tableRowExtractor(Tables.OUTFLOW),
                        OutFlow.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("Read a specific table row with specific columns and validate target cell value")
   @Regression
   void readTableRowWithSpecifiedColumns_validateCell(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // readRow(table, row, columns...): reads a specific row with only the specified columns
            .table().readRow(Tables.OUTFLOW, 1, TableField.of(OutFlow::setCategory),
                  TableField.of(OutFlow::setAmount))
            .validate(() -> Assertions.assertEquals(
                  "$160.00",
                  retrieve(tableRowExtractor(Tables.OUTFLOW, TRANSPORTATION),
                        OutFlow.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("Read a table row by search criteria with specified columns and validate target cell value")
   @Regression
   void readTableRowByCriteriaWithSpecifiedColumns_validateCell(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // readRow(table, criteria, columns...): reads by search criteria and map the specified columns
            .table().readRow(Tables.OUTFLOW, List.of(RETAIL), TableField.of(OutFlow::setCategory),
                  TableField.of(OutFlow::setAmount))
            .validate(() -> Assertions.assertEquals(
                  "$375.55",
                  retrieve(tableRowExtractor(Tables.OUTFLOW, RETAIL),
                        OutFlow.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("Click a link inside a cell found by row using cell insertion interface")
   @Regression
   void clickLinkInCertainCell_usingCellInsertion(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.ACCOUNT_SUMMARY_LINK)
            // clickElementInCell(table, row, column): clicks an element inside a cell resolved by TableField mapping
            .table().clickElementInCell(Tables.CREDIT_ACCOUNTS, 1, TableField.of(CreditAccounts::setAccount))
            .table().readTable(Tables.ALL_TRANSACTIONS)
            .validate(() -> Assertions.assertEquals(
                  "99.6",
                  retrieve(tableRowExtractor(Tables.ALL_TRANSACTIONS, TRANSACTION_DESCRIPTION_TELECOM),
                        AllTransactionEntry.class).getWithdrawal().getText(),
                  "Wrong Balance")
            )
            .complete();
   }

   @Test
   @Description("Click a button inside a cell found by row using custom insertion interface")
   @Regression
   void clickButtonInCertainCell_usingCustomInsertion(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // clickElementInCell(table, row, column): clicks an element inside a cell resolved by TableField mapping
            .table().clickElementInCell(Tables.OUTFLOW, 4, TableField.of(OutFlow::setDetails))
            .table().readTable(Tables.DETAILED_REPORT)
            .validate(() -> Assertions.assertEquals(
                  "$105.00",
                  retrieve(tableRowExtractor(Tables.DETAILED_REPORT, TRANSACTION_REPORT_DATE),
                        DetailedReport.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("Click a button inside a cell found by search criteria using custom insertion interface")
   @Regression
   void clickButtonInCellFoundByCriteria_usingCustomInsertion(Quest quest) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // clickElementInCell(table, criteria, column): locates the row by criteria and click inside the mapped column
            .table().clickElementInCell(Tables.OUTFLOW, List.of(CHECKS_WRITTEN), TableField.of(OutFlow::setDetails))
            .table().readTable(Tables.DETAILED_REPORT)
            .validate(() -> Assertions.assertEquals(
                  "$105.00",
                  retrieve(tableRowExtractor(Tables.DETAILED_REPORT, TRANSACTION_REPORT_DATE),
                        DetailedReport.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("Click a button inside a cell found by row using data object")
   @Regression
   void clickButtonInCertainCell_usingDataObject(
         Quest quest,
         @Craft(model = DataCreator.Data.OUTFLOW_DATA) OutFlow outFlowDetails) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // clickElementInCell(table, row, model): locates the row, clicks an element inside a cell
            // resolved by the data model
            .table().clickElementInCell(Tables.OUTFLOW, 4, outFlowDetails)
            .table().readTable(Tables.DETAILED_REPORT)
            .validate(() -> Assertions.assertEquals(
                  "$105.00",
                  retrieve(tableRowExtractor(Tables.DETAILED_REPORT, TRANSACTION_REPORT_DATE),
                        DetailedReport.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

   @Test
   @Description("Click a button inside a cell found by search criteria using data object")
   @Regression
   void clickButtonInCellFoundByCriteria_usingDataObject(
         Quest quest,
         @Craft(model = DataCreator.Data.OUTFLOW_DATA) OutFlow outFlowDetails) {
      quest
            .use(RING_OF_UI)
            .browser().navigate(getUiConfig().baseUrl())
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.USERNAME_FIELD, Data.testData().username())
            .input().insert(InputFields.PASSWORD_FIELD, Data.testData().password())
            .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
            .browser().back()
            .button().click(ButtonFields.MORE_SERVICES_BUTTON)
            .link().click(LinkFields.MY_MONEY_MAP_LINK)
            // clickElementInCell(table, criteria, model): locates the row by search criteria and clicks an element
            // inside a cell resolved by the data model
            .table().clickElementInCell(Tables.OUTFLOW, List.of(CHECKS_WRITTEN), outFlowDetails)
            .table().readTable(Tables.DETAILED_REPORT)
            .validate(() -> Assertions.assertEquals(
                  "$105.00",
                  retrieve(tableRowExtractor(Tables.DETAILED_REPORT, TRANSACTION_REPORT_DATE),
                        DetailedReport.class).getAmount().getText(),
                  "Wrong Amount")
            )
            .complete();
   }

}
