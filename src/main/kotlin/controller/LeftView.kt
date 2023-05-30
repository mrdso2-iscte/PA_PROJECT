package controller

import jsonValues.*
import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.BorderFactory.createCompoundBorder

class LeftView(model: JObject) : JPanel() {

    private val observers: MutableList<LeftViewObserver> = mutableListOf()
    fun addObserver(observer: LeftViewObserver) = observers.add(observer)

    init {
        layout = GridLayout(0, 1)
        border = 	createCompoundBorder( BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(5,5,10,5))




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
            //layout = BoxLayout(this, BoxLayout.X_AXIS)
            layout = GridLayout(0, 2)

            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            border = 	createCompoundBorder( BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(10,10,10,10))




            val labelPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                val jLabel = JLabel(attribute.label)

                add(jLabel)
            }


            val textFieldPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                val newTextField = TextField( this, attribute.label, attribute.value, textFieldsList)
                add(newTextField)
                //textFieldCounter++
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

                                if(newList.all { it is JObject }){
                                    //muda isto

                                }


                                if (newList.add(JNull))  {
                                    val newAttribute = JObjectAttribute(attribute.label, JArray(newList))
                                    println("array old $attribute new $newAttribute")
                                    callUpdateObserver(attribute, newAttribute)
                                }
                            } else {
                                val newAttribute = JObjectAttribute(attribute.label, JArray(listOf(attribute.value, JNull)))
                                println("old $attribute new $newAttribute")
                                callUpdateObserver(attribute, newAttribute)
                            }

                            textFieldPanel.add(TextField(textFieldPanel, attribute.label, JNull,textFieldsList))
                            //textFieldCounter++
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




        fun modify(newAttribute: JObjectAttribute) {

            attribute.value = newAttribute.value




        }

        fun matches(l: String) = attribute.label == l
    inner class TextField(val panel: JPanel,label: String, value:JValue, val textFieldsList:MutableList<TextField>) : JTextField() {


        init {

            text = value.toString()
            addKeyListener(object : KeyAdapter() {
                override fun keyPressed(e: KeyEvent) {
                    if (e.keyCode == KeyEvent.VK_ENTER) {
                        var updatedAttribute = JObjectAttribute(label, textToJValue(text))
                        if(text.endsWith(":")) {
                            panel.remove(this@TextField)
                            repaint()
                            revalidate()
                            val newObject = JObject(listOf(JObjectAttribute(text.removeSuffix(":"), JNull)))
                            updatedAttribute = JObjectAttribute(attribute.label, JArray(listOf(newObject)))
                            addNewView(newObject)

                        }

                        if (attribute.value is JArray) {
                            val originalList = attribute.value as JArray
                            val newList = mutableListOf<JValue>()
                            newList.addAll(originalList.listValues)
                            newList[textFieldsList.indexOf(this@TextField)] = textToJValue(text)
                            updatedAttribute= JObjectAttribute(label,JArray(newList))
                        }
                        callUpdateObserver(attribute, updatedAttribute)

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
                                it.deleteAttribute(attribute, textFieldsList.indexOf(this@TextField))
                                //textFieldsList.remove(this@TextField)
                            }
                        }
                        menu.add(deleteButton)
                        menu.show(this@TextField, 100, 100)
                    }
                }
            })
            textFieldsList.add(this)
        }

        fun addNewView(newModel: JObject) {
            val newLeftView = LeftView(newModel)
            newLeftView.apply {

            }
            panel.add(newLeftView)
            newLeftView.addObserver(object : LeftViewObserver{
                override fun componentAdded(attribute: JObjectAttribute) {
                    newModel.add(attribute)
                }

                override fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {
                    newModel.update(oldAttribute, newAttribute)
                }

            })
        }



    }



    }



}

interface LeftViewObserver {
    fun componentAdded(attribute: JObjectAttribute) {}
    fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {}
    fun deleteAttribute(attribute: JObjectAttribute, position: Int) {}
    fun deleteObject(attribute: JObjectAttribute) {}
    fun deleteAllObjects() {}
}
