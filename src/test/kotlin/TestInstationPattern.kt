import org.junit.jupiter.api.Test
data class Inscrito(
    val numero: Int,
    val nome: String,
    val internacional: Boolean

)
data  class UnidadeCurricular(
    val uc: String,
    val ects: Double,
    //isto nao funciona HELP
    val dataExame: String? = null,
    val inscritos: List<Inscrito>

)
class TestInstationPattern {
    val ins1 = Inscrito(101101, "Dave Farley",true)
    val ins2 = Inscrito(101102,"Martin Fowler",true)
    val ins3 = Inscrito(26503,"André Santos", false)

    val uc = UnidadeCurricular("PA", 6.0,  null, listOf<Inscrito>(ins1,ins2,ins3))

    @Test
    fun testCreateObject(){
        val i = JInstatiatonPattern()
        println(i.createObject(uc))
    }
}