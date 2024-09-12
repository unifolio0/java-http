package com.techcourse.controller;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.httpresponse.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
    private static final String REGISTER_PATH = "/register";
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String INDEX_PATH = "/index.html";

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (validateUserInput(httpRequest)) {
            log.error("입력하지 않은 항목이 있습니다.");
            redirectPage(httpRequest, httpResponse, REGISTER_PATH);
            return;
        }
        acceptRegister(httpRequest, httpResponse);
    }

    private void acceptRegister(HttpRequest httpRequest, HttpResponse httpResponse) {
        String account = httpRequest.getBodyValue(ACCOUNT);
        String password = httpRequest.getBodyValue(PASSWORD);
        String email = httpRequest.getBodyValue(EMAIL);

        if (InMemoryUserRepository.containsByAccount(account)) {
            log.error("이미 존재하는 account입니다");
            redirectPage(httpRequest, httpResponse, REGISTER_PATH);
            return;
        }

        User user = new User(account, password, email);
        InMemoryUserRepository.save(user);
        redirectPage(httpRequest, httpResponse, INDEX_PATH);
    }

    private boolean validateUserInput(HttpRequest httpRequest) {
        return !httpRequest.containsBody(ACCOUNT)
                || !httpRequest.containsBody(PASSWORD)
                || !httpRequest.containsBody(EMAIL);
    }

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.ok(httpRequest);
        httpResponse.staticResource(httpRequest.getPath());
    }

    private void redirectPage(HttpRequest httpRequest, HttpResponse httpResponse, String path) {
        httpResponse.location(httpRequest, path);
    }
}
