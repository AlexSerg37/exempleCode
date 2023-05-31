package userService.ep_18_changingPhoneNumber;

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
import test_data.UserAuthorizationTestData;

import static BaseTest.BaseTest.Messages.STATUS_CODE_CHECK;
import static org.testng.Assert.assertEquals;

@Epic(value = "UserService")
@Feature(value = "EP-18 Изменение номера телефона")
public class ChangingPhoneNumberTest extends BaseTest {

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

    @Test(description = "C5963812 Изменение номера телефона")
    public void changingPhoneNumberTest() {
        String newPhoneNumber = "79898989898";
        body = UserControllerRequestsData.builder().mobilePhone(newPhoneNumber).build();
        response = USER_CONTROLLER_REQUESTS.updateUserPhoneNumber(accessToken, body).extract();
        softAssert.assertEquals(response.statusCode(), 200, STATUS_CODE_CHECK.toString());
        softAssert.assertEquals(USER_SERVICE_DB_REQUESTS.getMobilePhoneByClientId(userId), newPhoneNumber,
                "номер телефона в базе данных не совпадает с новым");
        softAssert.assertAll();
    }

    @Test(description = "C5963813 Изменение номера телефона при вводе невалидного значения параметра")
    @Ignore("AFI-2379")
    public void changingPhoneNumberInvalidValueTest() {
        String invalidPhoneNumber = "7989898989abc";
        body = UserControllerRequestsData.builder().mobilePhone(invalidPhoneNumber).build();
        response = USER_CONTROLLER_REQUESTS.updateUserPhoneNumber(accessToken, body).log().all().extract();
        assertEquals(response.statusCode(), 400, STATUS_CODE_CHECK.toString());
    }

    @Test(description = "C5963819 Изменение номера телефона при вводе неправильного access token",
            dataProviderClass = UserAuthorizationTestData.class, dataProvider = "invalidTokens")
    public void changingPhoneNumberInvalidAccessTokenTest(String invalidToken) {
        body = UserControllerRequestsData.builder().mobilePhone("777777777").build();
        response = USER_CONTROLLER_REQUESTS.updateUserPhoneNumber(invalidToken, body).extract();
        assertEquals(response.statusCode(), 401, STATUS_CODE_CHECK.toString());
    }

    @Test(description = "C5963820 Изменение номера телефона при вводе неверного URL")
    public void changingPhoneNumberInvalidURLTest() {
        String invalidUrl = "api/v1/user/settings/phoneeeeeee";
        body = UserControllerRequestsData.builder().mobilePhone("722233444").build();
        response = USER_CONTROLLER_REQUESTS.basePatchRequestWithBodyAndToken(invalidUrl, accessToken, body).extract();
        assertEquals(response.statusCode(), 404, STATUS_CODE_CHECK.toString());
    }

    @AfterClass
    public void tearDown() {
        USER_SERVICE_DB_REQUESTS.deleteUserFromUserServiceDataBaseById(userId);
    }
}