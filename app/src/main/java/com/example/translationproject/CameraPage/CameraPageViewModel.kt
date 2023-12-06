import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.translationproject.Util.TextHolder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executors

class CameraPageViewModel(private val textHolder: TextHolder): ViewModel() {
    var panelIsOpen: Boolean by mutableStateOf(false)
    var errorPanelIsOpen: Boolean by mutableStateOf(false)
    var currentlyDetectedText: String by mutableStateOf("")
    val executor = Executors.newSingleThreadExecutor()
    val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun storeCameraText(text: String){
        textHolder.storeNewText(text)
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    fun takePhoto(cameraController: LifecycleCameraController){
        cameraController.setImageAnalysisAnalyzer(executor) { imageProxy ->
            imageProxy.image?.let {image ->
                val img = InputImage.fromMediaImage(
                    image,
                    imageProxy.imageInfo.rotationDegrees
                )
                textRecognizer.process(img).addOnCompleteListener { task ->
                    if(!task.isSuccessful || task.result.text == "")
                        errorPanelIsOpen = true
                    else{
                        currentlyDetectedText = task.result.text
                        panelIsOpen = true
                    }

                    cameraController.clearImageAnalysisAnalyzer()
                    imageProxy.close()
                }
            }
        }
    }
}