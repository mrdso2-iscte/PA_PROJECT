class JObjectAttribute (
    private val label: String,
    private val value: JValue
)
{
    override fun toString(): String{
        return "\"${label}\" : ${value}"
    }



}