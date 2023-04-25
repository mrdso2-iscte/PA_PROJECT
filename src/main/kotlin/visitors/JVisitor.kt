package visitors

import jsonValues.JArray
import jsonValues.JObject
import jsonValues.JObjectAttribute

/**
 * Defines a visitor to pass through JSON values and their attributes.
 */
interface JVisitor {

    /**
     * Visits a [JObject] and performs an operation on it.
     * @param jObject the [JObject] to visit.
     */
    fun visit(jObject: JObject){}


    /**
     * Visits a [JArray] and performs an operation on it.
     * @param jArray the [JObject] to visit.
     */
    fun visit(jArray: JArray){}


    /**
     * Visits a [JLeaf] and performs an operation on it.
     * @param jLeaf the [JLeaf] to visit.
     */
    fun visit(jLeaf: JLeaf){}

    /**
     * Visits a [JObjectAttribute] and performs an operation on it.
     * @param jObjectAttribute the [JObjectAttribute] to visit.
     */
    fun visit(jObjectAttribute: JObjectAttribute){}
}