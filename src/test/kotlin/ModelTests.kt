import jsonValues.*
import org.junit.jupiter.api.Test
import visitors.GetObjectsWithLabels
import visitors.GetValuesWithLabel
import visitors.ValidateProperty
import visitors.ValidateStructure
import kotlin.test.assertEquals

class ModelTests{


    @Test
    fun testGetValuesWithLabel(){

        val ucList = GetValuesWithLabel("uc")
        myObject.accept(ucList)
        val a = listOf<JValue>(JString("PA"))
        assertEquals(a, ucList.list)


        val etcsList = GetValuesWithLabel("numero")
        myObject.accept(etcsList)
        val b = listOf<JValue>(JNumber(101101), JNumber(101102), JNumber(26503))
        assertEquals(b, etcsList.list)

    }
    @Test
    fun testGetObjectsWithLabels(){

        val ucList = GetObjectsWithLabels(listOf("uc","ects") )
        myObject.accept(ucList)
        val a = listOf<JValue>(myObject)
       assertEquals(a, ucList.list)

        val objList = GetObjectsWithLabels(listOf("numero", "nome") )
        myObject.accept(objList)
        val inscritos = listOf<JValue>(jIns1, jIns2, jIns3)

        assertEquals(inscritos, objList.list)
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
        val isCorrect = ValidateStructure()
        jInscritos.accept(isCorrect)
        assertEquals(true, isCorrect.validator)
    }
}

