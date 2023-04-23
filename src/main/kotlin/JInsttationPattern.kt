import kotlin.reflect.full.memberProperties

class JInstatiatonPattern  {


    fun createObject(obj: Any?): JValue {

        return when (obj) {
            null-> JNull()
            is Boolean -> JBoolean(obj)
            is String -> JString(obj)
            is Int -> JNumber(obj)
            is Double -> JNumber(obj)
            is Float -> JNumber(obj)
            is Enum<*> -> JString(obj.name)
            is Map<*, *> -> {
                val list = obj.map { (k, v) ->
                    JObjectAttribute(k.toString(), createObject(v!!))
                }.toMutableList()
                JObject(list)
            }
            is Collection<*> -> JArray(obj.map { createObject(it!!) })
            else -> {
                val list = mutableListOf<JObjectAttribute>()
                obj::class.memberProperties.forEach {
                    list.add(JObjectAttribute(it.name, createObject(it.call(obj))))
                }
                JObject(list)

            }
        }

    }



}