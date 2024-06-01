package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.concurrent.CompletableFuture

class KickAllCommand : SimpleCommand {

    init {
        VRuom.registerCommand("kickall", emptyList(), this)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.KICKALL)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.KICKALL))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(Message.KICKALL_USAGE)
            return
        }

        val target = VRuom.getServer().allServers.find { it.serverInfo.name.lowercase() == args[0] }

        if (target == null) {
            sender.sendMessage(Message.KICKALL_NO_SERVER)
            return
        }

        target.playersConnected.filter { !it.hasPermission(Permissions.Actions.KICKALL_BYPASS) }.forEach { it.createConnectionRequest(VRuom.getServer().allServers.first()).fireAndForget() }
        sender.sendMessage(Message.KICKALL_USE, TextReplacement("server", target.serverInfo.name))
        return
    }
    
    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val list = VRuom.getServer().allServers.map { it.serverInfo.name }

        return if (list.isNotEmpty()) list.filter { it.lowercase().startsWith(invocation.arguments().last().lowercase()) }.sorted() else list
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<List<String>> {
        val future = CompletableFuture<List<String>>()
        val list = mutableListOf<String>()
        val args = invocation.arguments()

        list.addAll(VRuom.getServer().allServers.map { it.serverInfo.name })
        future.complete(if (args.isNotEmpty()) list.filter { it.lowercase().startsWith(args.last().lowercase()) }.sorted() else list)
        return future
    }



}