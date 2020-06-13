# language: pt
# encoding UTF-8
@backend
Funcionalidade: Realizar Criacao de usuario

  @CriarFuncionario
  Esquema do Cenario: Criar usuario
    Dado que informo os dados de login <name> <salary> <age>
    Quando envio uma requisição POST para "hml" "loginUser"
    Então o código de resposta é <statusCode>


    Exemplos: 
      | name          | salary      |age         | statusCode |
      | "Tiago Cesar" | "5500,00"   | "34"       |        200 |
