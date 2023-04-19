class JObjectAttributes (
    private val attribute: Pair<String,JValue>
)
{
    override fun toString(): String{
        return "\"${attribute.first}\" : ${attribute.second.toString()}"
    }


}