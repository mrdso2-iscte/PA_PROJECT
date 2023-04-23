import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class mainTest{


    val uc = JObjectAttribute("uc", JString("PA"))
    val ects = JObjectAttribute("ects", JNumber(6.0))
    val dt = JObjectAttribute("data-exame", JNull())

    val ins1= JObject(
        listOf(
            JObjectAttribute("numero", JNumber(101101)),
            JObjectAttribute("nome", JString("Dave Farley")),
            JObjectAttribute("internacional", JBoolean(true)))
    )
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

        val a = JArray(listOf(JString("a"), JNumber(2)))
        val isCorrect = ValidateStructure()
        inscritos.accept(isCorrect)
        assertEquals(true, isCorrect.validator)
    }
}

