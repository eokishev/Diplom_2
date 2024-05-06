package org.example;

import io.restassured.RestAssured;
import org.junit.Before;

import static org.example.Constants.*;

public class BaseTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

}
