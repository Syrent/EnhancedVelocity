package org.sayandev.enhancedvelocity.api

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import org.sayandev.enhancedvelocity.feature.Features
import org.sayandev.enhancedvelocity.feature.features.hook.FeatureVanishHook
import org.sayandev.stickynote.velocity.server
import java.util.UUID

interface VanishHook {

    fun isVanished(uniqueId: UUID): Boolean

    fun register() {
        HANDLERS.add(this)
    }

    fun unregister() {
        HANDLERS.remove(this)
    }

    companion object {
        @JvmStatic
        val HANDLERS = mutableListOf<VanishHook>()

        @JvmStatic
        fun isVanished(uniqueId: UUID): Boolean {
            return HANDLERS.any { it.isVanished(uniqueId) } && Features.getFeature<FeatureVanishHook>().isActive()
        }

        @JvmStatic
        fun hasVanishedPlayer(server: RegisteredServer): Boolean {
            return server.playersConnected.any { player -> isVanished(player.uniqueId) }
        }

        @JvmStatic
        fun getVanishedPlayers(): Collection<Player> {
            return server.allPlayers.filter { player -> isVanished(player.uniqueId) }
        }

        @JvmStatic
        fun getNonVanishedPlayers(): List<Player> {
            return server.allPlayers.filter { player -> !isVanished(player.uniqueId) }
        }

        @JvmStatic
        fun getNonVanishedPlayers(server: RegisteredServer): Collection<Player> {
            return server.playersConnected.filter { !isVanished(it.uniqueId) }
        }
    }

}