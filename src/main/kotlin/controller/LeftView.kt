package controller

import jsonValues.*
import java.awt.Component
import java.awt.GridLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class LeftView1(model: JObject): JPanel() {

    private val observers: MutableList<LeftView1Observer> = mutableListOf()
    fun addObserver(observer: LeftView1Observer) = observers.add(observer)

    init {

        layout= GridLayout(0,1)
        model.listAttributes.forEach {
            addObject(it)

        }
        model.addObserver(object : JObjectObserver {
            override fun attributeAdded(attribute: JObjectAttribute) {
                addObject(attribute)
            }
        })

    }


    private fun addObject(att: JObjectAttribute) {
        add(AttributeComponent(att))
       // add(testWidget(label,JNull))
        revalidate()
        repaint()
    }


    inner class AttributeComponent(att: JObjectAttribute) : JPanel() {

        inner class MouseClick(val first: Boolean) : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    val menu = JPopupMenu("Message")
                    val add = JButton("add")
                    add.addActionListener {
                        val text = JOptionPane.showInputDialog("text")

                        observers.forEach {
                            it.attributeAdded(JObjectAttribute(text,JNull))
                        }
                    }
                    menu.add(add);
                    menu.show(this@AttributeComponent, 100, 100)


                }

            }
        }
        init {
            addMouseListener(MouseClick(true))
            add(testWidget(att.label,att.value))





        }


    }
    fun testWidget(key: String, value: JValue): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            add(JLabel(key))
            val text = JTextField(value.toString())

            //para quando se dá update a um value na GUI
            text.addFocusListener(object : FocusAdapter() {
                override fun focusLost(e: FocusEvent) {
                    println("perdeu foco: ${text.text}")
                }
            })
            add(text)
        }


}

interface LeftView1Observer {
    fun attributeAdded(attribute: JObjectAttribute){}

}
