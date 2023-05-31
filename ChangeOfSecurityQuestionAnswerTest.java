package userService.ep_15_changeSecurityQuestionAnswer;

import BaseTest.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pojos.AuthenticatedUserData;
import pojos.User;
import pojos.UserControllerRequestsData;
import test_data.ChangeOfSecuritySettingsTestData;
import test_data.UserAuthorizationTestData;

import static BaseTest.BaseTest.ExpectedStatuses.BAD_REQUEST;
import static BaseTest.BaseTest.ExpectedStatuses.NOT_FOUND;
import static BaseTest.BaseTest.ExpectedStatuses.UNAUTHORIZED;
import static BaseTest.BaseTest.Messages.ERROR_MESSAGE_DESCRIPTION;
import static BaseTest.BaseTest.Messages.STATUS_CODE_CHECK;
import static BaseTest.BaseTest.Messages.STATUS_CODE_DESCRIPTION;

@Epic(value = "UserService")
@Feature(value = "EP-15 Изменение контрольного вопроса/ответа")

public class ChangeOfSecurityQuestionAnswerTest extends BaseTest {

    private String userId;
    private String accessToken;
    private UserControllerRequestsData body;
    private ExtractableResponse<Response> response;

    @BeforeClass
    public void setUp() {
        User registeredUser = USER_PROVIDER.getDefaultUserFromPropertyFile();
        AuthenticatedUserData authenticatedData = USER_MANAGEMENT.registerAndAuthorizeUserByPhoneNumber(registeredUser);
        accessToken = authenticatedData.getAccessToken();
        userId = authenticatedData.getUserId();
    }

    @Test(description = "C5900758 Проверка изменения контрольного вопроса/ответа",
            dataProvider = "correctSecurityQuestionAndAnswer", dataProviderClass = ChangeOfSecuritySettingsTestData.class)
    public void changeOfSecurityQuestionAndAnswerTest(String securityQuestion, String securityAnswer) {
        body = UserControllerRequestsData
                .builder()
                .securityQuestion(securityQuestion)
                .securityAnswer(securityAnswer)
                .build();
        response = USER_CONTROLLER_REQUESTS.updateSecurityQuestionAndAnswer(accessToken, body).extract();
        softAssert.assertEquals(response.statusCode(), 200, STATUS_CODE_CHECK.toString());
        softAssert.assertEquals(USER_SERVICE_DB_REQUESTS.getSecurityQuestionByClientId(userId), securityQuestion,
                "Контрольный вопрос не добавлен в базу данных");
        softAssert.assertEquals(USER_SERVICE_DB_REQUESTS.getSecurityAnswerByClientId(userId), securityAnswer,
                "Контрольный ответ не добавлен в базу данных");
        softAssert.assertAll();
    }

    @Test(description = "C5900760 Проверка изменения контрольного вопроса/ответа при вводе неправильного access token",
            dataProvider = "invalidTokens", dataProviderClass = UserAuthorizationTestData.class)
    public void incorrectAccessTokenTest(String invalidToken) {
        body = UserControllerRequestsData
                .builder()
                .securityQuestion("A")
                .securityAnswer("B")
                .build();
        response = USER_CONTROLLER_REQUESTS.updateSecurityQuestionAndAnswer(invalidToken, body).extract();
        softAssert.assertEquals(response.statusCode(), 401, STATUS_CODE_CHECK.toString());
        softAssert.assertEquals(response.jsonPath().get("statusCode"), UNAUTHORIZED.toString(),
                STATUS_CODE_DESCRIPTION.toString());
        softAssert.assertAll();
    }

    @Test(description = "C5900761 Проверка изменения контрольного вопроса/ответа при вводе невалидных значений " +
            "параметров", dataProvider = "incorrectSecurityQuestionAndAnswer",
            dataProviderClass = ChangeOfSecuritySettingsTestData.class)
    @Ignore("Issue: AFI-2377 - при вводе некоторых невалидных значений вместо кода 400 приходит 500." +
            "Issue: AFI-2392 - в базу данных можно записать невалидные значения")
    public void incorrectSecurityDataTest(String securityQuestion, String securityAnswer) {
        body = UserControllerRequestsData
                .builder()
                .securityQuestion(securityQuestion)
                .securityAnswer(securityAnswer)
                .build();
        response = USER_CONTROLLER_REQUESTS.updateSecurityQuestionAndAnswer(accessToken, body).extract();
        softAssert.assertEquals(response.statusCode(), 400, STATUS_CODE_CHECK.toString());
        softAssert.assertEquals(response.jsonPath().get("errorMessage"), BAD_REQUEST.toString(),
                ERROR_MESSAGE_DESCRIPTION.toString());
        softAssert.assertAll();
    }

    @Test(description = "C5963797 Проверка изменения контрольного вопроса/ответа при вводе неверного URL")
    public void incorrectURLTest() {
        String invalidUrl = "/api/v1/user/settings/controls1";
        body = UserControllerRequestsData
                .builder()
                .securityQuestion("A")
                .securityAnswer("B")
                .build();
        response = USER_CONTROLLER_REQUESTS.basePatchRequestWithBodyAndToken(invalidUrl, accessToken, body).extract();
        softAssert.assertEquals(response.statusCode(), 404, STATUS_CODE_CHECK.toString());
        softAssert.assertEquals(response.jsonPath().get("error"), NOT_FOUND.toString(),
                ERROR_MESSAGE_DESCRIPTION.toString());
        softAssert.assertAll();
    }

    @AfterClass
    public void tearDown() {
        USER_SERVICE_DB_REQUESTS.deleteUserFromUserServiceDataBaseById(userId);
    }
}