# language: pt
# encoding UTF-8
@backend
Funcionalidade: Realizar Criacao de usuario

  @CriarFuncionario
  Esquema do Cenario: Criar usuario
    Dado que informo os dados do funcionario <name> <salary> <age>
    Quando envio uma requisição POST para "hml" "employee.create"
    Então o código de resposta é <statusCode>
    E o serviço retorna o token na respota

    Exemplos: 
      | name          | salary      |age         | statusCode |
      | "Tiago Cesar" | "5500,00"   | "34"       |        200 |

  @VerificarFuncionario
  Esquema do Cenario: Verificar Funcionario
    Dado que informo os dados do funcionario <name> <salary> <age>
    Quando envio uma requisição GET para "hml" "employee.verify"
    Então o código de resposta é <statusCode>
    E o serviço retorna o token na respota

    Exemplos:
      | name          | salary      |age         | statusCode |
      | "Tiago Cesar" | "5500,00"   | "34"       |        200 |


  @DeletarFuncionario
  Esquema do Cenario: Deletar Funcionario
    Dado que informo os dados do funcionario <name> <salary> <age>
    Quando envio uma requisição DELETE para "hml" "employee.delete"
    Então o código de resposta é <statusCode>

    Exemplos:
      | name          | salary      |age         | statusCode |
      | "Tiago Cesar" | "5500,00"   | "34"       |        200 |