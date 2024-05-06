package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.user.CreateUser;
import org.example.user.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.Constants.*;
import static org.example.api.UserApi.*;

public class ChangeUserTests extends BaseTest{

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    public void changeUserEmailWithAuth() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        Response responseNewUser = responseSendPostCreateUser(newUser);
        String accessToken = getAccessToken(responseNewUser);
        CreateUser updateUser = new CreateUser(CHANGE_USER_EMAIL, null, null);
        Response responseUpdateUser = responseSendPatchChangUser(updateUser, accessToken);
        verificationChangTrue(responseUpdateUser, CHANGE_USER_EMAIL, USER_NAME);
        sendPatchChangUserToDefault(newUser, accessToken);
    }

    @Test
    @DisplayName("Изменение password пользователя с авторизацией")
    public void changeUserPasswordWithAuth() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        Response responseNewUser = responseSendPostCreateUser(newUser);
        String accessToken = getAccessToken(responseNewUser);
        CreateUser updateUser = new CreateUser(null, CHANGE_USER_PASSWORD, null);
        Response responseUpdateUser = responseSendPatchChangUser(updateUser, accessToken);
        verificationChangTrue(responseUpdateUser, USER_EMAIL, USER_NAME);
        sendPatchChangUserToDefault(newUser, accessToken);
    }

    @Test
    @DisplayName("Изменение name пользователя с авторизацией")
    public void changeUserNameWithAuth() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        Response responseNewUser = responseSendPostCreateUser(newUser);
        String accessToken = getAccessToken(responseNewUser);
        CreateUser updateUser = new CreateUser(null, null, CHANGE_USER_NAME);
        Response responseUpdateUser = responseSendPatchChangUser(updateUser, accessToken);
        verificationChangTrue(responseUpdateUser, USER_EMAIL, CHANGE_USER_NAME);
        sendPatchChangUserToDefault(newUser, accessToken);
    }

    @Test
    @DisplayName("Изменение email без авторизации")
    public void changeUserEmailWithoutAuth() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        sendPostCreateUser(newUser);
        CreateUser updateUser = new CreateUser(CHANGE_USER_EMAIL, null, null);
        Response response = responseSendPatchChangUser(updateUser);
        verificationUserSuccessFalse(response, "You should be authorised", SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение password без авторизации")
    public void changeUserPasswordWithoutAuth() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        sendPostCreateUser(newUser);
        CreateUser updateUser = new CreateUser(null, CHANGE_USER_PASSWORD, null);
        Response response = responseSendPatchChangUser(updateUser);
        verificationUserSuccessFalse(response, "You should be authorised", SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение name без авторизации")
    public void changeUserNameWithoutAuth() {
        CreateUser newUser = new CreateUser(USER_EMAIL, USER_PASSWORD, USER_NAME);
        sendPostCreateUser(newUser);
        CreateUser updateUser = new CreateUser(null, null, CHANGE_USER_NAME);
        Response response = responseSendPatchChangUser(updateUser);
        verificationUserSuccessFalse(response, "You should be authorised", SC_UNAUTHORIZED);
    }

    @After
    public void deleteUser() {
        Login login = new Login(Constants.USER_EMAIL, Constants.USER_PASSWORD);
        Response authorization = responseSendPostLoginUser(login);
        String accessToken = getAccessToken(authorization);
        Response delete = sendDeleteUser(accessToken);
        verificationUserSuccessTrue(delete, "User successfully removed", SC_ACCEPTED);
    }
}

