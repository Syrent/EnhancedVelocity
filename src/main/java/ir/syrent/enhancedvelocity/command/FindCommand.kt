package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import ir.syrent.enhancedvelocity.hook.VelocityVanishHook
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.storage.Settings
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import me.mohamad82.ruom.VRuom
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


        val vanished = VelocityVanishHook.isVanished(targetPlayer)
        val server = targetPlayer.currentServer

        if (!server.isPresent && !vanished) {
            sender.sendMessage(Message.FIND_NO_SERVER)
            return
        }

        sender.sendMessage(
            Message.FIND_USE,
            TextReplacement("player", targetPlayer.username),
            TextReplacement("server", if (server.isPresent) server.get().serverInfo.name else "Unknown"),
            TextReplacement("vanished", if (vanished) Settings.formatMessage(Message.FIND_VANISHED) else "")
        )
    }

    override fun suggest(invocation: SimpleCommand.Invocation): MutableList<String> {
        if (invocation.arguments().isNotEmpty()) {
            return VelocityVanishHook.getNonVanishedPlayers().map { it.username }.toMutableList()
        }
        return mutableListOf()
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<MutableList<String>> {
        val future = CompletableFuture<MutableList<String>>()
        if (invocation.arguments().isNotEmpty()) {
            future.complete(VelocityVanishHook.getNonVanishedPlayers().map { it.username }.toMutableList())
        }
        return future
    }
}