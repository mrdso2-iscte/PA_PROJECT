class JObject (

    private val value: List<JObjectAttribute>


) : JValue  {
    val listAttributes = mutableListOf<JObjectAttribute>()

    init {
        value.forEach {
            listAttributes.add(it)
        }
    }
    override fun toString(): String{

        return listAttributes.joinToString(",\n", "{\n", "\n}"){it.toString()}
    }

    override fun accept(visitor: JVisitor) {
        visitor.visit(this)
        listAttributes.forEach{
            it.accept(visitor)
        }

    }




}



