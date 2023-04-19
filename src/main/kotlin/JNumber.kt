class JNumber(
    private val value: Number
): JLeaf {
    override fun toString(): String{
        return "$value"
    }

}