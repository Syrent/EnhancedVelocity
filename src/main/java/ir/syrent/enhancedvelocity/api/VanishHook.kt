package ir.syrent.enhancedvelocity.api

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.UUID

interface VanishHook {

    fun isVanished(uniqueId: UUID): Boolean

    fun setVanished(uniqueId: UUID)

    fun setUnVanished(uniqueId: UUID)

    companion object {
        @JvmStatic
        val HANDLERS = listOf<VanishHook>()

        @JvmStatic
        fun isVanished(uniqueId: UUID): Boolean {
            return HANDLERS.any { it.isVanished(uniqueId) }
        }

        @JvmStatic
        fun hasVanishedPlayer(server: RegisteredServer): Boolean {
            return server.playersConnected.any { player -> isVanished(player.uniqueId) }
        }

        fun getVanishedPlayers(): Collection<Player> {
            return VRuom.getServer().allPlayers.filter { isVanished(it.uniqueId) }
        }

        fun getNonVanishedPlayers(): List<Player> {
            return VRuom.getServer().allPlayers.filter { !isVanished(it.uniqueId) }
        }

        fun getNonVanishedPlayers(server: RegisteredServer): Collection<Player> {
            return server.playersConnected.filter { !isVanished(it.uniqueId) }
        }
    }

}