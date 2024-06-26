package baaahs.plugin.beatlink

interface BeatLinkListener {
    fun onBeatData(beatData: BeatData)
    fun onPlayerStateUpdate(deviceNumber: Int, playerState: PlayerState)
}

class BeatLinkListeners {
    private val listeners = arrayListOf<BeatLinkListener>()

    fun addListener(listener: BeatLinkListener) { listeners.add(listener) }
    fun removeListener(listener: BeatLinkListener) { listeners.remove(listener) }

    fun notifyListeners(block: (BeatLinkListener) -> Unit) {
        listeners.forEach(block)
    }
}