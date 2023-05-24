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
        layout = GridLayout(0, 1)
        model.listAttributes.forEach {
            addAttribute(it)

        }

        model.addObserver(object : JObjectObserver {
            override fun attributeAdded(attribute: JObjectAttribute) {
                addAttribute(attribute)
            }

            override fun attributeUpdated(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {
                if (newAttribute.value is JArray)
                    println("LEFT VIEW: O NOVO ATRIBUTO AGORA VAI SER UM ARRAY")
                updateWidget(oldAttribute, newAttribute) //SOLUCAO DO PROBLEMA É AQUI, PARA ISSO TEM DE DESCOBRIR QUAL É O PANEL QUE TEM AQUELA LABEL
            }
        })
        add(MenuComponent())
    }


    private fun updateWidget(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {
        val find = components.find { it is AttributeComponent && it.matches(oldAttribute.label) } as? AttributeComponent
        find?.let {
            find.modify(newAttribute)
        }

    }


    private fun addAttribute(attribute: JObjectAttribute) {
        add(AttributeComponent(attribute))
        revalidate()
        repaint()
    }


    private fun textToJValue(text: String): JValue {
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


    inner class MenuComponent() : JPanel() {
        inner class MouseClick() : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e != null) {
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    val menu = JPopupMenu("Message")
                    val add = JButton("add")
                    add.addActionListener {
                        val text = JOptionPane.showInputDialog("text")

                        observers.forEach {
                            it.componentAdded(JObjectAttribute(text, JNull))
                        }
                    }
                    menu.add(add)
                    menu.show(this@MenuComponent, 100, 100)
                }

            }
        }

        init {
            addMouseListener(MouseClick())
        }
    }


    inner class AttributeComponent(private val attribute: JObjectAttribute) : JPanel() {

        init {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            border = LineBorder(Color.ORANGE, 4, true)

            //MUDEI ISTO GUIDA: AGORA HA UMA LABELPANEL PARA POR A JLABEL
            val labelPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                val jlabel = JLabel(attribute.label)

                add(jlabel)
            }

            //MUDEI ISTO GUIDA: AGORA HA UM TEXTFIELDPANEL PARA POR O(S) JTEXTFIELD(S)
            val textFieldPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(createTextField(attribute.label, attribute.value))
            }



            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val add = JButton("add")

                        add.addActionListener {
                            println("LEFTVIEW: CLICASTE NUMA LABEL COM UM VALUE " + attribute.value.javaClass)
                            if (attribute.value is JArray) { //PROBLEMA GUIDA: ELE NAO DA UPDATE DO VALUE AQUI, ELE ACHA QUE TEM ALI UM JNULL PRA SEMPRE
                                println("LEFT VIEW: ESTAS A CARREGAR NUM ATRIBUTO QUE JA E ARRAY")
                                if ((attribute.value as JArray).listValues.add(JNull)) {
                                    val newAttribute = JObjectAttribute(attribute.label, attribute.value)
                                }
                            } else {
                                println("LEFT VIEW: ESTAS A CARREGAR NUM ATRIBUTO QUE NAO E ARRAY")
                                val newAttribute = JObjectAttribute(attribute.label, JArray(listOf(attribute.value, JNull)))
                                observers.forEach {
                                    //MUDEI ISTO GUIDA: AGORA O LEFTVIEW TB CHAMA O ATTRIBUTEMODIFIED PARA DAR O ATRIBUTO NOVO CUJO VALOR AGORA VAI SER UM JARRAY (QUE ESTA OVERRIDEN NO INIT)
                                    it.attributeModified(attribute, newAttribute)
                                }
                                textFieldPanel.add(createTextField(attribute.label, JNull))
                            }
                            repaint()
                            revalidate()
                        }
                        menu.add(add);
                        menu.show(this@AttributeComponent, 100, 100)
                    }
                }
            })
            add(labelPanel)
            add(textFieldPanel)
        }

        fun createTextField(label: String, value: JValue): JTextField =
            JTextField().apply {
                text = value.toString()
                addKeyListener(object : KeyAdapter() {
                    override fun keyPressed(e: KeyEvent) {
                        if (e?.keyCode == KeyEvent.VK_ENTER) {

                            observers.forEach {
                                val updatedAttribute = JObjectAttribute(label, textToJValue(text))
                                it.attributeModified(attribute, updatedAttribute)
                            }
                        }
                    }
                })
            }

        fun modify(newAttribute: JObjectAttribute) {
            print("modify: "  + newAttribute.value)
            attribute.value = newAttribute.value
        }

        fun matches(l: String) = attribute.label == l


    }


}

interface LeftViewObserver {
    fun componentAdded(attribute: JObjectAttribute) {}
    fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {}


}
