import annotations.AsJString
import annotations.ChangeName
import annotations.Ignore
import jsonValues.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

/**
 * A class that pass an Ojbect to JSON format
 */

class JInstatiatonPattern  {

    /**
     * Creates a [JValue] object based on the type of the given object [obj].
     *
     * @param obj The object to create a [JValue] from.
     * @return A [JValue] object corresponding to the type of [obj].
     */
    fun createObject(obj: Any?): JValue {

            return when (obj) {
                null -> JNull()
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
                    obj::class.memberProperties.filter { !it.hasAnnotation<Ignore>() }.forEach {
                        list.add(
                            JObjectAttribute(
                            if (it.hasAnnotation<ChangeName>()) it.findAnnotation<ChangeName>()!!.name else it.name,
                            if(it.hasAnnotation<AsJString>()) JString(it.call(obj).toString()) else createObject(it.call(obj)))
                        )
                    }
                    JObject(list)

                }
            }


    }




}