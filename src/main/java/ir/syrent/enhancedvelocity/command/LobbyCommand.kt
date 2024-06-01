package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.net.InetSocketAddress

class LobbyCommand : SimpleCommand {

    init {
        VRuom.registerCommand("lobby", emptyList(), this)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val player = invocation.source() as? Player ?: return
        val address = player.virtualHost.map(InetSocketAddress::getHostString).map(String::lowercase).orElse(null)
        if (!address.contains("amobig")) {
            player.createConnectionRequest(VRuom.getServer().allServers.find { it.serverInfo.name.lowercase() == "lobby" }).fireAndForget()
        } else {
            player.createConnectionRequest(VRuom.getServer().allServers.find { it.serverInfo.name.lowercase() == "lobby-amobig" }).fireAndForget()
        }
    }

}