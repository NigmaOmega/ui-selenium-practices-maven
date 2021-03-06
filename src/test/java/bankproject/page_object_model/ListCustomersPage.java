package bankproject.page_object_model;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import bankproject.model.WebUrl;
import bankproject.model.Customer;
import bankproject.interaction.ui.Selenium;

public class ListCustomersPage extends BasePage {
    By searchCustomerInput = By.xpath("//intpu[@ng-model='searchCustomer']");
    By customerTable = By.tagName("table");

    public ListCustomersPage(WebDriver driver) {
        this.driver = driver;
        this.url = WebUrl.ListCustomers;
        this.selenium = Selenium.Init(driver);
    }


    By deleteCustomerLocator(String accountId) {
        By deleteBtn = By.xpath("//span[contains(.,'" + accountId + "')]/../..//button");
        return deleteBtn;
    }


    public void searchCustomer(String value) {
        selenium.waitUntil(searchCustomerInput).visible().sendKeys(value);
    }

    public List<Customer> getCustomerInformation()
        {
            List<Customer> result = new ArrayList<Customer>();
            var items = selenium.table(customerTable).getTableData();
             for (var item : items)
             {
                 String firstName = item.get(0);
                 String lastName = item.get(1);
                 String postCode = item.get(2);
                 List<String> accountNumbers = Arrays.asList(item.get(3).split(" "));;
                 Customer customer = new Customer(firstName, lastName, postCode, accountNumbers);
                 result.add(customer);
             }
            return result;
        }

    public void verifyListCustomerTable(int numberOfItem) {
        int currentNumberOfItem = getCustomerInformation().size();

        assertThat(currentNumberOfItem, is(numberOfItem));
    }

    public void verifyCustomerIsExisted(Customer customer) {
        List<Customer> customers = getCustomerInformation();
        var isContains = customers.stream().anyMatch(p -> p.FirstName.equals(customer.FirstName) && p.LastName.equals(customer.LastName) && p.PostCode.equals(customer.PostCode));
        assertThat(isContains,is(true));
    }

    public void deleteCustomer(String accountId) {
        selenium.waitUntil(deleteCustomerLocator(accountId)).visible().click();
    }
}
