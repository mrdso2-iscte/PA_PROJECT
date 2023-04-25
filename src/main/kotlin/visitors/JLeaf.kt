package visitors

import jsonValues.JValue

/**
 * Represents a JSON leaf node, it represents all JSON values that do not contain any sub-elements.
 *
 * Implementing this interface indicates that a class represents a JSON value that cannot contain any other JSON values.
 */
interface JLeaf: JValue {

    /**
     * Accepts a [visitor] to perform an operation on the [JLeaf].
     * Since [JLeaf] values do not have any sub-elements, this method only calls the `visit()` method of the [visitor].
     *
     * @param visitor the [JVisitor] to accept.
     */
     override fun accept(visitor: JVisitor) {
         visitor.visit(this)
     }

}