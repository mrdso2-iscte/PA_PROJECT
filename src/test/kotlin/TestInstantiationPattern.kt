import annotations.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

data class Inscrito(
    val numero: Int,
    val nome: String,
    val internacional: Boolean

)
data  class UnidadeCurricular(
    @ChangeName("cadeira")
    val uc: String,
    @AsJString
    val ects: Double,
    val dataExame: String? = null,
    @Ignore
    val inscritos: List<Inscrito>

)
class TestInstationPattern {
    private val ins1 = Inscrito(101101, "Dave Farley",true)
    private val ins2 = Inscrito(101102,"Martin Fowler",true)
    private val ins3 = Inscrito(26503,"André Santos", false)

    private val uc = UnidadeCurricular("PA", 6.0, null,  listOf(ins1,ins2,ins3))

    @Test
    fun testCreateObject(){
        val i = JInstatiatonPattern()
        val json = "{\n" +
                "\"dataExame\" : null,\n" +
                "\"ects\" : 6.0,\n" +
                "\"inscritos\" : [{\n" +
                "\"internacional\" : true,\n" +
                "\"nome\" : \"Dave Farley\",\n" +
                "\"numero\" : 101101\n" +
                "},{\n" +
                "\"internacional\" : true,\n" +
                "\"nome\" : \"Martin Fowler\",\n" +
                "\"numero\" : 101102\n" +
                "},{\n" +
                "\"internacional\" : false,\n" +
                "\"nome\" : \"André Santos\",\n" +
                "\"numero\" : 26503\n" +
                "}],\n" +
                "\"uc\" : \"PA\"\n" +
                "}"
       assertEquals(json, i.createObject(uc).toString())
    }
    @Test
    fun  testAnnotations(){
        val i = JInstatiatonPattern()
        val json ="{\n" +
                "\"dataExame\" : null,\n" +
                "\"ects\" : \"6.0\",\n" +
                "\"cadeira\" : \"PA\"\n" +
                "}"
        assertEquals(json, i.createObject(uc).toString())


    }





}