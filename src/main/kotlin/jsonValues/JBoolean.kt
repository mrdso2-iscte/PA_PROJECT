package jsonValues

import visitors.JLeaf

class JBoolean (
    private val value: Boolean

): JLeaf {
    override fun toString(): String{
        return "$value"
    }

}