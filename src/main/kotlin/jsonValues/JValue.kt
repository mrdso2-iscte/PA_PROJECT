package jsonValues

import visitors.JVisitor

interface JValue {
    fun accept(visitor: JVisitor){}

}


