package baaahs.plugin.beatlink

import baaahs.gl.data.FeedContext
import baaahs.gl.data.singleUniformFeedContext
import baaahs.gl.glsl.GlslType
import baaahs.gl.patch.ContentType
import baaahs.gl.shader.InputPort
import baaahs.plugin.objectSerializer
import baaahs.show.Feed
import baaahs.show.FeedBuilder
import baaahs.show.FeedOpenContext
import baaahs.util.Clock
import kotlinx.serialization.SerialName

@SerialName("baaahs.BeatLink:BeatLink")
class BeatLinkFeed internal constructor(
    private val facade: BeatLinkPlugin.BeatLinkFacade,
    private val clock: Clock
) : Feed {
    override val pluginPackage: String get() = BeatLinkPlugin.id
    override val title: String get() = "BeatLink"
    override val contentType: ContentType get() = BeatLinkFeed.contentType

    override fun getType(): GlslType = GlslType.Float

    override fun open(feedOpenContext: FeedOpenContext, id: String): FeedContext =
        singleUniformFeedContext<Float>(id) {
            facade.beatData.fractionTillNextBeat(clock)
        }

    companion object {
        val contentType = ContentType("beat-link", "Beat Link", GlslType.Float)
    }

    inner class Builder : FeedBuilder<BeatLinkFeed> {
        override val title: String get() = "Beat Link"
        override val description: String
            get() = "A float representing the current beat intensity, between 0 and 1."
        override val resourceName: String get() = "BeatLink"
        override val contentType: ContentType get() = BeatLinkFeed.contentType
        override val serializerRegistrar
            get() = objectSerializer("${BeatLinkPlugin.id}:$resourceName", this@BeatLinkFeed)

        override fun looksValid(inputPort: InputPort, suggestedContentTypes: Set<ContentType>): Boolean =
            inputPort.contentType == BeatLinkFeed.contentType
                    || suggestedContentTypes.contains(BeatLinkFeed.contentType)
                    || inputPort.type == GlslType.Float

        override fun build(inputPort: InputPort): BeatLinkFeed = this@BeatLinkFeed
    }
}