package bankproject.page_object_model;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import bankproject.model.WebUrl;
import bankproject.model.CustomerTransaction;
import bankproject.interaction.ui.Selenium;

public class CustomerTransactionsPage  extends CustomerPage {
     By backBtn = By.xpath("//button[text()='Back']");
     By resetBtn = By.xpath("//button[text()='Reset']");
     By startDatePicker = By.id("start");
     By endDatePicker = By.id("end");
     By transactionTable = By.tagName("table");

    public CustomerTransactionsPage(WebDriver driver)
    {
        this.driver = driver;
        this.url = WebUrl.CustomerTransaction;
        selenium = Selenium.Init(driver);
    }

    public void verifyPageIsActive()
    {
        boolean isDisplayed = selenium.waitUntil(transactionTable).visible().isDisplayed();

        assertThat(isDisplayed, is(true));
    }

    public CustomerAccountPage backToCustomerAccountPage()
    {
        selenium.waitUntil(backBtn).visible().click();

        return new CustomerAccountPage(driver);
    }

    public void reset()
    {
        selenium.waitUntil(resetBtn).visible().click();
    }

    public void selectDate(String startDate, String endDate)
    {
        selenium.waitUntil(startDatePicker).visible().clear();
        selenium.waitUntil(startDatePicker).visible().sendKeys(startDate);
        selenium.waitUntil(endDatePicker).visible().clear();
        selenium.waitUntil(endDatePicker).visible().sendKeys(endDate);
    }

    public void verifyInformation(CustomerTransaction customerTransaction)
    {
        List<CustomerTransaction> transactions = getTransactionInformations();
        
        assertThat(transactions, hasItem(customerTransaction));
    }

    public List<CustomerTransaction> getTransactionInformations()
    {
        List<CustomerTransaction> transactions = new ArrayList<CustomerTransaction>();
          List<List<String>> data = selenium.table(transactionTable).getTableData();
          for (List<String> row : data)
          {
              CustomerTransaction transaction = new CustomerTransaction();
              transaction.DateTime = row.get(0);
              transaction.Amount = Integer.valueOf(row.get(1));
              transaction.Type = row.get(2);

              transactions.add(transaction);
          }

        return transactions;
    }

    public void verifyNumberOfTransactions(int expectedNumber)
    {
        int numberOfTransaction = getTransactionInformations().size();

        assertThat(expectedNumber, is(numberOfTransaction));
    }

    public void VerifyLastCustomerTransaction(int amount, String type)
    {
        var transactions = getTransactionInformations();
        var lastTransaction = transactions.get(transactions.size() - 1);

        assertThat(lastTransaction.Amount, is(amount));
        assertThat(lastTransaction.Type, is(type));
    }
}
