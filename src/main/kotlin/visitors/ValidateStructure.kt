package visitors
import jsonValues.JArray
import jsonValues.JObject
import jsonValues.JValue
import kotlin.reflect.KClass


/**
 * A [JVisitor] implementation that validates the structure of a [JArray] instance, ensuring that
 * all contained [JObject] instances have the same attributes labels when it is an [JObject] and attribute types.
 *
 * @property validator a boolean value indicating whether the validation passed or failed
 */
class ValidateStructure: JVisitor {
    var validator = true

    /**
     * Visit a [JArray] and set the validator to false if any [JObject] in the array has different attributes than the first [JObject]
     * or if the [JValue] types are different .
     *
     * @param jArray The [JArray] to visit
     */
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

    /**
     * Validates that two [JObject]s have the same attributes with the same types.
     * @param obj1 the first [JObject] to compare.
     * @param obj2 the second [JObject] to compare.
     * @return `true` if the objects have the same attributes with the same types,
     * `false` otherwise.
     */
    private fun validateJobject(obj1: JObject, obj2: JObject): Boolean{
        val list1 = mutableListOf<Pair<String, KClass<*>>>()
        obj1.listAttributes.forEach {  list1.add(Pair(it.label, it.value::class) ) }
        val list2 = mutableListOf<Pair<String, KClass<*>>>()
        obj2.listAttributes.forEach {  list2.add(Pair(it.label, it.value::class) ) }
        return list1==list2

    }



}

