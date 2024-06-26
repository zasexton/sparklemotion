package baaahs.plugin.beatlink

import baaahs.util.seconds
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class PlayerState(
    val trackTitle: String? = null,
    val trackArtist: String? = null,
    /** Each sample is 8 hex bytes: HHRRGGBB where 'H' is height. */
    private val encodedWaveform: String? = null,
    private val waveformScale: Int? = null,
    val trackStartTime: Instant? = null,
    val mode: String? = null,
    val isOnAir: Boolean? = null
) {
    val waveform: Waveform? get() = if (encodedWaveform != null && waveformScale != null) {
        Waveform(encodedWaveform, waveformScale)
    } else null

    val trackEndTime: Instant? get() = waveform?.totalTimeInSeconds?.let { totalTimeInSeconds ->
        if (trackStartTime != null) trackStartTime + totalTimeInSeconds.seconds else null
    }

    fun trackId() = listOfNotNull(trackTitle, trackArtist).joinToString(" – ")

    fun withWaveform(waveformScale: Int, block: Waveform.Builder.() -> Unit): PlayerState {
        val builder = Waveform.Builder(waveformScale)

        builder.block()

        return copy(
            encodedWaveform = builder.encodedWaveform.toString(),
            waveformScale = waveformScale
        )
    }

    companion object {
        /** Per [org.deepsymmetry.beatlink.Util#halfFrameToTime], there are 150 samples per second. */
        fun Int.asTotalTimeMs() = this * 100 / 15f
        fun secondsToFrameCount(seconds: Double) = (seconds * 150).roundToInt()
    }
}

@Serializable
data class PlayerStates(
    val byDeviceNumber: Map<Int, PlayerState> = emptyMap()
) {
    fun updateWith(deviceNumber: Int, playerState: PlayerState): PlayerStates {
        return copy(byDeviceNumber = byDeviceNumber + (deviceNumber to playerState))
    }

    fun updateWith(deviceNumber: Int, block: (PlayerState) -> PlayerState): PlayerStates {
        val oldPlayerState = byDeviceNumber[deviceNumber] ?: PlayerState()
        return updateWith(deviceNumber, block(oldPlayerState))
    }
}