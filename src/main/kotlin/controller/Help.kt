package controller


import jsonValues.*
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JScrollPane


class Help {

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
    val model = JObject(listOf( JObjectAttribute("cursos", JNull)))

    private val frame=JFrame("Json Editor").apply {
        defaultCloseOperation = javax.swing.JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(600, 600)

    val leftView1=LeftView(model)
    val scrollPane = JScrollPane(leftView1).apply {
        horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
    }
    add(scrollPane)

    leftView1.addObserver(object : LeftViewObserver{
        override fun componentAdded(attribute: JObjectAttribute) {
            model.add(attribute)
        }

        override fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {

            model.update(oldAttribute, newAttribute)
        }

        override fun deleteAllObjects() {
            model.deleteAll()
        }

        override fun deleteObject(attribute: JObjectAttribute) {
            model.objectDeleted(attribute)
        }

        override fun deleteAttribute(attribute: JObjectAttribute, position: Int) {
            model.deleteAttribute(attribute, position)
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