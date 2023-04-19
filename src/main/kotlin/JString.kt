class JString (
    private val value: String

): JLeaf {
    override fun toString(): String{
        return "\"${value}\""
    }

}