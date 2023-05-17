package controller

import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.JTextArea

class RightView : JPanel(){
    init {
        layout = GridLayout()
        val srcArea = JTextArea()
        srcArea.tabSize = 2
        srcArea.text = "TODO"
        add(srcArea)
    }
}