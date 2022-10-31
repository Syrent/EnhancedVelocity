package ir.syrent.enhancedvelocity.utils

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import ir.syrent.enhancedvelocity.storage.Message
import ir.syrent.enhancedvelocity.storage.Settings
import net.kyori.adventure.audience.Audience

fun CommandSource.sendMessage(message: Message, vararg replacements: TextReplacement) {
    Audience.audience(this).sendMessage(Settings.formatMessage(message, *replacements).component())
}

fun Player.sendMessage(message: Message, vararg replacements: TextReplacement) {
    Audience.audience(this).sendMessage(Settings.formatMessage(message, *replacements).component())
}

fun Player.sendActionbar(message: Message, vararg replacements: TextReplacement) {
    Audience.audience(this).sendActionBar(Settings.formatMessage(message, *replacements).component())
}
