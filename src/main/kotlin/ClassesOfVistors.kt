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

class ValidateProperty(private val label: String, private val kClass: KClass<*>): JVisitor{
    var validator = true //lançar excessao caso falso??
    override fun visit(jObject: JObject) {
        jObject.listAttributes.forEach {
            if (it.label == label && it.value::class != kClass)
                validator = false
        }
    }
}
class ValidateStructure: JVisitor{
    var validator = true
    override fun visit(jArray: JArray) {
        val isEqual= jArray.listValues[0]
        jArray.listValues.forEachIndexed { index, it ->
            if (index >= 1) {
                println("$isEqual $it")
                if (isEqual::class == it::class) {
                    if (isEqual is JObject) {
                        validator = validateJobject(isEqual, it as JObject)
                    }

                } else validator = false
            }
        }
    }
    private fun validateJobject(obj1:JObject, obj2:JObject): Boolean{
        val list1 = mutableListOf<Pair<String, KClass<*>>>()
        obj1.listAttributes.forEach {  list1.add(Pair(it.label, it.value::class) ) }
        val list2 = mutableListOf<Pair<String, KClass<*>>>()
        obj2.listAttributes.forEach {  list2.add(Pair(it.label, it.value::class) ) }
        return list1==list2

    }



}

