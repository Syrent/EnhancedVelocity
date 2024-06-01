package ir.syrent.enhancedvelocity.api

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.UUID

interface VanishHook {

    fun setIsVanished(uniqueId: UUID): Boolean

    fun setVanished(uniqueId: UUID)

    fun setUnVanished(uniqueId: UUID)

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
            return HANDLERS.any { it.setIsVanished(uniqueId) }
        }

        @JvmStatic
        fun hasVanishedPlayer(server: RegisteredServer): Boolean {
            return server.playersConnected.any { player -> isVanished(player.uniqueId) }
        }

        @JvmStatic
        fun getVanishedPlayers(): Collection<Player> {
            return VRuom.getServer().allPlayers.filter { isVanished(it.uniqueId) }
        }

        @JvmStatic
        fun getNonVanishedPlayers(): List<Player> {
            return VRuom.getServer().allPlayers.filter { !isVanished(it.uniqueId) }
        }

        @JvmStatic
        fun getNonVanishedPlayers(server: RegisteredServer): Collection<Player> {
            return server.playersConnected.filter { !isVanished(it.uniqueId) }
        }
    }

}