class JArray (value : List<JValue>): JValue {
    val listValues = mutableListOf<JValue>()
    init {
        value.forEach { listValues.add(it) }
    }

    override fun toString(): String {
        return listValues.joinToString(",", "[", "]"){it.toString()}
    }

    override fun accept(visitor: JVisitor) {
        visitor.visit(this)
        listValues.forEach {
            it.accept(visitor)}
    }


}