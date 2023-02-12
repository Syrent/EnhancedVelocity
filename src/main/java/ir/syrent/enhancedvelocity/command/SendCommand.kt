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

class SendCommand : SimpleCommand {

    init {
        VRuom.registerCommand("send", emptyList(), this)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.SEND)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.SEND))
            return
        }

        if (args.size < 2) {
            sender.sendMessage(Message.SEND_USAGE)
            return
        }

        val target = VRuom.getServer().getPlayer(args[0]).orElse(null)

        if (target == null) {
            sender.sendMessage(Message.PLAYER_NOT_FOUND)
            return
        }

        val server = VRuom.getServer().getServer(args[1]).orElse(null)

        if (server == null) {
            sender.sendMessage(Message.SERVER_NOT_FOUND)
            return
        }

        target.createConnectionRequest(server).fireAndForget()

        sender.sendMessage(Message.SEND_USE, TextReplacement("player", target.username), TextReplacement("server", server.serverInfo.name))
    }

}