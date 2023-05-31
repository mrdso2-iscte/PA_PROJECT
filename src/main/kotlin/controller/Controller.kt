package controller


import command.*
import jsonValues.*
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JScrollPane


class Controller {

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
    val model = JObject(listOf( JObjectAttribute("cursos", JArray(listOf(JString("MEI"),JString("LEI"))))))
    val undoStack = mutableListOf<Command>()

    private val frame = JFrame("Json Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 3)
        size = Dimension(600, 600)


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

        fun addLeftViewObservers(leftView: LeftView, parentModel: JObject?) {
            val model = leftView.model
            leftView.addObserver(object: LeftViewObserver{
                override fun componentAdded(attribute: JObjectAttribute) {
                    val command = AddCommand(parentModel, model, attribute, 0)
                    undoStack.add(command)
                    command.run()
                }

                override fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute, position: Int) {
                    val command = UpdateCommand(parentModel, model, oldAttribute, newAttribute, position)
                    undoStack.add(command)
                    command.run()
                }


                override fun deleteAllObjects() {
                    val command = DeleteAllObjectsCommand(parentModel, model)
                    undoStack.add(command)
                    command.run()
                }

                override fun deleteObject(attribute: JObjectAttribute) {
                    val command = DeleteObjectCommand(parentModel, model, attribute )
                    undoStack.add(command)
                    command.run()
                }

                override fun deleteAttribute(attribute: JObjectAttribute, position: Int) {
                    val command = DeleteAttributeCommand(parentModel, model, attribute, position)
                    undoStack.add(command)
                    command.run()
                }

                override fun objectAdded(childLeftView: LeftView, parentModel: JObject) {
                addLeftViewObservers(childLeftView, parentModel)
            }
            })
        }

        addLeftViewObservers(leftView1, null)


        val right = RightView(model)
        add(right)

    }
    fun open() {
        frame.isVisible = true
    }
}

fun main(){
    Controller().open()
}

