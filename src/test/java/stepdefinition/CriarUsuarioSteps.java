package stepdefinition;

import commons.APIMethods;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import pojos.login.CriarFuncionarioSteps;
import report.ExtentCucumberFormatter;

public class CriarUsuarioSteps {

    public CriarFuncionarioSteps login = new CriarFuncionarioSteps();
    private String LoginToken;

    @Dado("^que informo os dados do funcionario \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void que_informo_os_dados_do_funcionario(String name, String salary, String age) throws Throwable {
        login.setName(name);
        login.setSalary(salary);
        login.setAge(age);

        Hooks.api.buildRequest(login);
    }

   /* @Então("^o serviço retorna o token na respota$")
    public void tokenLogin() {
        LoginToken = APIMethods.response.then().extract().path("token").toString();

        System.out.println("Token de login criado com sucesso:" + " " + LoginToken);

        ExtentCucumberFormatter.insertInfoTextInStepReport("Token:" + " " + LoginToken);*/
    }


