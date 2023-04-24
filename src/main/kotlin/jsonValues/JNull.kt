package jsonValues

import visitors.JLeaf

class JNull: JLeaf {
    override fun toString(): String{
        return "null"
    }

}