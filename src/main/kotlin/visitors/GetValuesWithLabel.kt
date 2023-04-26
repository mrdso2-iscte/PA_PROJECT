package visitors

import jsonValues.JObject
import jsonValues.JValue

/**
 * A [JVisitor] implementation that retrieves all [JValue] instances with a specified label
 * from a [JObject] instance and stores them in a mutable list.
 *
 * @property label the label  that is wantend
 * @property list the mutable list to store the all [JValue] instances the as the label
 */
class GetValuesWithLabel(private val label: String): JVisitor {
    val list  = mutableListOf<JValue>()

    /**
     * Retrieves all [JObjectAttribute] instances in  [jObject] with a label that matches
     * the [label] of this instance and stores the corresponding [JValue] instances in the [list].
     *
     * @param jObject the [JObject] instance to be visited
     */

    override fun visit(jObject: JObject) {
        jObject.listAttributes.forEach {
            if(it.label == label) list.add(it.value) }
    }
}