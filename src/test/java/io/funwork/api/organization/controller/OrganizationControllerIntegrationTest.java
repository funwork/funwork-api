package io.funwork.api.organization.controller;

import com.jayway.restassured.RestAssured;
import io.funwork.FunworkApiApplicationTests;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FunworkApiApplicationTests.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@ActiveProfiles(profiles = "test")
public class OrganizationControllerIntegrationTest {

    private static final String CONTENT_TYPE = "application/json";
    private static final String URI = "/api/organization";


    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void test_get_tree_by_person_id() throws Exception {

        given().contentType(CONTENT_TYPE)
                .when().get(URI+"/tree/1")
                .then().statusCode(HttpStatus.SC_OK).body(containsString("테스트1-1"));
    }

    @Test
    public void test_get_tree_by_person_but_person_is_null() throws Exception {

        given().contentType(CONTENT_TYPE)
                .when().get(URI+"/tree/9999")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);

    }



}
