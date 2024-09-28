package org.sayandev.enhancedvelocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.sayandev.stickynote.loader.velocity.StickyNoteVelocityLoader
import org.slf4j.Logger
import java.nio.file.Path

class EnhancedVelocity @Inject constructor(
    val server: ProxyServer,
    val logger: Logger,
    @DataDirectory val dataDirectory: Path
)  {
    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        StickyNoteVelocityLoader.load(this, "enhancedvelocity", server, logger, dataDirectory)
    }
}