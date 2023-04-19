interface JVisitor {
    fun visit(jObject: JObject){}
   // fun visit(jArray:JArray){}
    fun visit(jLeaf: JLeaf){}
}