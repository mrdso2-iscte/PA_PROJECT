package controller


import jsonValues.JNull
import jsonValues.JObject
import jsonValues.JObjectAttribute
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JScrollPane


class Help {

    val model = JObject(listOf( JObjectAttribute("ola", JNull)))

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

    leftView1.addObserver(object : LeftView1Observer{
        override fun attributeAdded(attribute: JObjectAttribute) {
            model.add(attribute)
           println( model.toString())
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