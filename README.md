# PA_PROJECT - JSON API


### Instalação
Para proceder à instalação da API, seguir os seguintes passos:

1. Clonar o repositório git: `git clone https://github.com/mrdso2-iscte/PA_PROJECT/assets/99800589/0a1fb72d-0083-4d20-a7b4-530cc9727802`
2. Importar o projeto para uma IDE à escolha.
3. Colocar as dependências necessárias.

### Usabilidade
A API consiste em variadas classes e visitantes que permitem a realização de várias operações em dados de formato JSON. 
Segue-se um panorama das classes principais:

- `JValue`: Interface que representa um valor JSON. 
- `JObject`: Implementa a interface JValue. Representa um objeto JSON. 
- `JArray`: Implementa a interface JValue. Representa um array JSON.
- `JLeaf`:  Implementa a interface JValue. Interface que representa folhas JSON (valores sem sub-elementos).
- `JString`, `JNumber`, `JBoolean`: Implementam a interface JLeaf. Representam valores JSON de texto, número e boolean.
- `JVisitor`: Interface a ser implementada por visitantes para realizar operações em estruturas JSON.

São suportadas as seguintes operações:

1. Obter valores com dada "label": Use o visitor `GetValuesWithLabel` para obter todos os valores de um objeto JSON, que cumpram a condição de ter uma dada label.

2. Obter objetos com dada "label": Use o visitor `GetObjectsWithLabels` para obter todos os objetos de um objeto JSON, que cumpram a condição de ter uma dada label. 

3. Validar uma propriedade: Use o visitor `ValidateProperty` para validar se o tipo de uma propriedade de um objeto JSON corresponde a um tipo específico de `KClass`. 

4. Validar a estrutura: Use o visitor `ValidateStructure` para validar a estrutura de um array JSON, assegurando que todos os objetos nele contidos têm os mesmos tipos de atributos, labels e tipos.

### Exemplos

Seguem-se alguns exemplos que demonstram como usar a API:

1. Para criar um JString, JNumber, JBoolean, JNull:
```kotlin
val myString = JString("John")
val myNumber = JNumber(6.0)
val myBoolean = JBoolean(true)
val myNull = JNull()
```

2. Para criar um JObjectAttribute:
```kotlin
val myJObjectAttribute1 = JObjectAttribute("name", myString )
val myJObjectAttribute2 = JObjectAttribute("grade", myNumber)
```

3.Para criar um JObject:
```kotlin
val listAttributes = listOf(myJObjectAttribute1, myJObjectAttribute2)
val myObject = JObject(listAttributes)
```
4. Para criar um JArray de valores:
```kotlin
val listValues = listOf<JString>(JString("PA"), JString("MEI"))
val myArray = JArray(listValues)
```

### Visitantes
Neste projeto os visitantes permitem interagir com os valores JSON e os seus atributos. Seguem-se exemplos das 4 operações possíveis com estes visitantes:

1.Obter todos os objetos com dada "label" num determinado objeto:
```kotlin
val ucListFinder = GetObjectsWithLabels("uc")
myObject.accept(ucListFinder)
val values = ucListFinder.list
//Output: `values` irá conter todos os objetos JSON `myobject` que tenham a label "uc" num dos seus valoes
```

2. Obter todos valores com dada "label" num determinado objeto:
```kotlin
val ucListFinder = GetValuesWithLabel("uc")
myObject.accept(ucListFinder)
val values = ucListFinder.list
//Output: `values` irá conter todos os valores do objeto JSON `myobject` que tenham a label "uc"
```

3. Verificar se o valor associado a dada "label" corresponde a dada `KClass`:
```kotlin
val isCorrect = ValidateProperty("numero", JNumber::class)
myObject.accept(isCorrect)
val value = isCorrect.validator
//Output: `value` irá ser true
```
4. Verificar se o valor associado a dada "label" corresponde a dada `KClass`:
```kotlin
 val validator1 = ValidateStructure()
myArray.accept(validator1)
val result1 =  validator1.validator
//Output: `result1` irá ser true

val myArray2 = JArray(listOf<JValue>(JString("PA"), JNumber(9)))
val validator2 = ValidateStructure()
myArray2.accept(validator2)
val result2 = validator2.validator
//Output: `result2` irá ser false
```


### Anotações
De forma a conseguir personalizar os atributos de um JSON são usadas anotações, pela classe JInstatiatonPattern, nas propriedades de um objeto.
Segue-se abaixo a criação da data class UnidadeCurricular:

1. Anotação  @ChangeName
Caso queira mudar o nome de uma propriedade de um objeto JSON basta adiconar a anotação "@ChangeName"  por cima da propridade
```kotlin
annotation class ChangeName
data class UnidadeCurricular(
    @ChangeName("course")
    val uc: String
)
val uc = UnidadeCurricular("PA")
val i = JInstatiatonPattern().createObject(uc)
//Output do JSON = { "curso" : "PA"}
```

2. Anotação @AsString
Se pretender que uma proriedade seja sempre uma String, adicione a anotação "@AsString" por cima da propridade
```kotlin
annotation class AsString
data class UnidadeCurricular(
@ChangeName("course")
val ects: Double
)
val uc = UnidadeCurricular(6.0)
val i = JInstatiatonPattern().createObject(uc)
//Output do JSON = { "ects" : "6.0"}
```

3. Anotação @Ignore
   Se pretender que uma proriedade seja igorada, adicione a anotação "@Ignore" por cima da propridade
```kotlin 

annotation class Ignore
data class UnidadeCurricular(
@Ignore   
val uc: String,
val ects: Double
)
val uc = UnidadeCurricular("PA",6.0)
val i = JInstatiatonPattern().createObject(uc)
//Output do JSON = { "ects" : 6.0}
```
## GUI
Para inicializar a GUI, basta clicar em "Run" na função main do ficheiro `Controller.kt`. 
Esta visualização gráfica tem como propósito editar objetos JSON através de uma `LeftView` que permite ao utilizador adicionar, modificar ou eliminar atributos de um objeto JSON.
Existe também a opção de "undo", pelo que foi necessário assentar a arquitetura das classes sobre interfaces "Observer", que despoletarão as operações executadas e guardarão os comando a partir de uma classe controladora.
A ideia é então que seja um cliente do modelo anteriormente explicado.

Vídeo de utilização da GUI:
https://github.com/mrdso2-iscte/PA_PROJECT/assets/99800589/0a1fb72d-0083-4d20-a7b4-530cc9727802



