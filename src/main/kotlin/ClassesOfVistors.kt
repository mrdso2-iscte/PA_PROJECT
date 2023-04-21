import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

// obtem lista de atributos pela ordem do construtor primario
val KClass<*>.dataClassFields: List<KProperty<*>>
    get() {
        require(isData) { "instance must be data class" }
        return primaryConstructor!!.parameters.map { p ->
            declaredMemberProperties.find { it.name == p.name }!!
        }
    }

class GetValuesWithLabel(private val label: String): JVisitor{
    val list  = mutableListOf<JValue>()


    override fun visit(jObject: JObject) {
        jObject.listAttributes.forEach {
           if(it.label == label) list.add(it.value) }
    }

}

class GetObjectsWithLabels(private val labels: List<String>):JVisitor {
    val list = mutableListOf<JObject>()


    override fun visit(jObject: JObject) {

        jObject.listAttributes.forEach { it -> it::class.dataClassFields.forEach { println(it.name) } }


    }
}
