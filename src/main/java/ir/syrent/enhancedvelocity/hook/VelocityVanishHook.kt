package ir.syrent.enhancedvelocity.hook

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import ir.syrent.enhancedvelocity.EnhancedVelocity
import ir.syrent.velocityvanish.velocity.VelocityVanish
import me.mohamad82.ruom.VRuom

object VelocityVanishHook {

    fun hasVanishedPlayer(server: RegisteredServer): Boolean {
        return if (EnhancedVelocity.instance.velocityVanishHook) {
            server.playersConnected.stream().anyMatch { player -> getVanishedPlayerNames().contains(player.username) }
        } else false
    }

    fun isVanished(player: Player): Boolean {
        return if (EnhancedVelocity.instance.velocityVanishHook) {
            getVanishedPlayerNames().contains(player.username)
        } else false
    }

    fun getVanishedPlayerNames(): Collection<String> {
        return VelocityVanish.instance.vanishedPlayers
    }

    fun getNonVanishedPlayers(): Collection<Player> {
        return if (EnhancedVelocity.instance.velocityVanishHook) {
            VRuom.getServer().allPlayers.filter { !getVanishedPlayerNames().contains(it.username) }
        } else VRuom.getServer().allPlayers
    }

    fun getNonVanishedPlayers(server: RegisteredServer): Collection<Player> {
        return if (EnhancedVelocity.instance.velocityVanishHook) {
            server.playersConnected.filter { !getVanishedPlayerNames().contains(it.username) }
        } else server.playersConnected
    }
}