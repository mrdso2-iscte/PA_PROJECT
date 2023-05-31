package command

import jsonValues.*

interface Command {
    fun run()
    fun undo()
}
class AddCommand(private val parentModel: JObject?, private val model: JObject, private val attribute: JObjectAttribute, private val id : Int) : Command {
    override fun run() {
        model.add(attribute)
        parentModel?.update()
    }

    override fun undo() {
        model.deleteAttribute(attribute, id )
        parentModel?.update()
    }
}
class UpdateCommand(private val parentModel: JObject?, private val model: JObject, private val oldAttribute: JObjectAttribute, private val newAttribute: JObjectAttribute, private val position: Int ) : Command {

    private val oldValue = oldAttribute.value
    override fun run() {
        model.update(oldAttribute, newAttribute, position)
        parentModel?.update()
    }

    override fun undo() {
        model.update(newAttribute, JObjectAttribute(oldAttribute.label, oldValue), position)
        parentModel?.update()
    }
}
class DeleteObjectCommand(private val parentModel: JObject?, private val model: JObject, private val attribute: JObjectAttribute) : Command {
    override fun run() {
        model.objectDeleted(attribute)
        parentModel?.update()
    }

    override fun undo() {
        model.add(attribute)
        parentModel?.update()
    }
}

class DeleteAllObjectsCommand(private val parentModel: JObject?, private val model: JObject) : Command {
    private val oldModel= mutableListOf<JObjectAttribute>()
    override fun run() {
        oldModel.addAll(model.listAttributes)
        model.deleteAll()
        parentModel?.update()
    }

    override fun undo() {
        oldModel.forEach {
            model.add(it)
        }
        parentModel?.update()

    }
}
class DeleteAttributeCommand(private val parentModel: JObject?, private val model: JObject, private val attribute: JObjectAttribute, private val position: Int) : Command {

    private var oldValue: JValue = JNull

    override fun run() {
        attribute.value.let {
            oldValue = if(it is JArray){
                it.listValues[position]
            }else{
                it
            }
        }
        model.deleteAttribute(attribute, position)
        parentModel?.update()
    }

    override fun undo() {
        val newValue = if(attribute.value is JArray){
            val list = (attribute.value as JArray).listValues.toMutableList()
            list.add(oldValue)
            JArray(list)
        }else{
            JArray(listOf(attribute.value, oldValue))
        }
        model.update(attribute, JObjectAttribute(attribute.label,newValue), newValue.listValues.size-1 )
        parentModel?.update()
    }
}