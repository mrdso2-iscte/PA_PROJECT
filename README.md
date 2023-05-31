# PA_PROJECT
omo a utilizá-la nas várias vertentes (simples, visitors, reflexão/anotações, observadores).

# Simple
Na file test/kotlin/ObjectTester.kt encontra-se um varivável myObject, uma instância de um um JObject que representa um objeto JSON, este vai ser usado nas classes testes que se encontram na mesma pasta

A classe test/kotlin/ModelTests permite validar algumas funcionalidades que devem ser implementadas pelo objeto JSON, para as testar basta correr as funções que se encontram nesta classe

O file  test/kotlin/TestInstamtionPattern.kt, mostra o funcionamento das anotações criadas, "ChangeName", "AsJString" e "Ignore", da mesma forma que na classe a cima, para consigar executar os testes bast dar run em cada função, no entanto tenha atenção aos comentários a cima de cada teste pois será necessário comentar algumas das anotações 

# GUI
O file Help.kt permite lançar uma GUI de forma a interagir com um objeto JSON, onde apresenta as seguintes funcionalidades. De referir para que todos os menus não visiveis apareceçam é necessarios corregar com o botão do lado diretio do rato para as seguintes funcionalidades funcionarem:
-criar um atributo com uma label correspondete, carregando no painel exterior e seguidamente no botão de "add"
- para um atributo é possível adicionar valores, carregando na label e no botão de "add"
- para eliminar um valor de uma tributo basta carregar no seu TextField e clicar no botão de "Eliminar"
- para dar update num valor de um atributo, é só escrever o desejado na TextField e dar ENTER
- se pretender criar um Araay de objeos Json basta que escreva o desejado no TextField com ":" no final do mesmo e por fim carregar ENTER
- é tambem possivel apagar um atributo independentemente, para tal, basta carregar na label e no botão eliminar
- para eleminar todo o objeto JSON basta carregar na parte que envolve os atributos e apasrcerá um botão com a descrição de "DeleteAll"
Por fim existe um botão de undo do lado esquerdo da GUI que permite retroceder todas as tarefas realizadas.


