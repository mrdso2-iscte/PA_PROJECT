package controller


import jsonValues.*
import java.awt.Dimension
import java.awt.GridLayout
import java.text.FieldPosition
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JScrollPane


class Help {

    /*

    val inscritos1 = JObject(listOf(
        JObjectAttribute("numero", JNumber(1)),
        JObjectAttribute("nome", JString("André")),
        JObjectAttribute("internacional", JBoolean(false))
    ))
    val inscritos2 = JObject(listOf(
        JObjectAttribute("numero", JNumber(2)),
        JObjectAttribute("nome", JString("Miguel")),
        JObjectAttribute("internacional", JBoolean(true))
    ))

    val cursos = JObject(listOf(
        JObjectAttribute("cursos", JArray(listOf(JString("MEI"), JString("LCD"))))
    ))

//    val model = JObject(listOf(  JObjectAttribute("inscritos", JArray(listOf(inscritos1, inscritos2)))))

     */
    val model = JObject(listOf( JObjectAttribute("cursos", JString("MEI"))))
    val undoStack = mutableListOf<Command>()

    private val frame=JFrame("Json Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 3)
        size = Dimension(600, 600)


    /**MARIA: Adicionei aqui o botão de Undo*/
        val undoButton = JButton("Undo").apply {
            addActionListener {
                if (undoStack.isNotEmpty()) {
                    val command = undoStack.removeAt(undoStack.size - 1)
                    command.undo()

                }
            }



        }
        add(undoButton)

        val leftView1=LeftView(model)
        val scrollPane = JScrollPane(leftView1).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }

        add(scrollPane)

        leftView1.addObserver(object : LeftViewObserver{
            override fun componentAdded(attribute: JObjectAttribute) {
                val command = AddCommand(model, attribute, 0)
                undoStack.add(command)
                command.run()
            }

            override fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute, position: Int) {

                val command = UpdateCommand(model, oldAttribute, newAttribute, position)
                undoStack.add(command)
                command.run()

            }

            override fun deleteAllObjects() {
              val command = DeleteAllObjectsCommand(model)
                undoStack.add(command)
                command.run()
            }

            override fun deleteObject(attribute: JObjectAttribute) {
               val command = DeleteObjectCommand(model, attribute )
                undoStack.add(command)
                command.run()
            }

            override fun deleteAttribute(attribute: JObjectAttribute, position: Int) {
                val command = DeleteAttributeCommand(model, attribute, position)
                undoStack.add(command)
                command.run()
            }
        })


        val right = RightView(model)
        add(right)

    }
    fun open() {
        frame.isVisible = true
    }
}

fun main(){
    Help().open()
}

interface Command {
    fun run()
    fun undo()
}
class AddCommand(private val model: JObject, private val attribute: JObjectAttribute, private val id : Int) : Command {
    override fun run() {
        model.add(attribute)
    }

    override fun undo() {
        model.deleteAttribute(attribute, id )
    }
}
class UpdateCommand(private val model: JObject, private val oldAttribute: JObjectAttribute, private val newAttribute: JObjectAttribute, private val position: Int ) : Command {

    private val oldValue = oldAttribute.value
    override fun run() {
        model.update(oldAttribute, newAttribute, position)
    }

    override fun undo() {
        model.update(newAttribute, JObjectAttribute(oldAttribute.label, oldValue), position)
    }
}
class DeleteObjectCommand(private val model: JObject, private val attribute: JObjectAttribute) : Command {
    override fun run() {
        model.objectDeleted(attribute)
    }

    override fun undo() {
        model.add(attribute)
    }
}

class DeleteAllObjectsCommand(private val model: JObject) : Command {
    private val oldModel= model
    override fun run() {
        model.deleteAll()
    }

    override fun undo() {
        oldModel.listAttributes.forEach { model.add(it) }

    }
}
class DeleteAttributeCommand(private val model: JObject, private val attribute: JObjectAttribute, private val position: Int) : Command {


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
    }
}