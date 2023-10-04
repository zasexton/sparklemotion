package baaahs.control

import baaahs.app.ui.dialog.DialogPanel
import baaahs.app.ui.editor.EditableManager
import baaahs.app.ui.editor.GenericPropertiesEditorPanel
import baaahs.camelize
import baaahs.gadgets.ImagePicker
import baaahs.randomId
import baaahs.show.Control
import baaahs.show.Feed
import baaahs.show.live.*
import baaahs.show.mutable.MutableControl
import baaahs.show.mutable.MutableShow
import baaahs.show.mutable.ShowBuilder
import baaahs.ui.View
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
@SerialName("baaahs.Core:ImagePicker")
data class ImagePickerControl(
    /** The name for this imagePicker. */
    override val title: String,

    override val controlledFeedId: String
) : Control {
    override fun createMutable(mutableShow: MutableShow): MutableImagePickerControl {
        return MutableImagePickerControl(
            title,
            mutableShow.findFeed(controlledFeedId).feed
        )
    }

    override fun open(id: String, openContext: OpenContext): OpenControl {
        val controlledFeed = openContext.getFeed(controlledFeedId)
        val imagePicker = ImagePicker(title)
        openContext.registerGadget(id, imagePicker, controlledFeed)
        return OpenImagePickerControl(id, imagePicker, controlledFeed)
    }
}

data class MutableImagePickerControl(
    /** The name for this imagePicker. */
    override var title: String,

    val controlledFeed: Feed
) : MutableControl {
    override var asBuiltId: String? = null

    override fun getEditorPanels(editableManager: EditableManager<*>): List<DialogPanel> = listOf(
        GenericPropertiesEditorPanel(
            editableManager,
//            ImagePickerPropsEditor(this)
        )
    )

    override fun buildControl(showBuilder: ShowBuilder): ImagePickerControl {
        return ImagePickerControl(
            title,
            showBuilder.idFor(controlledFeed)
        )
    }

    override fun previewOpen(): OpenImagePickerControl {
        val imagePicker = ImagePicker(title)
        return OpenImagePickerControl(randomId(title.camelize()), imagePicker, controlledFeed)
    }
}

class OpenImagePickerControl(
    override val id: String,
    val imagePicker: ImagePicker,
    override val controlledFeed: Feed
) : FeedOpenControl() {
    override val gadget: ImagePicker
        get() = imagePicker

    override fun getState(): Map<String, JsonElement> = imagePicker.state

    override fun applyState(state: Map<String, JsonElement>) = imagePicker.applyState(state)

    override fun resetToDefault() {
        imagePicker.imageRef = null
    }

    override fun toNewMutable(mutableShow: MutableShow): MutableControl {
        return MutableImagePickerControl(
            imagePicker.title, controlledFeed
        )
    }

    override fun controlledFeeds(): Set<Feed> =
        setOf(controlledFeed)

    override fun getView(controlProps: ControlProps): View =
        controlViews.forImagePicker(this, controlProps)
}