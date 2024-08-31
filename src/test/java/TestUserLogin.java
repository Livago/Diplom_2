import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.UserSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
public class TestUserLogin {
    UserSteps userSteps;
    String email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
    String password = RandomStringUtils.randomNumeric(9);
    String name = RandomStringUtils.randomAlphabetic(8);


    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
    }
    @Test
    @DisplayName("Авторизация пользователем")
    @Description("Проверка авторизации пользователя")
    public void canLoginUser() {
        userSteps.create(email, password, name);
        ValidatableResponse responseTwo = userSteps.login(email, password).statusCode(SC_OK).and().body("success", equalTo(true));
        userSteps.getAccessToken(responseTwo);
    }
    @Test
    @DisplayName("Авторизация пользователя без логина")
    @Description("Проверка невозможности авторизации пользователя без логина")
    public void loginUserWithoutEmail() {
        userSteps.login(null, password).statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("email or password are incorrect"));
    }
    @Test
    @DisplayName("Авторизация пользователя без пароля")
    @Description("Проверка невозможности авторизации пользователя без пароля")
    public void loginUserWithoutPassword() {
        userSteps.login(null, password).statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("email or password are incorrect"));
    }
    @After
    public void tearDown() {
        userSteps.delete();
    }
}
