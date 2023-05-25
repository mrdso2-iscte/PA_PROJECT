package controller

import jsonValues.*
import java.awt.Color
import java.awt.Component
import java.awt.GridLayout
import java.awt.event.*
import javax.swing.*
import javax.swing.border.LineBorder

class LeftView(model: JObject) : JPanel() {

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
                updateWidget(oldAttribute, newAttribute) 
            }
        })
        add(MenuComponent())
    }

    private fun addAttribute(attribute: JObjectAttribute) {
        add(AttributeComponent(attribute))
        revalidate()
        repaint()
    }

    private fun updateWidget(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {
        val find = components.find { it is AttributeComponent && it.matches(oldAttribute.label) } as? AttributeComponent
        find?.let {
            find.modify(newAttribute)
        }
    }

    private fun callUpdateObserver(attribute: JObjectAttribute, updatedAttribute: JObjectAttribute) {
        observers.forEach {
            it.attributeModified(attribute, updatedAttribute)
        }
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


    inner class MenuComponent : JPanel() {
        inner class MouseClick : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e != null) {

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

            val labelPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                val jLabel = JLabel(attribute.label)

                add(jLabel)
            }
            var textFieldCounter = 0
            val textFieldPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                createTextField(attribute.label, attribute.value,this, textFieldCounter)
                textFieldCounter++
            }


            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val addButton = JButton("add")

                        addButton.addActionListener {
                            if (attribute.value is JArray) {
                                var newList = attribute.value as JArray
                                if(newList.listValues.add(JNull)) {
                                    val newAttribute = JObjectAttribute(attribute.label, newList)

                                    callUpdateObserver(attribute, newAttribute)
                                }

                            } else {
                                val newAttribute = JObjectAttribute(attribute.label, JArray(listOf(attribute.value, JNull)))
                                callUpdateObserver(attribute, newAttribute)
                            }
                            createTextField(attribute.label, JNull, textFieldPanel,textFieldCounter)
                            textFieldCounter++
                            repaint()
                            revalidate()
                        }
                        menu.add(addButton)
                        menu.show(this@AttributeComponent, 100, 100)
                    }
                }
            })
            add(labelPanel)
            add(textFieldPanel)
        }



        fun createTextField(label: String, value: JValue, textFieldPanel: JPanel, position: Int): JTextField {
            val textField = JTextField()
            textField.apply {
                text = value.toString()
                addKeyListener(object : KeyAdapter() {
                    override fun keyPressed(e: KeyEvent) {
                        if (e.keyCode == KeyEvent.VK_ENTER) {
                            var updatedAttribute = JObjectAttribute(label, textToJValue(text))
                            if (attribute.value is JArray) {
                                val newList= (attribute.value as JArray)
                                newList.listValues[position] = textToJValue(text)
                                updatedAttribute= JObjectAttribute(label,newList)
                            }
                            callUpdateObserver(attribute, updatedAttribute)
                        }
                    }
                })
            }

            textFieldPanel.add(textField)
            return textField
        }


        fun modify(newAttribute: JObjectAttribute) {
            attribute.value = newAttribute.value
        }

        fun matches(l: String) = attribute.label == l
    }


}

interface LeftViewObserver {
    fun componentAdded(attribute: JObjectAttribute) {}
    fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {}


}
