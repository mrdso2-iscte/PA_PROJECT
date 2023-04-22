import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class mainTest{
    /*
    //UC 1
    val ucname1 = JObjectAttribute("uc", JString("PA"))
    val ects1 = JObjectAttribute("ects", JNumber(6.0))
    val dt1 = JObjectAttribute("data-exame", JNull())
    val b = JObjectAttribute("s/n", JBoolean(true))
    val obj1 = JObject(listOf(ucname1, ects1, dt1,b))
    val uc1 = JObjectAttribute("uc1", obj1)
    //UC 2
    val ucname2 = JObjectAttribute("uc", JString("PO"))
    val ects2 = JObjectAttribute("ects", JNumber(3.0))
    val obj2 = JObject(listOf(ucname2, ects2))
    val uc2 = JObjectAttribute("uc2", obj2)
    //UC3
    val ucname3 = JObjectAttribute("uc", JString("IA"))
    //val ects3 = JObjectAttribute("ects", JNumber(6.0))
    val obj3 = JObject(listOf(ucname3))
    val uc3 = JObjectAttribute("uc3", obj3)
    */

    /////////////////////////////////////////////////
    val uc = JObjectAttribute("uc", JString("PA"))
    val ects = JObjectAttribute("ects", JNumber(6.0))
    val dt = JObjectAttribute("data-exame", JNull())

    val ins1= JObject(listOf(
        JObjectAttribute("numero", JNumber(101101)),
        JObjectAttribute("nome", JString("Dave Farley")),
        JObjectAttribute("internacional", JBoolean(true))))
    val ins2= JObject(listOf(
        JObjectAttribute("numero", JNumber(101102)),
        JObjectAttribute("nome", JString("Martin Fowler")),
        JObjectAttribute("internacional", JBoolean(true))))
    val ins3= JObject(listOf(
        JObjectAttribute("numero", JNumber(26503)),
        JObjectAttribute("nome", JString("André Santos")),
        JObjectAttribute("internacional", JBoolean(false))))
    val inscritos = JArray(listOf(ins1,ins2,ins3))

    val ins = JObjectAttribute("inscritos", inscritos)


    val myObject = JObject(listOf(uc,ects,dt,ins))



    @Test
    fun testGetValuesWithLabel(){

        val ucList = GetValuesWithLabel("uc")
        myObject.accept(ucList)
        val a = listOf<JValue>(JString("PA"))
        assertEquals(a.toString(), ucList.list.toString())


        val etcsList = GetValuesWithLabel("numero")
        myObject.accept(etcsList)
        val b = listOf<JValue>(JNumber(101101),JNumber(101102),JNumber(26503))
        assertEquals(b.toString(), etcsList.list.toString())

    }
    @Test
    fun testGetObjectsWithLabels(){

        val ucList = GetObjectsWithLabels(listOf("uc","ects") )
        myObject.accept(ucList)
        val a = listOf<JValue>(myObject)
        assertEquals(a.toString(), ucList.list.toString())

        val objList = GetObjectsWithLabels(listOf("numero", "nome") )
        myObject.accept(objList)
        val b = listOf<JValue>(ins1, ins2,ins3)
        assertEquals(b.toString(), objList.list.toString())
    }

    @Test
    fun testValidateProprety(){
        val isCorrect = ValidateProperty("numero", JNumber::class)
        myObject.accept(isCorrect)
        assertEquals(true,isCorrect.validator)

        val isIncorrect = ValidateProperty("numero", JString::class)
        myObject.accept(isIncorrect)
        assertEquals(false,isIncorrect.validator)
    }

    @Test
    fun testValidateStructure(){
        //duvida nao sei se é suposto fazer uma função que valide a estrutura do arry ou uma funçao que valide a estrutura de qualquer JValue
        val isCorrect = ValidateStructure("inscritos", listOf<String>("numero", "nome", "internacional") )
        myObject.accept(isCorrect)
        assertEquals(true, isCorrect.validator)
    }
}

