package epic_1_registrationAuthorizationSecurity.us_1_1_registration.us_1_1_5_registrationForNonBankClients.step_3_enterData;

import baseTest.BaseTest;
import data.Country;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static constants.ErrorMessages.BACK_BUTTON_NOT_DISPLAYED;
import static constants.ErrorMessages.BURGER_MENU_BUTTON_NOT_DISPLAYED;
import static constants.ErrorMessages.HEADER_INCORRECT_OR_NOT_DISPLAYED;
import static constants.ErrorMessages.BUTTON_NOT_DISPLAYED_OR_HAS_INCORRECT_TEXT;
import static constants.ErrorMessages.PRIMARY_BUTTON_NOT_DISPLAYED_OR_HAS_INCORRECT_TEXT;
import static constants.ErrorMessages.INPUT_NOT_DISPLAYED_OR_PLACEHOLDER_INCORRECT;
import static constants.ErrorMessages.LABEL_ABOVE_INPUT_INCORRECT_OR_NOT_DISPLAYED;
import static constants.ErrorMessages.PLACEHOLDER_DISPLAYED_IN_FOCUSED_INPUT;
import static constants.ErrorMessages.PRIMARY_BUTTON_ENABLED;
import static constants.ErrorMessages.PROGRESS_BAR_NOT_DISPLAYED;
import static constants.ErrorMessages.TITLE_INCORRECT_OR_NOT_DISPLAYED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.DataGenerator.generatePhoneNumber;

@Epic("Epic-1 Регистрация/Авторизация/Безопасность")
@Feature("US-1.1 Регистрация")
@Story("US-1.1.5 Регистрация (для не клиентов банка)")
//Аннотация Order добавлена временно, чтобы стабилизировать выполнение теста проверки
// пустых значений. Иначе при запуске всех тестов проекта этот тест падает
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EnterDataNonClientsRegistrationTest extends BaseTest {

    private static final String phone = generatePhoneNumber(Country.RU);

    @BeforeAll
    public static void goToEnterDataPage() {
        mainPage.clickOnSignUpButton();
        enterPhoneForRegistrationPage.clickOnSkipButton();
        enterPhoneForRegistrationPage.enterPhoneNumber(phone);
        enterPhoneForRegistrationPage.clickOnPrimaryButton();
        enterSMSCodePage.enterSMSCode(databaseHandler.getSMSCode(phone));
        enterSMSCodePage.clickOnPrimaryButton();
    }

    @Test
    @DisplayName("C5992666 Проверка формы 'Заполните следующую информацию' (для не клиентов банка)")
    @Order(1)
    public void enterDataFormTest() {
        assertAll("Проверка отображения полей ввода формы 'Заполните следующую информацию'",
                () -> assertEquals("Регистрация", enterDataPage.getRegistrationPageHeaderText(),
                        HEADER_INCORRECT_OR_NOT_DISPLAYED),
                () -> assertEquals("Заполните следующую информацию", enterDataPage.getEnterDataTitleText(),
                        TITLE_INCORRECT_OR_NOT_DISPLAYED),
                () -> assertEquals("Имя", enterDataPage.getFirstNameInputText(),
                        INPUT_NOT_DISPLAYED_OR_PLACEHOLDER_INCORRECT),
                () -> assertEquals("Фамилия", enterDataPage.getLastNameInputText(),
                        INPUT_NOT_DISPLAYED_OR_PLACEHOLDER_INCORRECT),
                () -> assertEquals("Отчество (необязательно)", enterDataPage.getMiddleNameInputText(),
                        INPUT_NOT_DISPLAYED_OR_PLACEHOLDER_INCORRECT),
                () -> assertEquals("Продолжить", enterDataPage.getPrimaryButtonText(),
                        PRIMARY_BUTTON_NOT_DISPLAYED_OR_HAS_INCORRECT_TEXT),
                () -> assertTrue(enterDataPage.isBackButtonDisplayed(), BACK_BUTTON_NOT_DISPLAYED),
                () -> assertTrue(enterDataPage.isBurgerMenuButtonDisplayed(), BURGER_MENU_BUTTON_NOT_DISPLAYED),
                () -> assertTrue(enterDataPage.isProgressBarDisplayed(), PROGRESS_BAR_NOT_DISPLAYED)
        );
        enterDataPage.scrollForm(0.5, 0.5, 0.5, 0.2);
        assertAll("Проверка отображения радиобаттонов",
                () -> assertEquals("Резидент РФ", enterDataPage.getResidentRadioButtonText(),
                        BUTTON_NOT_DISPLAYED_OR_HAS_INCORRECT_TEXT),
                () -> assertEquals("Нерезидент РФ", enterDataPage.getNonResidentRadioButtonText(),
                        BUTTON_NOT_DISPLAYED_OR_HAS_INCORRECT_TEXT)
        );
    }

    @Test
    @DisplayName("C5992686 Проверка полей ввода 'Имя', 'Фамилия', " +
            "'Отчество (Необязательно)', 'Номер паспорта' (для не клиентов банка)")
    public void enterDataInputsTest() {
        enterDataPage.clickInFirstNameInput();
        assertAll("Проверка экрана после фокуса на поле ввода имени",
                () -> assertTrue(enterDataPage.getFirstNameInputText().isEmpty(),
                        PLACEHOLDER_DISPLAYED_IN_FOCUSED_INPUT),
                () -> assertEquals("Имя", enterDataPage.getLabelAboveInputText(),
                        LABEL_ABOVE_INPUT_INCORRECT_OR_NOT_DISPLAYED)
        );
        enterDataPage.clickOnDeviceBackButton();
        enterDataPage.clickInLastNameInput();
        assertAll("Проверка экрана после фокуса на поле ввода фамилии",
                () -> assertTrue(enterDataPage.getLastNameInputText().isEmpty(),
                        PLACEHOLDER_DISPLAYED_IN_FOCUSED_INPUT),
                () -> assertEquals("Фамилия", enterDataPage.getLabelAboveInputText(),
                        LABEL_ABOVE_INPUT_INCORRECT_OR_NOT_DISPLAYED)
        );
        enterDataPage.clickOnDeviceBackButton();
        enterDataPage.clickInMiddleNameInput();
        assertAll("Проверка экрана после фокуса на поле ввода отчетства",
                () -> assertTrue(enterDataPage.getMiddleNameInputText().isEmpty(),
                        PLACEHOLDER_DISPLAYED_IN_FOCUSED_INPUT),
                () -> assertEquals("Отчество", enterDataPage.getLabelAboveInputText(),
                        LABEL_ABOVE_INPUT_INCORRECT_OR_NOT_DISPLAYED)
        );
        enterDataPage.clickOnDeviceBackButton();
        enterDataPage.clickInPassportNumberInput();
        enterDataPage.clickOnDeviceBackButton();
        assertAll("Проверка экрана после фокуса на поле ввода номера паспорта",
                () -> assertTrue(enterDataPage.getPassportNumberInputText().isEmpty(),
                        PLACEHOLDER_DISPLAYED_IN_FOCUSED_INPUT),
                () -> assertEquals("Номер паспорта", enterDataPage.getLabelAboveInputText(),
                        LABEL_ABOVE_INPUT_INCORRECT_OR_NOT_DISPLAYED)
        );
    }

    @Test
    @DisplayName("C5865177 Проверка ввода пустых значений на странице " +
            "'Заполните следующую информацию' (Для не клиентов банка)")
    public void emptyDataTest() {
        enterDataPage.clickOnPrimaryButton();
        assertAll("Проверка экрана после попытки перейти на следующий этап регистрации без заполнения полей формы",
                () -> assertFalse(enterDataPage.isPrimaryButtonEnabled(), PRIMARY_BUTTON_ENABLED),
                () -> assertEquals("Заполните следующую информацию",
                        enterDataPage.getEnterDataTitleText(),
                        TITLE_INCORRECT_OR_NOT_DISPLAYED)
        );
    }

    @AfterEach
    public void scrollToTop() {
        enterDataPage.scrollForm(0.5, 0.2, 0.5, 0.5);
    }
}