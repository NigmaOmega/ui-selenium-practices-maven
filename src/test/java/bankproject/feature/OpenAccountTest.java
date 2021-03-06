package bankproject.feature;

import org.junit.Before;
import org.junit.Test;

import bankproject.model.BankConstants;
import bankproject.WebHook;
import bankproject.page_object_model.CustomerAccountPage;
import bankproject.page_object_model.CustomerLoginPage;
import bankproject.page_object_model.HomePage;
import bankproject.page_object_model.OpenAccountPage;

public class OpenAccountTest extends WebHook {
    OpenAccountPage openAccountPage;

    String validationMessage = "Please select an item in the list.";


    @Before
    public void classSetUp() {
        openAccountPage = new OpenAccountPage(driver);
        openAccountPage.goTo();
    }

    @Test
    public void should_be_able_to_create_new_account() {
        openAccountPage.openAccount(BankConstants.CustomerAccountValid.CustomerName, BankConstants.CustomerAccountValid.Currency);
        String newAccountNumber = openAccountPage.getAccountNumberIsCreatedInAlert();
        openAccountPage.verifyAccountIsOpenedAndCloseAlert();

        HomePage homePage = new HomePage(driver);
        homePage.goTo();
        CustomerLoginPage customerLoginPage = homePage.customerLogin();
        CustomerAccountPage customerAccountPage = customerLoginPage.login(BankConstants.CustomerAccountValid.CustomerName);
        customerAccountPage.verifyCutomerHaveAccount(newAccountNumber);
    }


    @Test
    public void should_verify_the_not_selected_customer_name()
    {
        openAccountPage.selectCurrency(BankConstants.CustomerAccountValid.Currency);
        openAccountPage.ClickOnProcessButton();
        openAccountPage.verifyCustomerNameValidationMessage(validationMessage);
    }

    @Test
    public void should_verify_the_not_selected_currency()
    {
        openAccountPage.selectCustomer(BankConstants.CustomerAccountValid.CustomerName);
        openAccountPage.ClickOnProcessButton();
        openAccountPage.verifyCurrencyValidationMessage(validationMessage);
    }
}
