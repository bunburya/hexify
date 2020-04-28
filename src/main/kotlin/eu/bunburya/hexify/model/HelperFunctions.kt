package eu.bunburya.hexify.model

import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.WritableImage
import java.io.File
import javax.imageio.ImageIO

fun WritableImage.saveToFile(path: String) {
    var file = File(path)
    var bufferedImage = SwingFXUtils.fromFXImage(this, null)
    ImageIO.write(bufferedImage, "png", file)
}
