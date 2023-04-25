package visitors

import jsonValues.JArray
import jsonValues.JObject
import jsonValues.JValue
import kotlin.reflect.KClass


/**
 * A [JVisitor] implementation that retrieves all [JValue] instances with a specified label
 * from a [JObject] instance and stores them in a mutable list.
 *
 * @property label the label  that is wantend
 * @property list the mutable list to store the all [JValue] instances the as the label
 */
class GetValuesWithLabel(private val label: String): JVisitor {
    val list  = mutableListOf<JValue>()

    /**
     * Retrieves all [JObjectAttribute] instances in  [jObject] with a label that matches
     * the [label] of this instance and stores the corresponding [JValue] instances in the [list].
     *
     * @param jObject the [JObject] instance to be visited
     */

    override fun visit(jObject: JObject) {
        jObject.listAttributes.forEach {
           if(it.label == label) list.add(it.value) }
    }
}

/**
 * A [JVisitor] implementation that retrieves all [JObject] instances with a specified labels
 * from a [JObject] instance and stores them in a mutable list.
 *
 * @property labels the labels that are wantend
 * @property list the mutable list to store the all [JObjects] instances the as all labels
 */

class GetObjectsWithLabels(private val labels: List<String>): JVisitor {
    val list = mutableListOf<JObject>()

    /**
     * Retrieves all [JObject] instances in the provided [jObject] that contain all of the
     * specified [labels] and stores them in the [list].
     *
     * @param jObject the [JObject] instance to be visited
     */
    override fun visit(jObject: JObject) {
        val attributesList= mutableListOf<String>()
        jObject.listAttributes.forEach {  attributesList.add(it.label)}
        if (attributesList.containsAll(labels)) list.add(jObject)
    }
}

/**
 * A [JVisitor] implementation that validates the [JValue] type of a [JObjectAttribute] with a
 * specified label against a specified [KClass] type.
 *
 * @property label the label of the [JObjectAttribute] to be validated
 * @property kClass the expected [KClass] type of the [JValue] of the [JObjectAttribute]
 * @property validator a boolean value indicating whether the validation passed or failed
 */
class ValidateProperty(private val label: String, private val kClass: KClass<*>): JVisitor {
    var validator = true //devia lançar exception??


    /**
     * Validates the [JValue] type of a [JObjectAttribute] with a specified [label] against
     * the expected [kClass] type. If the validation fails, sets the [validator] property to false.
     *
     * @param jObject the [JObject] instance to be visited
     */
    override fun visit(jObject: JObject) {
        jObject.listAttributes.forEach {
            if (it.label == label && it.value::class != kClass)
                validator = false
        }
    }
}


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

