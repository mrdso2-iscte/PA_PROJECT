package visitors

import jsonValues.JObject
import jsonValues.JValue
import kotlin.reflect.KClass

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
