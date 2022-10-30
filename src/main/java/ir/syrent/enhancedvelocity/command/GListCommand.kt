package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
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
        val canSeeVanishedPlayers = sender.hasPermission(Permissions.Commands.GLIST)

        sender.sendMessage(Message.GLOBALLIST_HEADER, TextReplacement("count", getNonVanishedPlayers().size.toString()))

        var counter = 1
        for (server in VRuom.getServer().allServers.sortedBy { it.playersConnected.size }.reversed()) {
            if (counter == 9) {
                break
            }
            counter++
            val nonVanishedPlayers = getNonVanishedPlayers(server)
            val serverName = server.serverInfo.name
            val progress = ProgressBar.progressBar(nonVanishedPlayers.size, VRuom.getServer().playerCount, Settings.progressCount, Settings.progressComplete, Settings.progressNotComplete)

            val playersContext = if (canSeeVanishedPlayers) {
                if (server.playersConnected.isEmpty()) {
                    Settings.formatMessage(Message.NO_ONE_PLAYING)
                } else {
                    Settings.formatMessage(formatPlayerList(server.playersConnected))
                }
            } else if (nonVanishedPlayers.isEmpty()) {
                Settings.formatMessage(Message.NO_ONE_PLAYING)
            } else {
                Settings.formatMessage(formatPlayerList(nonVanishedPlayers))
            }

            sender.sendMessage(
                Message.GLOBALLIST_SERVER,
                TextReplacement("players", playersContext),
                TextReplacement("progress", progress),
                TextReplacement("count", (
                        if (canSeeVanishedPlayers) server.playersConnected.size
                        else nonVanishedPlayers.size).toString()
                ),
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