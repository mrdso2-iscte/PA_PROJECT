package controller

import jsonValues.JArray
import jsonValues.JObject
import jsonValues.JObjectAttribute
import jsonValues.JObjectObserver
import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.JTextArea

class RightView(private val model: JObject): JPanel(){
    init {
        layout = GridLayout()
        val srcArea = JTextArea()
        srcArea.tabSize = 2
        add(srcArea)
        srcArea.text = "$model"
        model.addObserver(object : JObjectObserver {
            override fun attributeAdded(attribute: JObjectAttribute) {
                println("Attributes: " + model.listAttributes)
                srcArea.text = "$model"
            }

            override fun attributeUpdated(oldAttribute: JObjectAttribute, newAttribute: JObjectAttribute) {
                srcArea.text = "$model"
                println(model.toString())
                if(newAttribute.value.javaClass == JArray::class.java)
                    println("RIGHT VIEW: RECEBI UM NOVO ATRIBUTO ARRAY")
            }
        })
    }


}