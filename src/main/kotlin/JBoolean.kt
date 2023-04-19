class JBoolean (
    private val value: Boolean

): JValue {
    override fun toString(): String{
        return "$value"
    }

}