package commons;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import report.ExtentCucumberFormatter;
import utils.PropertiesHelper;

public class APIMethods {

    public static Response response;
    public Properties properties = new PropertiesHelper().getProperties();
    public static RequestSpecification request = RestAssured.with();
    public String jsonRequest = null;


    //Criar JsonRequest e Header
    public void buildRequest(Object pojoObject) throws UnsupportedEncodingException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            jsonRequest = mapper.writeValueAsString(pojoObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        request = RestAssured.with();
        request.given().contentType(ContentType.JSON)
                .header("Content-Type", "application/json")
                .body(jsonRequest)
                .log().all();
    }

    //########################################## QUANDO ##########################################//

    //Realizar requisição GET
    @Quando("^envio uma requisição GET para \"([^\"]*)\" \"([^\"]*)\"$")
    public void getRequest(String environment, String endpoint) throws Throwable {
        response = request.when().get(properties.getProperty(environment) + properties.getProperty(endpoint));
        ExtentCucumberFormatter.insertInfoTextInStepReport("Endpoint:" + " " + properties.getProperty(environment) + properties.getProperty(endpoint));
    }

    //Realizar requisição POST
    @Quando("^envio uma requisição POST para \"([^\"]*)\" \"([^\"]*)\"$")
    public void postRequest(String environment, String endpoint) throws Throwable {
        response = request.when().post(properties.getProperty(environment) + properties.getProperty(endpoint));
        ExtentCucumberFormatter.insertInfoTextInStepReport("Endpoint:" + " " + properties.getProperty(environment) + properties.getProperty(endpoint));
    }

    //Realizar requisição PUT
    @Quando("^envio uma requisição PUT para \"([^\"]*)\" \"([^\"]*)\"$")
    public void putRequest(String environment, String endpoint) throws Throwable {
        response = request.when().put(properties.getProperty(environment) + properties.getProperty(endpoint));
        ExtentCucumberFormatter.insertInfoTextInStepReport("Endpoint:" + " " + properties.getProperty(environment) + properties.getProperty(endpoint));
    }

    //Realizar requisição PATCH
    @Quando("^envio uma requisição PATCH para \"([^\"]*)\" \"([^\"]*)\"$")
    public void patchRequest(String environment, String endpoint) throws Throwable {
        response = request.when().patch(properties.getProperty(environment) + properties.getProperty(endpoint));
        ExtentCucumberFormatter.insertInfoTextInStepReport("Endpoint:" + " " + properties.getProperty(environment) + properties.getProperty(endpoint));
    }

    //Realizar requisição DELETE
    @Quando("^envio uma requisição DELETE para \"([^\"]*)\" \"([^\"]*)\"$")
    public void deleteRequest(String environment, String endpoint) throws Throwable {
        response = request.when().delete(properties.getProperty(environment) + properties.getProperty(endpoint));
        ExtentCucumberFormatter.insertInfoTextInStepReport("Endpoint:" + " " + properties.getProperty(environment) + properties.getProperty(endpoint));
    }

    //########################################## ENTÃO ##########################################//

    //Validar o código de resposta
    @Então("^o código de resposta é (\\d+)$")
    public void o_código_de_resposta_é(int statusCode) throws Throwable {

        response.then().statusCode(statusCode).extract().response();

    }

}