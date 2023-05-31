package userService.ep_17_gettingInformationAboutUser;

import BaseTest.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojos.AuthenticatedUserData;
import pojos.User;
import test_data.UserAuthorizationTestData;

import java.io.File;

import static BaseTest.BaseTest.Messages.STATUS_CODE_CHECK;
import static org.testng.Assert.assertEquals;

@Epic(value = "UserService")
@Feature(value = "EP-17 Получение информации о пользователе")
public class UserInformationTest extends BaseTest {

    private String userId;
    private String accessToken;
    private ExtractableResponse<Response> response;

    @BeforeClass
    public void setUp() {
        User registeredUser = USER_PROVIDER.getDefaultUserFromPropertyFile();
        AuthenticatedUserData authenticatedData = USER_MANAGEMENT.registerAndAuthorizeUserByPhoneNumber(registeredUser);
        accessToken = authenticatedData.getAccessToken();
        userId = authenticatedData.getUserId();
    }

    @Test(description = "C5963802 Получение информации о пользователе")
    public void gettingInformationUserTest() {
        ValidatableResponse validatableResponse = INFORMATION_REQUESTS.getInformationAboutUser(accessToken);
        assertEquals(validatableResponse.extract().statusCode(), 200, STATUS_CODE_CHECK.toString());
        validatableResponse.assertThat().body(JsonSchemaValidator.matchesJsonSchema(
                new File("src/test/resources/schemes/get_user_info_response_body.json")));
    }

    @Test(description = "C5963810 Получение информации о пользователе при вводе неправильного access token",
            dataProviderClass = UserAuthorizationTestData.class, dataProvider = "invalidTokens")
    public void gettingInformationUserInvalidTokenTest(String invalidToken) {
        response = INFORMATION_REQUESTS.getInformationAboutUser(invalidToken).extract();
        assertEquals(response.statusCode(), 401, STATUS_CODE_CHECK.toString());
    }

    @Test(description = "C5963811 Получение информации о пользователе при вводе неверного URL")
    public void gettingInformationUserInvalidURLTest() {
        String invalidUrl = "api/v1/informationnnnnnnnnnnnnnnn";
        response = INFORMATION_REQUESTS.baseGetRequestWithToken(invalidUrl, accessToken).extract();
        assertEquals(response.statusCode(), 404, STATUS_CODE_CHECK.toString());
    }

    @AfterClass
    public void tearDown() {
        USER_SERVICE_DB_REQUESTS.deleteUserFromUserServiceDataBaseById(userId);
    }
}