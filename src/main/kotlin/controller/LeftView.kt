package controller

import jsonValues.*
import java.awt.Color
import java.awt.Component
import java.awt.GridLayout
import java.awt.event.*
import javax.swing.*
import javax.swing.border.LineBorder

class LeftView(private val model: JObject) : JPanel() {

    private val observers: MutableList<LeftViewObserver> = mutableListOf()
    fun addObserver(observer: LeftViewObserver) = observers.add(observer)

    init {
        name = "dkjfdskj"
        layout = GridLayout(0, 1)
        model.listAttributes.forEach {
            addObject(it)

        }

        model.addObserver(object : JObjectObserver {
            override fun attributeAdded(attribute: JObjectAttribute) {
                addObject(attribute)
            }


        })
        add(AttributeComponent())

    }


    private fun addObject(attribute: JObjectAttribute) {
        add(testWidget(attribute.label,attribute.value))
        revalidate()
        repaint()
    }


    inner class AttributeComponent() : JPanel() {

        inner class MouseClick() : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e != null) {
                    println("bibibibi" + e.component)
                }
                println("carreguei")
                if (SwingUtilities.isRightMouseButton(e)) {
                    val menu = JPopupMenu("Message")
                    val add = JButton("add")
                    add.addActionListener {
                        val text = JOptionPane.showInputDialog("text")

                        observers.forEach {
                            it.componentAdded(JObjectAttribute(text, JNull))
                        }
                    }
                    menu.add(add);
                    menu.show(this@AttributeComponent, 100, 100)


                }

            }


        }


        init {
            addMouseListener(MouseClick())

        }
    }
    fun  textToJValue(text: String): JValue {
        return when {
            text.toIntOrNull() != null -> JNumber(text.toInt())
            text.toDoubleOrNull() != null -> JNumber(text.toDouble())
            text.toFloatOrNull() != null -> JNumber(text.toFloat())
            text.toLongOrNull() != null -> JNumber(text.toLong())
            text.equals("true", ignoreCase = true) -> JBoolean(true)
            text.equals("false", ignoreCase = true) -> JBoolean(false)
            text.isEmpty() -> JNull
            else -> JString(text)
        }

    }

    fun testWidget(label: String, value:JValue): JPanel =
        JPanel().apply {

            layout = GridLayout(1,0)

            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            border= LineBorder(Color.ORANGE, 4, true)

            //ADICIONAR UM ATTRIBUTECOMPONENT A ESTA LABEL PARA FAZER O ARRAY
            val jlabel = JLabel(label)
            val text = JTextField(value.toString())

            jlabel.addMouseListener(object : MouseAdapter(){
                override fun mouseClicked(e: MouseEvent?) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val add = JButton("add")

                        add.addActionListener{
                            if(value.javaClass == JArray::class.java) {
                                println("HELLO")
                            }else{
                                println("I AM NOT AN ARRAY")

                            }
                            add(JTextField("N/A"))
                            repaint()
                            revalidate()
                        }
                        menu.add(add);
                        menu.show(this@apply, 100,100)


                    }



                }
            })
            add(jlabel)



            //para quando se dá update a um value na GUI
            text.addKeyListener(object : KeyAdapter(){
                override fun keyPressed(e: KeyEvent) {
                    if (e?.keyCode == KeyEvent.VK_ENTER) {
                        observers.forEach {
                            val updatedAttribute = JObjectAttribute(label, textToJValue(text.text))
                            it.attributeModified(JObjectAttribute(label,value), updatedAttribute)
                        }
                    }
                }
          })
            add(text)


        }
}

interface LeftViewObserver {
    fun componentAdded(attribute: JObjectAttribute) {}
    fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {}


}
