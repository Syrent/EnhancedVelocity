package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom

class AlertCommand : SimpleCommand {

    init {
        VRuom.registerCommand("alert", emptyList(), this)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.ALERT)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.ALERT))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(Message.ALERT_USAGE)
            return
        }

        val message = args.joinToString(" ")

        for (player in VRuom.getServer().allServers.map { it.playersConnected }.flatten()) {
            player.sendMessage(Message.ALERT_USE, TextReplacement("message", message))
        }
    }

}