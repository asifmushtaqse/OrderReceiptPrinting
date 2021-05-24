package climesoft

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.awt.Toolkit

class Main: Application() {
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("/main.fxml"))
        val scene = Scene(root, getWidthHeight()[0], getWidthHeight()[1])
        primaryStage.title = "Order Printing"
        primaryStage.scene = scene
        scene.stylesheets.add(javaClass.getResource("/stylesheet.css").toExternalForm())
        primaryStage.show()
    }

    private fun getWidthHeight(): DoubleArray {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val width = screenSize.getWidth()
        val height = screenSize.getHeight()
        return doubleArrayOf(width, height)
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}