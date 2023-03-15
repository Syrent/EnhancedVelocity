package ir.syrent.enhancedvelocity.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import ir.syrent.enhancedvelocity.hook.VelocityVanishHook
import ir.syrent.enhancedvelocity.hook.VelocityVanishHook.getNonVanishedPlayers
import ir.syrent.enhancedvelocity.hook.VelocityVanishHook.getVanishedPlayerNames
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.utils.TextReplacement
import ir.syrent.enhancedvelocity.utils.sendMessage
import me.mohamad82.ruom.VRuom
import java.util.concurrent.CompletableFuture

class PingCommand : SimpleCommand {

    init {
        VRuom.registerCommand("ping", emptyList(), this)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (!sender.hasPermission(Permissions.Commands.PING)) {
            sender.sendMessage(Message.NO_PERMISSION, TextReplacement("permission", Permissions.Commands.PING))
            return
        }

        if (args.isNotEmpty()) {
            val target = VRuom.getServer().allPlayers.find { it.username.lowercase() == args[0].lowercase() }

            if (target == null || VelocityVanishHook.isVanished(target)) {
                sender.sendMessage(Message.PING_NO_TARGET)
                return
            }

            sender.sendMessage(Message.PING_USE_TARGET, TextReplacement("player", target.username), TextReplacement("ping", target.ping.toString()))
            return
        }

        if (sender !is Player) {
            sender.sendMessage(Message.ONLY_PLAYERS)
            return
        }

        sender.sendMessage(Message.PING_USE, TextReplacement("ping", sender.ping.toString()))
    }



    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val list = getNonVanishedPlayers().map { it.username }

        return if (list.isNotEmpty()) list.filter { it.lowercase().startsWith(invocation.arguments().last().lowercase()) }.sorted() else list
    }

    override fun suggestAsync(invocation: SimpleCommand.Invocation): CompletableFuture<List<String>> {
        val future = CompletableFuture<List<String>>()
        val list = mutableListOf<String>()
        val args = invocation.arguments()

        list.addAll(getNonVanishedPlayers().map { it.username })
        future.complete(if (args.isNotEmpty()) list.filter { it.lowercase().startsWith(args.last().lowercase()) }.sorted() else list)
        return future
    }

}