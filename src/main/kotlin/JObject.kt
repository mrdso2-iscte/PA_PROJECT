import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties

data class JObject (

    val value: List<JObjectAttributes>


) : JValue  {
            private val listAttributes = mutableListOf<JObjectAttributes>()

    init {
        value.forEach {
            listAttributes.add(it)
        }
    }
    override fun toString(): String{

        return listAttributes.joinToString(",", "{", "}\n"){it.toString()}
    }

}



