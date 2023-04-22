import kotlin.reflect.KClass
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
        val attributesList= mutableListOf<String>()
        jObject.listAttributes.forEach {  attributesList.add(it.label)}
        if (attributesList.containsAll(labels)) list.add(jObject)
    }
}

class ValidateProperty(val label: String , val kClass: KClass<*>): JVisitor{
    var validator = true //lançar excessao caso falso??
    override fun visit(jObject: JObject) {
        jObject.listAttributes.forEach {
            if (it.label == label && it.value::class != kClass)
                validator = false
        }
    }
}
class ValidateStructure(val label: String, val structure: List<String>):JVisitor{


    var validator = true
    //nao sei o que é suposto fazer
    override fun visit(jObject: JObject) {

    }



}