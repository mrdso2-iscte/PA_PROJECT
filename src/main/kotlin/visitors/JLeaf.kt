package visitors

import jsonValues.JValue


interface JLeaf: JValue {
     override fun accept(visitor: JVisitor) {
         visitor.visit(this)
     }

}