package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import ir.syrent.enhancedvelocity.hook.VelocityVanishHook
import ir.syrent.enhancedvelocity.hook.VelocityVanishHook.getNonVanishedPlayers
import ir.syrent.enhancedvelocity.hook.VelocityVanishHook.isVanished
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.storage.Settings
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import me.mohamad82.ruom.VRuom
import me.mohamad82.ruom.string.ProgressBar

class GListCommand : SimpleCommand {

    init {
        VRuom.registerCommand(Settings.globalListCommand, Settings.globalListAliases, this)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()

        if (!sender.hasPermission(Permissions.Commands.GLIST)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.GLIST))
            return
        }

        val canSeeVanishedPlayers = sender.hasPermission(Permissions.Actions.SEE_VANISHED)

        sender.sendMessage(Message.GLOBALLIST_HEADER, TextReplacement("count", getNonVanishedPlayers().size.toString()))

        var counter = 1
        val orderedServers = mutableMapOf<RegisteredServer, Collection<Player>>()
        for (server in VRuom.getServer().allServers) {
            val serverName = server.serverInfo.name
            val allPlayers = mutableListOf<Player>()

            if (canSeeVanishedPlayers) {
                allPlayers.addAll(server.playersConnected)
            } else {
                allPlayers.addAll(getNonVanishedPlayers(server))
            }

            if (Settings.servers.containsKey(serverName)) {
                val serverData = Settings.servers[serverName]

                if (serverData?.hidden == true) continue
                if (serverData?.summarizedServers?.isNotEmpty() == true) {
                    for (summarizedServerName in serverData.summarizedServers) {
                        allPlayers.addAll(
                            let {
                                val list = mutableListOf<Player>()
                                val selectedServer = VRuom.getServer().allServers.find { it.serverInfo.name == summarizedServerName }
                                selectedServer?.let {
                                    if (canSeeVanishedPlayers) {
                                        list.addAll(selectedServer.playersConnected)
                                    } else {
                                        list.addAll(getNonVanishedPlayers(selectedServer))
                                    }
                                }
                                list
                            }
                        )
                    }
                }
            }

            orderedServers[server] = allPlayers
        }

        for (orderedServer in orderedServers.toList().sortedBy { (_, value) -> value.size }.reversed().toMap()) {
            if (counter == 9) {
                break
            }
            counter++

            val server = orderedServer.key
            val serverName = server.serverInfo.name
            val allServerPlayers = orderedServer.value

            val progress = ProgressBar.progressBar(allServerPlayers.size, let { if (canSeeVanishedPlayers) VRuom.getServer().playerCount else VRuom.getServer().allPlayers.filter { !isVanished(it) }.size }, Settings.progressCount, Settings.progressComplete, Settings.progressNotComplete)

            val playersContext = if (allServerPlayers.isEmpty()) {
                Settings.formatMessage(Message.NO_ONE_PLAYING)
            } else {
                Settings.formatMessage(formatPlayerList(allServerPlayers))
            }

            sender.sendMessage(
                Message.GLOBALLIST_SERVER,
                TextReplacement("players", playersContext),
                TextReplacement("progress", progress),
                TextReplacement("count", allServerPlayers.size.toString()),
                TextReplacement("server",
                    if (VelocityVanishHook.hasVanishedPlayer(server) && canSeeVanishedPlayers)
                        Settings.formatMessage(Settings.serverVanishDecoration.replace("\$server", serverName))
                    else serverName
                )
            )
        }
    }

    private fun formatPlayerList(players: Collection<Player>): String {
        return "\n" + players.joinToString(", ") {
            player ->
                if (isVanished(player))
                    Settings.formatMessage(Settings.playerVanishDecoration.replace("\$player", player.username))
                else player.username
        }
    }

}