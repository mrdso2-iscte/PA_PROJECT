package visitors

import jsonValues.JArray
import jsonValues.JObject
import jsonValues.JObjectAttribute

interface JVisitor {
    fun visit(jObject: JObject){}
    fun visit(jArray: JArray){}
    fun visit(jLeaf: JLeaf){}
    fun visit(jObjectAttribute: JObjectAttribute){}
}