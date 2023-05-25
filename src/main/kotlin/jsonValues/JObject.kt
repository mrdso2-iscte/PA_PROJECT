package jsonValues

import visitors.JVisitor
import java.text.FieldPosition

/**
 * Represents a JSON object value.
 *
 * @property[value] is a list of [JObjectAttribute] representing the attributes of the object.
 */
data class JObject(

     private val value: List<JObjectAttribute>


) : JValue {
    val listAttributes = mutableListOf<JObjectAttribute>()
    private val observers: MutableList<JObjectObserver> = mutableListOf()
    fun addObserver(observer: JObjectObserver) = observers.add(observer)


    /**
     * Initializes the [listAttributes] with the [value] provided.
     */
    init {
        value.forEach {
            listAttributes.add(it)
        }

    }
    /**
     * @return a string representation of the [JObject] value.
     */
    override fun toString(): String{

        return listAttributes.joinToString(",\n", "{\n", "\n}"){it.toString()}
    }
    /**
    * Accepts a [visitor] to perform an operation on the [JObject] value and its attributes.
    *
    * @param visitor the [JVisitor] to accept.
    */
    override fun accept(visitor: JVisitor) {
        visitor.visit(this)
        listAttributes.forEach{
            it.accept(visitor)
        }

    }

    fun add(attribute: JObjectAttribute){
        if(listAttributes.add(attribute))
        observers.forEach {
            it.attributeAdded(attribute) }
    }


    fun update(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute){
//        println("new attribute ${newAttribute.toString()}")
        if(listAttributes.remove(oldAttribute)){
            listAttributes.add(newAttribute)
            observers.forEach{
                it.attributeUpdated(oldAttribute,newAttribute)
            }
        }
    }
    fun objectDeleted(attribute: JObjectAttribute){
        if(listAttributes.remove(attribute)){
            observers.forEach{
                it.deleteObject(attribute)
            }
        }
    }
    fun deleteAttribute(attribute: JObjectAttribute, position: Int){

        if(attribute.value !is JArray)  objectDeleted(attribute)
        else{
            (attribute.value as JArray).listValues.removeAt(position)
            observers.forEach{
                it.deleteAttribute(attribute,position)
            }
            if((attribute.value as JArray).listValues.size==1){
                update(attribute,JObjectAttribute(attribute.label,(attribute.value as JArray).listValues[0]))
            }

        }

    }
    fun deleteAll(){
        listAttributes.clear()
        observers.forEach{
            it.allObjectsDeleted()
        }
    }


}
interface JObjectObserver{
    fun attributeAdded(attribute: JObjectAttribute){}

    fun attributeUpdated(oldAttribute: JObjectAttribute,newAttribute: JObjectAttribute){}

    fun deleteObject(attribute: JObjectAttribute){}
    fun deleteAttribute(attribute: JObjectAttribute, position: Int){}
    fun allObjectsDeleted(){}



}






