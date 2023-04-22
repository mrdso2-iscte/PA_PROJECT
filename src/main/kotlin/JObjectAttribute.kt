class JObjectAttribute (
    val label: String,
    val value: JValue
): JVisitor
{


    override fun toString(): String{
        return "\"${label}\" : $value"
    }

    fun accept(visitor: JVisitor) {
        visitor.visit(this)
        if(value::class.simpleName == "JObject")
        {val obj = value as JObject
            obj.accept(visitor)
        }
        if(value::class.simpleName == "JArray")
        {val jArray = value as JArray
            jArray.accept(visitor)
        }

    }


}