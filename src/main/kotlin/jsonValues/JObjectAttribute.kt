package jsonValues

import visitors.JVisitor

/**
 * Represents a single attribute of a JSON object.
 *
 * @property label the label of the attribute, represented as a [String].
 * @property value the value of the attribute, represented as a [JValue].
 */

class JObjectAttribute (
    val label: String,
    val value: JValue
): JVisitor
{


    /**
     * @return a string representation of the [JObjectAttribute] in JSON format.
     */
    override fun toString(): String{
        return "\"${label}\" : $value"
    }


    /**
     * Accepts a [visitor] to perform an operation on the [JObjectAttribute] and its value.
     *
     * If the [value] is an instance of [JObject] or [JArray], the [accept] method is called recursively
     * to visit its elements as well.
     *
     * @param visitor the [JVisitor] to accept.
     */
    fun accept(visitor: JVisitor) {
        visitor.visit(this)

        if(value::class.simpleName == "JObject")
        {val obj = value as JObject
            obj.accept(visitor)
        }
        if(value::class.simpleName == "JArray")
        {val jArray = value as JArray
            println("entrei")
            jArray.accept(visitor)
        }

    }


}