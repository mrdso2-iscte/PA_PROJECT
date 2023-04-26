package jsonValues

import visitors.JLeaf

/**
 * Represents a JSON Number value.
 *
 * @property value the Number value represented by this [JNumber].
 */
data class JNumber(
    private val value: Number
): JLeaf {

    /**
     * @return a string representation of the [JNumber] value.
     */
    override fun toString(): String{
        return "$value"
    }

}