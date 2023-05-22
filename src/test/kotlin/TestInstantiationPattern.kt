import annotations.*
import jsonValues.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

data class Inscrito(
    val numero: Int,
    val nome: String,
    val internacional: Boolean

)
data  class UnidadeCurricular(
    //@ChangeName("cadeira")
    val uc: String,
    //@AsJString
    val ects: Double,
    val dataExame: String? = null,
    //@Ignore
    val inscritos: List<Inscrito>

)
class TestInstationPattern {


    //for test purposes, comment the following annotations: @AsJString, @ChangeName("cadeira") and @Ignore
    @Test
    fun testCreateObject(){
        val i = JInstatiatonPattern()
       assertEquals(myObject, i.createObject(uc))
    }
    //for test purposes, comment the following annotations: @AsJString and @ChangeName("cadeira")
    @Test
    fun  testAnnotationIgnore(){
        val i = JInstatiatonPattern()
        val jObject = JObject(listOf(jUc, jEcts, jDt))
        assertEquals(jObject, i.createObject(uc))
    }

    //for test purposes, comment the following annotations:   @AsJString and  @Ignore
    @Test
    fun  testAnnotationChangeName(){
        val i = JInstatiatonPattern()
        val jUcName = JObjectAttribute("cadeira", JString("PA"))
        val json =  JObject(listOf(jUcName, jEcts, jDt, jIns) )
        assertEquals(json, i.createObject(uc))

    }
    //for test purposes, comment the following annotations:   //@Ignore e //@ChangeName("cadeira")
    @Test
    fun  testAnnotationAsString(){
        val i = JInstatiatonPattern()
        val jEctsString =JObjectAttribute("ects", JString("6.0"))
        val json =  JObject(listOf(jUc, jEctsString, jDt, jIns) )

        assertEquals(json, i.createObject(uc))


    }







}