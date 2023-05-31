package epic_3_credits.us_3_3_applyingForLoan;

import baseTest.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import pages.logged_in_pages.UserMainPage;
import pages.logged_in_pages.credit_pages.LoanProductsInfoPage;
import pages.logged_in_pages.credit_pages.LoanProductsOrderPage;
import pages.logged_in_pages.credit_pages.LoanProductsPage;
import pages.logging_in_pages.AuthorizationForm;
import pages.most_common_page_elements.Sidebar;

import static constants.TestConstants.DISPLAYED;
import static constants.TestConstants.DOES_NOT_MATCH_WITH_EXPECTED;
import static constants.TestConstants.EMPLOYER_IDENTIFICATION_NUMBER;
import static constants.TestConstants.INCORRECT_EMPLOYER_IDENTIFICATION_NUMBER;
import static constants.TestConstants.INCORRECT_LOAN_AMOUNT;
import static constants.TestConstants.INCORRECT_MONTHLY_EXPENDITURE;
import static constants.TestConstants.INCORRECT_MONTHLY_INCOME;
import static constants.TestConstants.LOAN_AMOUNT;
import static constants.TestConstants.MONTHLY_EXPENDITURE;
import static constants.TestConstants.MONTHLY_INCOME;
import static constants.TestConstants.NOT_ACTIVE;
import static constants.TestConstants.NOT_DISPLAYED;
import static constants.TestConstants.PASSPORT_NUMBER_USER_WITH_CREDITS;
import static constants.TestConstants.PASSWORD_USER_WITH_CREDITS;
import static constants.TestConstants.PERIOD_MONTHS;
import static constants.TestConstants.TEXT_ORDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Epic-3 Кредиты (WEB)")
@Story("US-3.3 Оформление заявки на кредит")

public class ApplyingForLoanTest extends BaseTest {

    private static AuthorizationForm authorizationForm;
    private static UserMainPage userMainPage;
    private static Sidebar sidebar;
    private static LoanProductsPage loanProductsPage;
    private static LoanProductsInfoPage loanProductsInfoPage;
    private static LoanProductsOrderPage loanProductsOrderPage;

    @BeforeAll
    public static void setBeforeAll() {
        WebDriver driver = driverActions.getDriver();
        authorizationForm = new AuthorizationForm(driver);
        userMainPage = new UserMainPage(driver);
        loanProductsPage = new LoanProductsPage(driver);
        loanProductsInfoPage = new LoanProductsInfoPage(driver);
        loanProductsOrderPage = new LoanProductsOrderPage(driver);
    }

    @BeforeEach
    public void setBeforeEach(){
        authorizationForm.openSwagger();
        authorizationForm.startAFinny();
        authorizationForm.byPassportButtonClick();
        authorizationForm.passportFieldSendKeys(PASSPORT_NUMBER_USER_WITH_CREDITS);
        authorizationForm.passwordFieldSendKeys(PASSWORD_USER_WITH_CREDITS);
        authorizationForm.signInButtonActiveClick();
        sidebar = userMainPage.getSidebar();
        sidebar.clickCreditsButton();
        loanProductsPage.loanProductsButtonClick();
        loanProductsInfoPage.makeApplyButtonClick();
    }

    @Test
    @DisplayName("C6168317 Обязательность заполнения полей при заявке на кредит")
    public void emptyFieldsLoanTest() {
        loanProductsOrderPage.loanAmountDelete();
        loanProductsOrderPage.periodMonthsDelete();
        assertTrue(loanProductsPage.isApplyLoanButtonActive(),
                "Кнопка 'Заказать'" + NOT_ACTIVE);
    }

    @Test
    @DisplayName("C6168325 Возможность закрыть окно при успешной подаче заявки")
    public void loanProductsOrderCloseTest(){
        loanProductsOrderPage.loanAmountDelete();
        loanProductsOrderPage.loanAmountFieldSendKeys(LOAN_AMOUNT);
        loanProductsOrderPage.periodMonthsDelete();
        loanProductsOrderPage.periodMonthsFieldSenKeys(PERIOD_MONTHS);
        loanProductsOrderPage.monthlyIncomeFieldSendKeys(MONTHLY_INCOME);
        loanProductsOrderPage.monthlyExpenditureFieldSendKeys(MONTHLY_EXPENDITURE);
        loanProductsOrderPage.employerIdentificationNumberFieldSendKeys(EMPLOYER_IDENTIFICATION_NUMBER);
        loanProductsOrderPage.submitButtonClick();
        assertTrue(loanProductsOrderPage.isOrderWindowDisplayed(),
                "Окно заявки" + NOT_DISPLAYED);
        loanProductsOrderPage.orderWindowClose();
        assertTrue(loanProductsOrderPage.isOrderWindowDisplayed(),
                "Окно заявки" + DISPLAYED);
    }

    @Test
    @DisplayName("C6168401 Оформление заявки на кредитный продукт банка ")
    public void loanProductsOrderApplyTest() {
        String product_id = "2";
        loanProductsOrderPage.loanAmountDelete();
        loanProductsOrderPage.loanAmountFieldSendKeys(LOAN_AMOUNT);
        loanProductsOrderPage.periodMonthsDelete();
        loanProductsOrderPage.periodMonthsFieldSenKeys(PERIOD_MONTHS);
        loanProductsOrderPage.monthlyIncomeFieldSendKeys(MONTHLY_INCOME);
        loanProductsOrderPage.monthlyExpenditureFieldSendKeys(MONTHLY_EXPENDITURE);
        loanProductsOrderPage.employerIdentificationNumberFieldSendKeys(EMPLOYER_IDENTIFICATION_NUMBER);
        loanProductsOrderPage.submitButtonClick();
        assertTrue(loanProductsOrderPage.getTextOrder().contains(TEXT_ORDER),
                "Текст не сопадает");
        assertEquals(product_id,databaseHandler.getIdProductByClientEIN(EMPLOYER_IDENTIFICATION_NUMBER),
                   "Product_id" + DOES_NOT_MATCH_WITH_EXPECTED);
        databaseHandler.deleteLoanProductFromBase(EMPLOYER_IDENTIFICATION_NUMBER);
    }

    @Test
    @DisplayName("C6165251 Возможность оформления заявки на конкретный продукт банка с неверными данными")
    public void loanProductsApplyCheckTest(){
        loanProductsOrderPage.loanAmountDelete();
        loanProductsOrderPage.loanAmountFieldSendKeys(INCORRECT_LOAN_AMOUNT);
        loanProductsOrderPage.periodMonthsDelete();
        loanProductsOrderPage.periodMonthsFieldSenKeys(INCORRECT_LOAN_AMOUNT);
        loanProductsOrderPage.monthlyIncomeFieldSendKeys(INCORRECT_MONTHLY_INCOME);
        loanProductsOrderPage.monthlyExpenditureFieldSendKeys(INCORRECT_MONTHLY_EXPENDITURE);
        loanProductsOrderPage.employerIdentificationNumberFieldSendKeys(INCORRECT_EMPLOYER_IDENTIFICATION_NUMBER);
        assertTrue(loanProductsOrderPage.isHelperTextDisplayed(),
                "Не подсвечиваются поля с подсказками");
    }

   @AfterEach
    public void getAfterEach() {
        userMainPage.exitButtonClick();
    }

    @AfterAll
    public static void closeDriver(){
        driverActions.quitDriver();
    }
}
