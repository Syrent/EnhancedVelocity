package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import ir.syrent.enhancedvelocity.api.VanishHook
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.storage.Settings
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import ir.syrent.enhancedvelocity.vruom.VRuom
import java.util.concurrent.CompletableFuture

class FindCommand : SimpleCommand {

    init {
        VRuom.registerCommand(Settings.findCommand, Settings.findAliases, this)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.FIND)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.FIND))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(Message.FIND_USAGE)
            return
        }

        val targetPlayer = VRuom.getOnlinePlayers().find { it.username.lowercase() == args[0].lowercase() }

        if (targetPlayer == null) {
            sender.sendMessage(Message.FIND_NO_TARGET)
            return
        }

        val vanished = VanishHook.isVanished(targetPlayer.uniqueId)
        val server = targetPlayer.currentServer

        if (!server.isPresent && !vanished) {
            sender.sendMessage(Message.FIND_NO_SERVER)
            return
        }

        if (vanished && !sender.hasPermission(Permissions.Actions.FIND_VANISHED)) {
            sender.sendMessage(Message.FIND_NO_TARGET)
            return
        }

        sender.sendMessage(
            Message.FIND_USE,
            TextReplacement("player", targetPlayer.username),
            TextReplacement("server", if (server.isPresent) server.get().serverInfo.name else "Unknown"),
            TextReplacement("vanished", if (vanished && sender.hasPermission(Permissions.Actions.FIND_VANISHED)) Settings.formatMessage(Message.FIND_VANISHED) else "")
        )
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val list = VanishHook.getNonVanishedPlayers().map { it.username }

        return if (list.isNotEmpty()) list.filter { it.lowercase().startsWith(invocation.arguments().last().lowercase()) }.sorted() else list
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<List<String>> {
        val future = CompletableFuture<List<String>>()
        val list = mutableListOf<String>()
        val args = invocation.arguments()

        list.addAll(VanishHook.getNonVanishedPlayers().map { it.username })
        future.complete(if (args.isNotEmpty()) list.filter { it.lowercase().startsWith(args.last().lowercase()) }.sorted() else list)
        return future
    }
}