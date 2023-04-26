package visitors
import jsonValues.JObject


/**
 * A [JVisitor] implementation that retrieves all [JObject] instances with a specified labels
 * from a [JObject] instance and stores them in a mutable list.
 *
 * @property labels the labels that are wantend
 * @property list the mutable list to store the all [JObjects] instances the as all labels
 */

class GetObjectsWithLabels(private val labels: List<String>): JVisitor {
    val list = mutableListOf<JObject>()

    /**
     * Retrieves all [JObject] instances in the provided [jObject] that contain all of the
     * specified [labels] and stores them in the [list].
     *
     * @param jObject the [JObject] instance to be visited
     */
    override fun visit(jObject: JObject) {
        val attributesList= mutableListOf<String>()
        jObject.listAttributes.forEach {attributesList.add(it.label)}
        if (attributesList.containsAll(labels)) list.add(jObject)
    }
}
