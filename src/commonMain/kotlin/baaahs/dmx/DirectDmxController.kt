package baaahs.dmx

import baaahs.controller.Controller
import baaahs.controller.ControllerId
import baaahs.controller.ControllerState
import baaahs.fixtures.FixtureConfig
import baaahs.fixtures.FixtureMapping
import baaahs.fixtures.Transport
import baaahs.fixtures.TransportConfig
import baaahs.io.ByteArrayWriter
import baaahs.model.Model
import baaahs.scene.ControllerConfig
import baaahs.scene.FixtureMappingData
import baaahs.scene.MutableControllerConfig
import baaahs.scene.MutableDirectDmxControllerConfig
import baaahs.util.Clock
import baaahs.util.Time
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class DirectDmxController(private val device: Dmx.Device, clock: Clock) : Controller {
    override val controllerId: ControllerId
        get() = ControllerId(controllerType, device.id)
    private val startedAt = clock.now()
    override val state: ControllerState =
        State(device.name, "N/A", startedAt)
    override val defaultFixtureMapping: FixtureMapping?
        get() = null
    private val universe = device.asUniverse()

    override fun createTransport(
        entity: Model.Entity?,
        fixtureConfig: FixtureConfig,
        transportConfig: TransportConfig?,
        pixelCount: Int
    ): Transport = DirectDmxTransport(transportConfig as DmxTransportConfig)


    @Serializable
    data class State(
        override val title: String,
        override val address: String?,
        override val onlineSince: Time?
    ) : ControllerState()

    inner class DirectDmxTransport(private val transportConfig: DmxTransportConfig) : Transport {
        private val buffer = run {
            val (start, end) = transportConfig
            universe.writer(start, end - start + 1)
        }

        override val name: String
            get() = "DMX Transport"
        override val controller: Controller
            get() = this@DirectDmxController
        override val config: TransportConfig = transportConfig

        override fun deliverBytes(byteArray: ByteArray) {
            byteArray.forEachIndexed { i, byte -> buffer[i] = byte }
        }

        override fun deliverComponents(
            componentCount: Int,
            bytesPerComponent: Int,
            fn: (componentIndex: Int, buf: ByteArrayWriter) -> Unit
        ) {
            val buf = ByteArrayWriter(bytesPerComponent)
            for (componentIndex in 0 until componentCount) {
                buf.offset = 0
                fn(componentIndex, buf)

                val bytes = buf.toBytes()
                val startChannel = componentIndex * bytesPerComponent
                for (i in 0 until bytesPerComponent) {
                    buffer[startChannel + i] = bytes[i]
                }
            }
        }
    }

    override fun afterFrame() {
        universe.sendFrame()
    }

    override fun getAnonymousFixtureMappings(): List<FixtureMapping> = emptyList()

    companion object {
        val controllerType = "DMX"
    }
}

@Serializable
@SerialName("DirectDMX")
data class DirectDmxControllerConfig(
    override val title: String = "Direct DMX",
    override val fixtures: List<FixtureMappingData> = emptyList()
) : ControllerConfig {
    override val controllerType: String
        get() = DirectDmxController.controllerType

    override fun edit(): MutableControllerConfig =
        MutableDirectDmxControllerConfig(this)
}