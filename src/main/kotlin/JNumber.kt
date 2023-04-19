class JNumber(
    private val value: Number
): JValue {
    override fun toString(): String{
        return "$value"
    }

}