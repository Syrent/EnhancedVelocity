package ir.syrent.enhancedvelocity.storage

enum class Message(val path: String) {
    RAW_PREFIX("general.raw_prefix"),
    PREFIX("general.prefix"),
    CONSOLE_PREFIX("general.console_prefix"),
    SUCCESSFUL_PREFIX("general.successful_prefix"),
    WARN_PREFIX("general.warn_prefix"),
    ERROR_PREFIX("general.error_prefix"),
    ONLY_PLAYERS("general.only_players"),
    VALID_PARAMS("general.valid_parameters"),
    PLAYER_NOT_FOUND("general.player_not_found"),
    SERVER_NOT_FOUND("general.server_not_found"),
    NO_PERMISSION("command.no_permission"),
    GLOBALLIST_HEADER("features.global_list.header"),
    NO_ONE_PLAYING("features.global_list.no_one_playing"),
    GLOBALLIST_SERVER("features.global_list.server"),
    FIND_USAGE("features.find.command.usage"),
    SEND_USAGE("features.send.command.usage"),
    SEND_USE("features.send.command.use"),
    ALERT_USAGE("features.alert.command.usage"),
    ALERT_USE("features.alert.command.use"),
    FIND_USE("features.find.command.use"),
    FIND_VANISHED("features.find.command.vanished"),
    FIND_NO_SERVER("features.find.command.no_target"),
    FIND_NO_TARGET("features.find.command.no_target"),
    EMPTY("");
}