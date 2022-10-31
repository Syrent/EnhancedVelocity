package ir.syrent.enhancedvelocity.storage

import ir.syrent.enhancedvelocity.EnhancedVelocity
import ir.syrent.enhancedvelocity.core.ServerData
import ir.syrent.enhancedvelocity.utils.TextReplacement
import me.mohamad82.ruom.utils.ResourceUtils
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File

object Settings {

    private val messages = mutableMapOf<Message, String>()

    val servers = mutableMapOf<String, ServerData>()

    lateinit var defaultLanguage: String
    lateinit var globalListCommand: String
    lateinit var globalListAliases: List<String>
    var progressCount = 45
    lateinit var progressComplete: String
    lateinit var progressNotComplete: String
    lateinit var playerVanishDecoration: String
    lateinit var serverVanishDecoration: String

    lateinit var findCommand: String
    lateinit var findAliases: List<String>

    init {
        load()
    }

    fun load() {
        val settingsYaml = YamlConfig("settings")
        settingsYaml.create()
        settingsYaml.load()
        val settingsRoot = settingsYaml.root!!

        defaultLanguage = settingsRoot.node("default_language").string ?: "en_US"

        val features = settingsRoot.node("features")
        val globalList = features.node("global_list")
        globalListCommand = globalList.node("command").string ?: "glist"
        globalListAliases = globalList.node("aliases").getList(String::class.java) ?: emptyList()

        val progress = globalList.node("progress")
        progressCount = progress.node("count").int
        progressComplete = progress.node("complete").string!!
        progressNotComplete = progress.node("not_complete").string!!

        val vanish = globalList.node("vanish")
        val decoration = vanish.node("decoration")
        playerVanishDecoration = decoration.node("player").string!!
        serverVanishDecoration = decoration.node("server").string!!

        servers.apply {
            this.clear()
            val serverSection = globalList.node("server")
            for (server in serverSection.childrenMap()) {
                val serverData = ServerData(
                    server.value.node("displayname").string,
                    server.value.node("sum").getList(String::class.java)?.toList(),
                    server.value.node("hidden").boolean,
                )
                this[server.key.toString()] = serverData
            }
        }

        val find = features.node("global_list")
        findCommand = find.node("command").string ?: "find"
        findAliases = find.node("aliases").getList(String::class.java) ?: emptyList()

        val languageYaml = YamlConfig("languages/${defaultLanguage}")
        languageYaml.create()
        languageYaml.load()
        val languageRoot = languageYaml.root!!

        messages.apply {
            this.clear()
            for (message in Message.values()) {
                if (message == Message.EMPTY) {
                    this[message] = ""
                    continue
                }

                var section: CommentedConfigurationNode? = null
                for (part in message.path.split(".")) {
                    section = if (section == null) languageRoot.node(part) else section.node(part)
                }

                this[message] = section?.string ?: "Cannot find message: ${message.name} in ${message.path}"
            }
        }

        settingsYaml.load()
        languageYaml.load()
    }


    fun formatMessage(message: String, vararg replacements: TextReplacement): String {
        var formattedMessage = message
            .replace("\$prefix", getMessage(Message.PREFIX))
            .replace("\$successful_prefix", getMessage(Message.SUCCESSFUL_PREFIX))
            .replace("\$warn_prefix", getMessage(Message.WARN_PREFIX))
            .replace("\$error_prefix", getMessage(Message.ERROR_PREFIX))
        for (replacement in replacements) {
            formattedMessage = formattedMessage.replace("\$${replacement.from}", replacement.to)
        }
        return formattedMessage
    }

    fun formatMessage(message: Message, vararg replacements: TextReplacement): String {
        return formatMessage(getMessage(message), *replacements)
    }

    fun formatMessage(messages: List<String>, vararg replacements: TextReplacement): List<String> {
        val messageList = mutableListOf<String>()
        for (message in messages) {
            messageList.add(formatMessage(message, *replacements))
        }

        return messageList
    }

    private fun getMessage(message: Message): String {
        return messages[message] ?: "Unknown message ($message)"
    }

    class YamlConfig constructor(private val name: String) {
        private var loader: YamlConfigurationLoader? = null
        var root: CommentedConfigurationNode? = null

        fun create() {
            val dataDirectory = EnhancedVelocity.instance.dataDirectory.toFile()
            dataDirectory.mkdir()

            val copyFile = File(dataDirectory, "$name.yml")

            if (!copyFile.exists()) {
                copyFile.parentFile.mkdirs()
                ResourceUtils.copyResource("$name.yml", copyFile)
            }

            loader = YamlConfigurationLoader.builder().file(copyFile).build()
        }

        fun load() {
            if (loader == null) {
                create()
            }

            root = loader!!.load()
        }

    }
}