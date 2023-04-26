package jsonValues
import visitors.JLeaf

/**
 * Represents a JSON boolean value.
 *
 * @property value the boolean value represented by [JBoolean].
 */
data class JBoolean (
    private val value: Boolean

): JLeaf {
    /**
     * @return a string representation of the [JBoolean] value.
     */
    override fun toString(): String{
        return "$value"
    }

}