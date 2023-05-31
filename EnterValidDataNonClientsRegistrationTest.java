package epic_1_registrationAuthorizationSecurity.us_1_1_registration.us_1_1_5_registrationForNonBankClients.step_3_enterData;

import baseTest.BaseTest;
import data.Country;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static constants.ErrorMessages.EXPECTED_PAGE_DID_NOT_OPEN;
import static constants.ErrorMessages.VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.DataGenerator.generatePhoneNumber;

@Epic("Epic-1 Регистрация/Авторизация/Безопасность")
@Feature("US-1.1 Регистрация")
@Story("US-1.1.5 Регистрация (для не клиентов банка)")
public class EnterValidDataNonClientsRegistrationTest extends BaseTest {

    private static final String PHONE = generatePhoneNumber(Country.RU);

    @BeforeAll
    public static void goToEnterDataPage() {
        mainPage.clickOnSignUpButton();
        enterPhoneForRegistrationPage.clickOnSkipButton();
        enterPhoneForRegistrationPage.enterPhoneNumber(PHONE);
        enterPhoneForRegistrationPage.clickOnPrimaryButton();
        enterSMSCodePage.enterSMSCode(databaseHandler.getSMSCode(PHONE));
        enterSMSCodePage.clickOnPrimaryButton();
    }


    @ParameterizedTest
    @DisplayName("C5865052, C5867368 Проверка ввода валидных значений на странице " +
            "'Заполните следующую информацию' (для не клиентов банка)")
    @CsvSource(value = {
            "Елизавета, NG34567893", "Elizabeth, 178181mp",
            "эд, 7", "ed, d", "АННА, D", "JAKE, DHLT",
            "15Анна, 765N687N9099N", "hannah15, n7656n879n099",
            "дав1д, 1 dhlt", "DAV1D, 1 DHLT 5",
            "0123456789, dhlt 1", "ЕЛИЗАBETH, NG34567893"})
    public void validDataTest(String value, String passportNumber) {
        enterDataPage.fillInForm(value, value, value, passportNumber);
        assertAll("Проверка формы 'Заполните следующую информацию' после её заполнения " +
                        "валидными значениями",
                () -> assertEquals(value, enterDataPage.getFirstNameInputText(),
                        VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED),
                () -> assertEquals(value, enterDataPage.getLastNameInputText(),
                        VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED),
                () -> assertEquals(value, enterDataPage.getMiddleNameInputText(),
                        VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED),
                () -> assertEquals(passportNumber, enterDataPage.getPassportNumberInputText(),
                        VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED)
        );
        enterDataPage.scrollForm(0.5, 0.5, 0.5, 0.2);
        enterDataPage.clickOnResidentRadioButton();
        enterDataPage.clickOnPrimaryButton();
        assertNotNull(setPasswordPage.getSetPasswordTitleText(), EXPECTED_PAGE_DID_NOT_OPEN);
    }

    @AfterEach
    public void backToPreviousScreen() {
        setPasswordPage.clickOnDeviceBackButton();
        enterDataPage.clearForm();
    }
}