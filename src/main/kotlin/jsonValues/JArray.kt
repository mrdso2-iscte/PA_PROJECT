package jsonValues
import visitors.JVisitor


/**
 * Represents a JSON object value.
 *
 * @property value is a list of [JValue] representing the attributes of the object.
 */
data class JArray (val value : List<JValue>): JValue {
    val listValues = mutableListOf<JValue>()

    /**
     * Initializes the [listValues] with the [value] provided.
     */
    init {
        value.forEach { listValues.add(it) }
    }

    /**
     * @return a string representation of the [JObject] value.
     */
    override fun toString(): String {
        return listValues.joinToString(",", "[", "]"){it.toString()}
    }

    /**
     * Accepts a [visitor] to perform an operation on the [JArray] value and its attributes.
     *
     * @param visitor the [JVisitor] to accept.
     */
    override fun accept(visitor: JVisitor) {
        visitor.visit(this)
        listValues.forEach {
            it.accept(visitor)}
    }


}