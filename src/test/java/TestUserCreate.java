import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
public class TestUserCreate {
    UserSteps userSteps;
    String email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
    String password = RandomStringUtils.randomAlphabetic(9);
    String name = RandomStringUtils.randomAlphabetic(8);


    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
    }
    @Test
    @DisplayName("Создание пользователя")
    @Description("Создание пользователя с валидным телом ")
    public void canUserCreate() {
        ValidatableResponse response = userSteps.create(email, password, name);
        response.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        userSteps.getAccessToken(response);
    }
    @Test
    @DisplayName("Создание существующего пользователя")
    @Description("Проверка невозможности создания пользователя, который уже был зарегистрирован")
    public void duplicateUserCreate() {
        ValidatableResponse responseOne = userSteps.create(email, password, name);
        userSteps.getAccessToken(responseOne);
        ValidatableResponse responseTwo = userSteps.create(email, password, name);
        responseTwo.assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка невозможности создания пользователя без обязательного поля")
    public void userCreateNoRequiredFieldPassword() {
        ValidatableResponse response = userSteps.create(email, null, name);
        response.assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка невозможности создания пользователя без обязательного поля")
    public void userCreateNoRequiredFieldEmail() {
        ValidatableResponse response = userSteps.create(null, password, name);
        response.assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка невозможности создания пользователя без обязательного поля")
    public void userCreateNoRequiredFieldName() {
        ValidatableResponse response = userSteps.create(email, password, null);
        response.assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }
    @After
    public void tearDown() {
        userSteps.delete();
    }
}
