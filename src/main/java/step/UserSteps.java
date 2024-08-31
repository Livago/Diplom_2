package step;

import client.UserClient;
import dto.UserCreateRequest;
import dto.UserLoginRequest;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
public class UserSteps {
    private final UserClient userClient;
    private String accessToken;

    public UserSteps (UserClient userClient){
        this.userClient = userClient;
    }

    @Step("Создать пользователя")
    public ValidatableResponse create(String email, String password, String name) {
        UserCreateRequest request = new UserCreateRequest(email, password, name);
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        return userClient.create(request).then();
    }
    @Step("Авторизоваться пользователем")
    public ValidatableResponse login(String email, String password) {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return userClient.login(request).then();
    }
    @Step("Изменить данные пользователя c токеном")
    public ValidatableResponse update(String accessToken, String email, String password, String name) {
        return userClient.update(accessToken, email, password, name).then();
    }
    @Step("Изменить данные пользователя без токена")
    public ValidatableResponse updateWithoutToken(String email, String password, String name) {
        return userClient.updateWithoutToken(email, password, name).then();
    }
    @Step("Получить токен")
    public String getAccessToken(ValidatableResponse validatableResponse) {
        accessToken = validatableResponse.extract().path("accessToken");
        return accessToken;
    }
    @Step("Удалить пользователей после тестов")
    public void delete() {
        userClient.deletingUsersAfterTests(accessToken);
    }

}
