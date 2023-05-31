package epic_1_registrationAuthorizationSecurity.us_1_1_registration.us_1_1_5_registrationForNonBankClients.step_4_createPassword;

import baseTest.BaseTest;
import data.Country;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static constants.ErrorMessages.ACTIVE;
import static constants.ErrorMessages.BACK_BUTTON_NOT_DISPLAYED;
import static constants.ErrorMessages.NOT_VISIBLE;
import static constants.ErrorMessages.PASSWORDS_MISMATCH;
import static constants.ErrorMessages.SECURITY_QUESTION_TITLE_NOT_VISIBLE;
import static constants.ErrorMessages.TEXT_DOES_NOT_MATCH_WITH_EXPECTED;
import static constants.ErrorMessages.TEXT_INCORRECT_SYMBOLS_ENTERED_NOT_VISIBLE;
import static constants.ErrorMessages.VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.DataGenerator.generatePhoneNumber;
import static utils.DataGenerator.generateRandomPassword;

@Epic("Epic-2 Личный кабинет")
@Feature("US-2.1 Личный кабинет")
@Story("US-2.1.5 Раздел 'О приложении'")
public class CreatePasswordNonClientRegistrationTest extends BaseTest {

    private static final String phone = generatePhoneNumber(Country.RU);

    @BeforeAll
    public static void goToEnterDataPage() {
        mainPage.clickOnSignUpButton();
        enterPhoneForRegistrationPage.clickOnSkipButton();
        enterPhoneForRegistrationPage.enterPhoneNumber(phone);
        enterPhoneForRegistrationPage.clickOnPrimaryButton();
        enterSMSCodePage.enterSMSCode(databaseHandler.getSMSCode(phone));
        enterSMSCodePage.clickOnPrimaryButton();
        enterDataPage.fillInForm("Firstname", "Lastname", "Middlename", "7777777");
        enterDataPage.scrollForm(0.5, 0.5, 0.5, 0.2);
        enterDataPage.clickOnResidentRadioButton();
    }

    @Test
    @DisplayName("C5870037 Проверка формы 'Придумайте пароль' (Для не клиентов банка)")
    public void createPasswordFieldNonClientRegistrationTest(){
        String password = generateRandomPassword(6);
        enterDataPage.clickOnPrimaryButton();
           assertAll("Проверка основных полей формы 'Придумайте пароль'",
                () -> assertTrue(createPasswordPage.isRegistrationHeaderDisplayed(),
                        "Регистрация" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isCreatePasswordTitleDisplayed(),
                        "Придумайте пароль" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isCreatePasswordInputFieldDisplayed(),
                        "Поле 'Введите пароль'" + NOT_VISIBLE)
        );
        createPasswordPage.clickPasswordInputField();
        assertAll("Проверка подсказок формы 'Придумайте пароль' при заполнении пароля",
                () -> assertTrue(createPasswordPage.isPasswordCreateTextToggleIconDisplayed(),
                        "Кнопка 'Посмотреть пароль'" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isEditTextLabelDisplayed(),
                        "Введите пароль" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isConditionLengthDisplayed(),
                        "Условие количества символов" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isConditionUppercaseLettersDisplayed(),
                        "Условие к наличию заглавных латинских букв" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isConditionLowercaseLettersDisplayed(),
                        "Условие к наличию строчных латинских букв" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isConditionDigitsDisplayed(),
                        "Условие к наличию цифр" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isConditionSpecialSymbolsDisplayed(),
                        "Условие к наличию специальных символов" + NOT_VISIBLE)
        );
        createPasswordPage.clickPasswordCreateTextToggleIcon();
        createPasswordPage.inputPassword(password);
        createPasswordPage.clickPasswordConfirmField();
        assertAll("Проверка полей формы при подтверждения пароля",
                () -> assertTrue(createPasswordPage.isPasswordConfirmFieldDisplayed(),
                        "Поле 'Подтвердить пароль'" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isPasswordConfirmTextToggleIcon(),
                        "Кнопка 'Посмотреть пароль'" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isPrimaryButtonDisplayed(),
                        "Кнопка 'Продолжить'" + NOT_VISIBLE)
        );
        createPasswordPage.confirmPassword(password);
        createPasswordPage.clickPasswordConfirmTextToggleIcon();
        createPasswordPage.clickOnPrimaryButton();
        createSecurityQuestionPage.clickOnBackButton();
        assertAll("Проверка при возвращении на предыдущую страницу с паролем, данные о пароле должны быть видны)",
                () -> assertEquals(password, createPasswordPage.getPasswordFromInputField(),
                        PASSWORDS_MISMATCH),
                () -> assertEquals(password, createPasswordPage.getPasswordFromConfirmField(),
                        PASSWORDS_MISMATCH)
        );
        createPasswordPage.clickOnBurgerMenuButton();
        assertAll("Проверка основного раздела Бургерного меню на отображение всех элементов",
                () -> assertTrue(burgerMenuPage.isATMsAndBranchesButtonDisplayed(),
                        "Не отображается кнопка 'Банкоматы и отделения'"),
                () -> assertTrue(burgerMenuPage.isExchangeRatesButtonDisplayed(),
                        "Не отображается кнопка 'Курсы валют'"),
                () -> assertTrue(burgerMenuPage.isLocalSupportNumberDisplayed(),
                        "Не отображается номер телефона для звонков по России"),
                () -> assertTrue(burgerMenuPage.isInternationalSupportNumberDisplayed(),
                        "Не отображается номер телефона для международных звонков"),
                () -> assertTrue(burgerMenuPage.isBugReportButtonDisplayed(),
                        "Не отображается кнопка 'Отправить отчет об ошибке'"),
                () -> assertTrue(burgerMenuPage.isSetLangDropdownDisplayed(),
                        "Не отображается дропдаун выбора языка приложения"),
                () -> assertTrue(burgerMenuPage.isSetThemeDropdownDisplayed(),
                        "Не отображается дропдаун выбора темы приложения"),
                () -> assertTrue(burgerMenuPage.isBackButtonDisplayed(), BACK_BUTTON_NOT_DISPLAYED)
        );
        burgerMenuPage.clickOnBackButton();
        assertAll("Проверка при возвращении на предыдущую страницу с паролем, данные о пароле должны быть видны)",
                () -> assertEquals(password, createPasswordPage.getPasswordFromInputField(),
                        PASSWORDS_MISMATCH),
                () -> assertEquals(password, createPasswordPage.getPasswordFromConfirmField(),
                        PASSWORDS_MISMATCH)
        );
        createPasswordPage.clickOnBackButton();
    }

    @Test
    @DisplayName("C5995476 Проверка поля 'Введите пароль' (Для не клиентов банка)")
    public void checkPasswordFieldNonClientRegistrationTest(){
        String condition_length = "Количество символов от 6 до 20";
        String uppercase_letters = "Есть заглавные латинские буквы";
        String lowercase_letters = "Есть строчные латинские буквы";
        String digits = "Есть цифры";
        String special_symbols = "Есть специальные символы !  $ & ' ( ) * + , - . : ; = [  ] ^ _` { | } ~";
        enterDataPage.clickOnPrimaryButton();
        assertTrue(createPasswordPage.isCreatePasswordInputFieldDisplayed(),
                "Поле 'Введите пароль'" + NOT_VISIBLE);
        createPasswordPage.clickPasswordInputField();
        assertAll("Проверка появления требований к паролю",
                () -> assertTrue(createPasswordPage.isEditTextLabelDisplayed(),
                        "Введите пароль" + NOT_VISIBLE),
                () -> assertTrue(createPasswordPage.isPasswordCreateTextToggleIconDisplayed(),
                        "Кнопка 'Посмотреть пароль'" + NOT_VISIBLE),
                () -> assertEquals(condition_length, createPasswordPage.getTextFromConditionLength(),
                        TEXT_DOES_NOT_MATCH_WITH_EXPECTED),
                () -> assertEquals(uppercase_letters, createPasswordPage.getTextFromConditionUppercaseLetters(),
                        TEXT_DOES_NOT_MATCH_WITH_EXPECTED),
                () -> assertEquals(lowercase_letters, createPasswordPage.getTextFromConditionLowercaseLetters(),
                        TEXT_DOES_NOT_MATCH_WITH_EXPECTED),
                () -> assertEquals(digits, createPasswordPage.getTextFromConditionDigits(),
                        TEXT_DOES_NOT_MATCH_WITH_EXPECTED),
                () -> assertEquals(special_symbols, createPasswordPage.getTextFromConditionSpecialSymbols(),
                        TEXT_DOES_NOT_MATCH_WITH_EXPECTED)
        );
        createPasswordPage.clickOnBackButton();
    }

    @Test
    @DisplayName("C5868412 Проверка несовпадающих паролей (Для не клиентов банка)")
    public void checkMismatchedPasswordsNonClientRegistrationTest(){
        String password = generateRandomPassword(6);
        enterDataPage.clickOnPrimaryButton();
        createPasswordPage.inputPassword(password);
        createPasswordPage.confirmPassword(password + "!");
        assertFalse(createPasswordPage.isPrimaryButtonEnabled(),
                "Кнопка 'Продолжить'" + ACTIVE);
        createPasswordPage.clickOnBackButton();
    }

    @Test
    @DisplayName("C5995513 Проверка на сохранения данных в полях при переходе назад (Для не клиентов банка)")
    public void checkBackPasswordNonClientRegistrationTest(){
        assertAll("Проверка формы 'Заполните следующую информацию' после её заполнения валидными значениями",
                () -> assertEquals("Firstname", enterDataPage.getFirstNameInputText(),
                        VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED),
                () -> assertEquals("Lastname", enterDataPage.getLastNameInputText(),
                        VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED),
                () -> assertEquals("Middlename", enterDataPage.getMiddleNameInputText(),
                        VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED),
                () -> assertEquals("7777777", enterDataPage.getPassportNumberInputText(),
                        VALUE_INCORRECTLY_DISPLAYED_OR_NOT_DISPLAYED)
        );
    }

    @ParameterizedTest
    @DisplayName("C5868416, C5868417, C5868418, C5868419 Проверка валидных значений пароля")
    @MethodSource("getValidArguments")
    public void validPassword(String password) {
        enterDataPage.clickOnPrimaryButton();
        createPasswordPage.clickPasswordInputField();
        createPasswordPage.inputPassword(password);
        createPasswordPage.clickPasswordConfirmField();
        createPasswordPage.confirmPassword(password);
        createPasswordPage.clickOnPrimaryButton();
        assertTrue(createSecurityQuestionPage.isSecurityQuestionTitleDisplayed(),
                SECURITY_QUESTION_TITLE_NOT_VISIBLE);
        createSecurityQuestionPage.clickOnBackButton();
        createPasswordPage.clickOnBackButton();
    }
    static Stream<Arguments> getValidArguments (){
        return Stream.of(
                Arguments.of(generateRandomPassword(6)),
                Arguments.of(generateRandomPassword(7)),
                Arguments.of(generateRandomPassword(19)),
                Arguments.of(generateRandomPassword(20)));
    }

    @ParameterizedTest
    @DisplayName("C5868422, C5868423, C5868426 Проверка невалидных значений пароля")
    @MethodSource("getNotValidArguments")
    public void notValidPassword(String password){
        enterDataPage.clickOnPrimaryButton();
        createPasswordPage.clickPasswordInputField();
        createPasswordPage.inputPassword(password);
        assertAll("Проверка не валидных значений пароля",
                () -> assertTrue(createPasswordPage.isTextIncorrectSymbolsEnteredDisplayed(),
                        TEXT_INCORRECT_SYMBOLS_ENTERED_NOT_VISIBLE),
                () -> assertFalse(createPasswordPage.isPasswordConfirmFieldActive(),
                        "Поле 'Подтвердить пароль'" + ACTIVE),
                () -> assertFalse(createPasswordPage.isPrimaryButtonEnabled(),
                        "Кнопка 'Продолжить'" + ACTIVE)
        );
        createPasswordPage.clickOnBackButton();
    }
    static Stream<Arguments> getNotValidArguments (){
        return Stream.of(
                Arguments.of("ïðèâåòìèð"),
                Arguments.of(" " + generateRandomPassword(4) + " " + generateRandomPassword(4) + " "),
                Arguments.of("' or '1'='1"));
    }

    @ParameterizedTest
    @DisplayName("C5868415, C5868420, C5868425 Проверка невалидных значений пароля")
    @MethodSource("getNotValidQuantityDigitsArguments")
    public void notValidDigitsPassword(String password){
        enterDataPage.clickOnPrimaryButton();
        createPasswordPage.clickPasswordInputField();
        createPasswordPage.inputPassword(password);
        assertAll("Проверка не валидных значений пароля",
                () -> assertFalse(createPasswordPage.isPasswordConfirmFieldActive(),
                        "Поле 'Подтвердить пароль'" + ACTIVE),
                () -> assertFalse(createPasswordPage.isPrimaryButtonEnabled(),
                        "Кнопка 'Продолжить'" + ACTIVE)
        );
        createPasswordPage.clickOnBackButton();
    }
    static Stream<Arguments> getNotValidQuantityDigitsArguments (){
        return Stream.of(
                Arguments.of(""),
                Arguments.of(generateRandomPassword(5)),
                Arguments.of(generateRandomPassword(21)));
    }

    @AfterAll
    public static void setAfterAll() {
        createPasswordPage.closeApp();
    }
}