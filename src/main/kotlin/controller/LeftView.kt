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

                println("LEFT VIEW: devia dar Update ${newAttribute.value}")
                if(newAttribute.value is JArray)
                    println("LEFT VIEW: O NOVO ATRIBUTO AGORA VAI SER UM ARRAY")
                    updateWidget(oldAttribute, newAttribute) //SOLUCAO DO PROBLEMA É AQUI, PARA ISSO TEM DE DESCOBRIR QUAL É O PANEL QUE TEM AQUELA LABEL
                  }


        })
        add(MenuComponent())

    }



    private fun updateWidget(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {


    }


    private fun addAttribute(attribute: JObjectAttribute) {
//        add(testWidget(attribute.label,attribute.value))
        add(AttributeComponent(attribute.label,attribute.value))
        revalidate()
        repaint()
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


    fun createTextField(label: String, value: JValue): JTextField =
        JTextField().apply {
            text = value.toString()
            addKeyListener(object : KeyAdapter(){
                override fun keyPressed(e: KeyEvent) {
                    if (e?.keyCode == KeyEvent.VK_ENTER) {
                        println("PARABENS!!!!!!!!!! CLICASTE NO ENTER PARA MUDAR UM VALOR")
                        observers.forEach {
                            val updatedAttribute = JObjectAttribute(label, textToJValue(text))
//                            print("TEXTFIELD ENTER: ${text}   ,  ${textToJValue(text)}" )
                            println("vou chamar o attributeModified : " + value)
                            it.attributeModified(JObjectAttribute(label,value), updatedAttribute)
                        }
                    }
                }
            })
        }

    fun  testWidget(label: String, value:JValue): JPanel =
        JPanel().apply {

            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            border= LineBorder(Color.ORANGE, 4, true)

            //MUDEI ISTO GUIDA: AGORA HA UMA LABELPANEL PARA POR A JLABEL
            val labelPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                val jlabel = JLabel(label)

                add(jlabel)
            }

            //MUDEI ISTO GUIDA: AGORA HA UM TEXTFIELDPANEL PARA POR O(S) JTEXTFIELD(S)
            val textFieldPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(createTextField(label, value))
            }



            addMouseListener(object : MouseAdapter(){
                override fun mouseClicked(e: MouseEvent?) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val add = JButton("add")

                        add.addActionListener{
                            println("LEFTVIEW: CLICASTE NUMA LABEL COM UM VALUE " + value.javaClass)
                            if(value.javaClass == JArray::class.java) { //PROBLEMA GUIDA: ELE NAO DA UPDATE DO VALUE AQUI, ELE ACHA QUE TEM ALI UM JNULL PRA SEMPRE
                                println("LEFT VIEW: ESTAS A CARREGAR NUM ATRIBUTO QUE JA E ARRAY")
                                if((value as JArray).listValues.add(JNull)) {
                                    val newAttribute = JObjectAttribute(label,value)}
                            }else{
                                println("LEFT VIEW: ESTAS A CARREGAR NUM ATRIBUTO QUE NAO E ARRAY")
                                val oldAttribute = JObjectAttribute(label, value)
                                val newAttribute = JObjectAttribute(label, JArray(listOf(value, JNull)))
                                observers.forEach {
                                    //MUDEI ISTO GUIDA: AGORA O LEFTVIEW TB CHAMA O ATTRIBUTEMODIFIED PARA DAR O ATRIBUTO NOVO CUJO VALOR AGORA VAI SER UM JARRAY (QUE ESTA OVERRIDEN NO INIT)
                                    it.attributeModified(oldAttribute, newAttribute) }
                                textFieldPanel.add(createTextField("N/A", JNull))
//                              textFieldPanel.add(JTextField("N/A"))
//                              model.update(oldAttribute, newAttribute)
                            }
                            repaint()
                            revalidate()
                        }
                        menu.add(add);
                        menu.show(this@apply, 100,100)
                    }
                }
            })

            add(labelPanel)
            add(textFieldPanel)

        }


    inner class AttributeComponent(private val label: String, private var value:JValue): JPanel(){
        init {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            border = LineBorder(Color.ORANGE, 4, true)

            //MUDEI ISTO GUIDA: AGORA HA UMA LABELPANEL PARA POR A JLABEL
            val labelPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                val jlabel = JLabel(label)

                add(jlabel)
            }

            //MUDEI ISTO GUIDA: AGORA HA UM TEXTFIELDPANEL PARA POR O(S) JTEXTFIELD(S)
            val textFieldPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(createTextField(label, value))
            }



            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")
                        val add = JButton("add")

                        add.addActionListener {
                            println("LEFTVIEW: CLICASTE NUMA LABEL COM UM VALUE " + value.javaClass)
                            if (value.javaClass == JArray::class.java) { //PROBLEMA GUIDA: ELE NAO DA UPDATE DO VALUE AQUI, ELE ACHA QUE TEM ALI UM JNULL PRA SEMPRE
                                println("LEFT VIEW: ESTAS A CARREGAR NUM ATRIBUTO QUE JA E ARRAY")
                                if ((value as JArray).listValues.add(JNull)) {
                                    val newAttribute = JObjectAttribute(label, value)
                                }
                            } else {
                                println("LEFT VIEW: ESTAS A CARREGAR NUM ATRIBUTO QUE NAO E ARRAY")
                                val oldAttribute = JObjectAttribute(label, value)
                                val newAttribute = JObjectAttribute(label, JArray(listOf(value, JNull)))
                                observers.forEach {
                                    //MUDEI ISTO GUIDA: AGORA O LEFTVIEW TB CHAMA O ATTRIBUTEMODIFIED PARA DAR O ATRIBUTO NOVO CUJO VALOR AGORA VAI SER UM JARRAY (QUE ESTA OVERRIDEN NO INIT)
                                    it.attributeModified(oldAttribute, newAttribute)
                                }
                                textFieldPanel.add(createTextField("N/A", JNull))
//                              textFieldPanel.add(JTextField("N/A"))
//                              model.update(oldAttribute, newAttribute)
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

        fun modify(newAttribute: JObjectAttribute) {
            value = newAttribute.value
        }


    }





















}

interface LeftViewObserver {
    fun componentAdded(attribute: JObjectAttribute) {}
    fun attributeModified(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {}


}
