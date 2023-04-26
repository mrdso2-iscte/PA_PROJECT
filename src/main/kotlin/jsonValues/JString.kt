package jsonValues

import visitors.JLeaf

/**
 * Represents a JSON string value.
 *
 * @property value the boolean value represented by this [JString].
 */
data class JString (
    private val value: String

): JLeaf {
    /**
     * @return a string representation of the [JString] in JSON format.
     */
    override fun toString(): String{
        return "\"${value}\""
    }

}