package ir.syrent.enhancedvelocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import ir.syrent.enhancedvelocity.command.*
import ir.syrent.enhancedvelocity.storage.Settings
import ir.syrent.enhancedvelocity.vruom.VRUoMPlugin
import ir.syrent.enhancedvelocity.vruom.VRuom
import org.bstats.velocity.Metrics
import org.slf4j.Logger
import java.nio.file.Path

class EnhancedVelocity @Inject constructor(
    server: ProxyServer,
    logger: Logger,
    metricsFactory: Metrics.Factory,
    @DataDirectory dataDirectory: Path
) : VRUoMPlugin(server, logger, dataDirectory) {

    private val metricsFactory: Metrics.Factory
    var velocityVanishHook = false
        private set

    init {
        this.metricsFactory = metricsFactory
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        instance = this

        enableMetrics()
        registerDependencies()
        initializeInstances()
        registerCommands()
    }

    private fun initializeInstances() {
        Settings
    }

    private fun registerDependencies() {

        VRuom.log("Checking for VelocityVanish...")
        try {
            Class.forName("ir.syrent.velocityvanish.velocity.VelocityVanish")
            VRuom.log("VelocityVanish found. enabling hook...")
            velocityVanishHook = true
        } catch (_: ClassNotFoundException) {
            VRuom.log("VelocityVanish not found. disabling hook...")
        }
    }

    private fun enableMetrics() {
        val pluginID = 16753
        metricsFactory.make(this, pluginID)
    }

    private fun registerCommands() {
        GListCommand()
        FindCommand()
        SendCommand()
        AlertCommand()
        PingCommand()
        KickAllCommand()
    }

    companion object {
        lateinit var instance: EnhancedVelocity
            private set
    }
}