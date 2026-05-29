package com.androdev.java.lsp

import android.content.Context
import com.rk.exec.isTerminalInstalled
import com.rk.file.child
import com.rk.file.sandboxHomeDir
import com.rk.icons.Icon
import com.rk.lsp.LspConnectionConfig
import com.rk.lsp.ScriptedLspServer
import java.io.File

class JavaServer(override val icon: Icon, override val installScript: File) : ScriptedLspServer() {
    override val id = "java"
    override val languageName = "Java"
    override val serverName = "jdtls"
    override val supportedExtensions = listOf("java")

    override val installId = "Java language server"

    override suspend fun isInstalled(context: Context): Boolean {
        if (!isTerminalInstalled()) {
            return false
        }

        return sandboxHomeDir().child(".lsp/java/bin/jdtls").exists()
    }

    override suspend fun isUpdatable(context: Context): Boolean {
        /*val versionFile = sandboxHomeDir().child(".lsp/java/version.txt")
        val currentVersion = runCatching { versionFile.readText().trim() }.getOrNull()
        return currentVersion != LATEST_VERSION*/
        return false;
    }

    override fun getConnectionConfig(): LspConnectionConfig {
        val lspDir = File(sandboxHomeDir(), ".lsp/java")
        val pluginsDir = File(lspDir, "plugins")
        val launcherJar = pluginsDir.listFiles()
            ?.firstOrNull { it.name.startsWith("org.eclipse.equinox.launcher_") && it.name.endsWith(".jar") }
            ?.absolutePath
        if (launcherJar == null) {
            return LspConnectionConfig.Process(arrayOf(File(lspDir, "bin/jdtls").absolutePath))
        }
        val configDir = File(lspDir, "config_linux").absolutePath
        val dataDir = File(lspDir, "/sdcard/Nost-Team/laser/nb-dev/").absolutePath
        val command = arrayOf(
            "java",
            "-Declipse.application=org.eclipse.jdt.ls.core.id1",
            "-Dosgi.bundles.defaultStartLevel=4",
            "-Declipse.product=org.eclipse.jdt.ls.core.product",
            "-Dlog.level=ALL",
            "-Xmx1G",
            "-jar", launcherJar,
            "-configuration", configDir,
            "-data", dataDir
        )
        return LspConnectionConfig.Process(command)
    }
}