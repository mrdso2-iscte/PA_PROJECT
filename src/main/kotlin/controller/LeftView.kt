package controller

import jsonValues.*
import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.BorderFactory.createCompoundBorder

class LeftView(val model: JObject) : JPanel() {

    val observers: MutableList<LeftViewObserver> = mutableListOf()
    fun addObserver(observer: LeftViewObserver) = observers.add(observer)

    init {
        layout = GridLayout(0, 1)
        border = createCompoundBorder( BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(5,5,10,5))
        model.listAttributes.forEach {
            addAttribute(it)
        }

        model.addObserver(object : JObjectObserver {
            override fun attributeAdded(attribute: JObjectAttribute) {
                addAttribute(attribute)
            }

            override fun attributeUpdated(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute, position: Int) {
                updateWidget(oldAttribute,newAttribute, position )
            }


            override fun deleteObject(attribute: JObjectAttribute) {
                val find = components.find { it is AttributeComponent && it.matches(attribute.label) } as? AttributeComponent
                find?.let {
                    remove(find)
                    revalidate()
                    repaint()
                }
            }

            override fun deleteAttribute(attribute: JObjectAttribute, position: Int) {
                val findComponent = components.find { it is AttributeComponent && it.matches(attribute.label) } as? AttributeComponent
                val textField = findComponent?.textFieldsList?.get(position)
                val findPanel=findComponent?.getComponent(1) as JPanel?

                findPanel?.remove(textField)
                findComponent?.textFieldsList?.remove(textField)
                findComponent?.revalidate()
                findComponent?.repaint()
            }

            override fun allObjectsDeleted() {
                components.forEach { if(it is AttributeComponent) remove(it) }
                revalidate()
                repaint()
            }

        })
        addMouseListener(MouseClick())

    }

    private fun addAttribute(attribute: JObjectAttribute) {
        add(AttributeComponent(attribute))
        revalidate()
        repaint()
    }

    private fun updateWidget(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute, position: Int) {
        val find = components.find { it is AttributeComponent && it.matches(oldAttribute.label) } as? AttributeComponent
        find?.modify(newAttribute, position)
    }

    private fun callUpdateObserver(attribute: JObjectAttribute, updatedAttribute: JObjectAttribute, position: Int) {
        observers.forEach {
            it.attributeModified(attribute, updatedAttribute, position)
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


        inner class MouseClick : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e != null) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                            val menu = JPopupMenu("Message")
                            val add = JButton("add")
                            val deleteAll = JButton("deleteAll")
                            add.addActionListener {
                                val text = JOptionPane.showInputDialog("text")
                                observers.forEach {
                                    it.componentAdded(JObjectAttribute(text, JNull))
                                }
                            }
                            deleteAll.addActionListener {
                                observers.forEach {
                                    it.deleteAllObjects()
                                }
                            }
                            menu.add(add)
                            menu.add(deleteAll)
                            menu.show(this@LeftView, 100, 100)

                    }
                }

            }
        }



    inner class AttributeComponent(private val attribute: JObjectAttribute) : JPanel() {

        var textFieldsList = mutableListOf<TextField>()

        init {
            layout = GridLayout(0, 2)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            border = createCompoundBorder( BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(10,10,10,10))

            val labelPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                val jLabel = JLabel(attribute.label)
                add(jLabel)
            }


            val textFieldPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                if(attribute.value is JArray && (attribute.value as JArray).listValues.all{it is JObject}) {
                    (attribute.value as JArray).listValues.forEach {
                        addNewView(model, it as JObject, this@apply) }
                }
                else if (attribute.value is JArray) {
                    (attribute.value as JArray).listValues.forEach {
                        add(TextField( this, attribute.label, it, textFieldsList)) }
                }
                else {
                    add(TextField(this, attribute.label, attribute.value, textFieldsList))
                }
            }

            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val addButton = JButton("add")
                        val deleteButton = JButton("delete")

                        addButton.addActionListener {
                             if (attribute.value is JArray) {
                                val originalList = attribute.value as JArray
                                val newList = mutableListOf<JValue>()
                                newList.addAll(originalList.listValues)
                                if (newList.add(JNull))  {
                                    val newAttribute = JObjectAttribute(attribute.label, JArray(newList))
                                    callUpdateObserver(attribute, newAttribute, newList.size - 1)
                                }
                            } else {
                                val newList =JArray(listOf(attribute.value, JNull))
                                val newAttribute = JObjectAttribute(attribute.label, newList)
                                callUpdateObserver(attribute, newAttribute, newList.listValues.size - 1)
                            }
                            repaint()
                            revalidate()
                        }
                        deleteButton.addActionListener {
                            observers.forEach {
                                it.deleteObject(attribute)
                            }
                        }
                        menu.add(addButton)
                        menu.add(deleteButton)
                        menu.show(this@AttributeComponent, 100, 100)
                    }
                }
            })
            add(labelPanel)
            add(textFieldPanel)
        }

        fun modify(newAttribute: JObjectAttribute, position: Int) {

            attribute.value = newAttribute.value
            if(newAttribute.value is JArray && (newAttribute.value as JArray).listValues.size > textFieldsList.size) {
                val textPanel = components[1] as JPanel
                val newTextField = TextField(textPanel, attribute.label, (newAttribute.value as JArray).listValues.get(position), textFieldsList)
                textPanel.add(newTextField)
            }
            else if ((newAttribute.value is JArray && (newAttribute.value as JArray).listValues.size < textFieldsList.size) ||
                (newAttribute.value !is JArray && position>0)) {
                    val textPanel = components[1] as JPanel
                    val textField = textPanel.getComponent(position)
                    textPanel.remove(textField)
                    textFieldsList.removeAt(position)
                }
            else {
                val textField = textFieldsList[position]
                textField.text = if (newAttribute.value is JArray) {
                    (newAttribute.value as JArray).listValues[position].toString()
                } else {
                    newAttribute.value.toString()
                }
                textField.repaint()
                textField.revalidate()
            }
        }


        fun matches(l: String) = attribute.label == l

    inner class TextField(val panel: JPanel,label: String, value:JValue, val textFieldsList:MutableList<TextField>) : JTextField() {

        init {
            text = value.toString()
            addKeyListener(object : KeyAdapter() {
                override fun keyReleased(e: KeyEvent) {
                    if (e.keyCode == KeyEvent.VK_ENTER) {
                        var updatedAttribute = JObjectAttribute(label, textToJValue(text))
                        if(text.endsWith(":")) {
                            val newLabel = text.removeSuffix(":")
                            panel.remove(this@TextField)
                            repaint()
                            revalidate()
                            val newObject = JObject(listOf(JObjectAttribute(newLabel, JNull)))
                            updatedAttribute = if(attribute.value is JArray) addValueToArray(newObject)
                            else JObjectAttribute(attribute.label, JArray(listOf(newObject)))
                            addNewView(model, newObject, panel)
                        }
                        else if(attribute.value is JArray) updatedAttribute = addValueToArray(textToJValue(text))
                        callUpdateObserver(attribute, updatedAttribute, textFieldsList.indexOf(this@TextField))
                    }
                }
            })

                addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent?) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            val menu = JPopupMenu("Message")
                            val deleteButton = JButton("delete")
                            deleteButton.addActionListener {
                                observers.forEach {
                                    if( attribute.value is JArray) it.deleteAttribute(attribute, textFieldsList.indexOf(this@TextField))
                                    else it.deleteObject(attribute)
                                }
                            }
                            menu.add(deleteButton)
                            menu.show(this@TextField, 100, 100)
                        }
                    }
                })

            textFieldsList.add(this)
        }

        fun addValueToArray(newValue: JValue): JObjectAttribute {
            val originalList = attribute.value as JArray
            val newList = mutableListOf<JValue>()
            newList.addAll(originalList.listValues)
            newList[textFieldsList.indexOf(this@TextField)] = newValue
            return JObjectAttribute(attribute.label,JArray(newList))
        }


    }
        fun addNewView(model: JObject, newModel: JObject, panel: JPanel) {
            val newLeftView = LeftView(newModel)
            panel.add(newLeftView)
            observers.forEach {
                it.objectAdded(newLeftView, model) }
        }
    }

}

interface LeftViewObserver {
    fun componentAdded(attribute: JObjectAttribute) {}
    fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute, position: Int) {}
    fun deleteAttribute(attribute: JObjectAttribute, position: Int) {}
    fun deleteObject(attribute: JObjectAttribute) {}
    fun deleteAllObjects() {}
    fun objectAdded(childLeftView: LeftView, parentModel: JObject) {}

}
