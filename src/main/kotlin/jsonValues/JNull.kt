package jsonValues

import visitors.JLeaf

/**
 * Represents a JSON null value.
 */
class JNull: JLeaf {

    /**
     * @return a string representation of the [JNull] value.
     */
    override fun toString(): String{
        return "null"
    }

}