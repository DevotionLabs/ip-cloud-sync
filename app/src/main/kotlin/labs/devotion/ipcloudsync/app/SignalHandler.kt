package labs.devotion.ipcloudsync.app

import sun.misc.Signal

object SignalHandler {
    fun register(signalNames: List<String>, action: () -> Unit) {
        signalNames.forEach {
            register(it, action)
        }
    }

    private fun register(signalName: String, action: () -> Unit) {
        Signal.handle(Signal(signalName)) {
            action()
        }
    }
}
