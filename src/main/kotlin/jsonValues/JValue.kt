package jsonValues
import visitors.JVisitor

/**
 * Represents a JSON value.
 */
interface JValue {
    /**
     * Accepts a [visitor] to perform an operation on the [JValue] and its attributes.
     * This method is designed to be overridden by implementing classes to enable the visitor pattern.
     *
     * @param visitor the [JVisitor] to accept.
     */
    fun accept(visitor: JVisitor){}

}


