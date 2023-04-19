class JString (
    private val value: String

): JValue {
    override fun toString(): String{
        return "\"${value}\""
    }

}