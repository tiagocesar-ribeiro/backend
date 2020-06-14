package stepdefinition;

import commons.APIMethods;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import pojos.login.CriarFuncionarioObj;
import report.ExtentCucumberFormatter;

public class CriarUsuarioSteps {

    public static String resposta;
    public CriarFuncionarioObj criarFuncionarioObj = new CriarFuncionarioObj();



    @Dado("^que informo os dados do funcionario \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void que_informo_os_dados_do_funcionario(String name, String salary, String age) throws Throwable {
        criarFuncionarioObj.setName(name);
        criarFuncionarioObj.setSalary(salary);
        criarFuncionarioObj.setAge(age);

        Hooks.api.buildRequest(criarFuncionarioObj);
    }

    @Então("^o serviço retorna o token na respota$")
    public void tokenLogin() {
        //String LoginToken = APIMethods.response.then().extract().path("token").toString();
        resposta = APIMethods.response.getBody().asString();

        System.out.println("Teste realiza de login criado com sucesso:" + " " + resposta);

        ExtentCucumberFormatter.insertInfoTextInStepReport("Resultado:" + resposta);
    }
}

