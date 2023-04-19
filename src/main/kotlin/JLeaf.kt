interface JLeaf:JValue {
     override fun accept(visitor: JVisitor) {
         visitor.visit(this)
     }

}