package com.androdev.java.lsp

import android.app.Activity
import android.content.Context
import com.androdev.java.lsp.utils.JdtlsApi
import com.rk.exec.isTerminalInstalled
import com.rk.extension.ExtensionContext
import com.rk.file.child
import com.rk.file.sandboxHomeDir
import com.rk.icons.Icon
import com.rk.lsp.LspConnectionConfig
import com.rk.lsp.ProcessConnection
import com.rk.lsp.ScriptedLspServer
import io.github.rosemoe.sora.lsp.requests.Timeouts
import kotlinx.coroutines.launch
import java.io.File

class JavaServer(
    private val context: ExtensionContext,
    override val icon: Icon,
    override val supportedExtensions: List<String>,
    override val installScript: File
) : ScriptedLspServer() {

    override val id = "java"
    override val languageName = "Java"
    override val serverName = "jdtls"

    override val installId = "java"

    private fun fetchLatestVersion(): String {
        return JdtlsApi().fetchLatestVersion() ?: "jdt-language-server-1.59.0-202605111959.tar.gz"
    }

    override suspend fun isInstalled(context: Context): Boolean {
        if (!isTerminalInstalled()) {
            return false
        }

        return sandboxHomeDir().child(".lsp/java/bin/jdtls").exists()
    }

    override fun install(activity: Activity) {
        context.scope.launch {
            launchInstaller(activity, "--install", fetchLatestVersion())
        }
    }

    override fun uninstall(activity: Activity) {
        context.scope.launch {
            launchInstaller(activity, "--uninstall", fetchLatestVersion())
        }
    }

    override fun update(activity: Activity) {
        context.scope.launch {
            launchInstaller(activity, "--update", fetchLatestVersion())
        }
    }

    override suspend fun hasUpdate(context: Context): Boolean {
        val versionFile = sandboxHomeDir().child(".lsp/java/version.txt")
        val currentVersion = runCatching { versionFile.readText().trim() }.getOrNull()
        return currentVersion != null && currentVersion != fetchLatestVersion()
    }

    override fun getConnectionConfig(): LspConnectionConfig {
        val lspDir = File(sandboxHomeDir(), ".lsp/java")
        val launcherJar = File(lspDir, "plugins").listFiles()
            ?.firstOrNull { it.name.startsWith("org.eclipse.equinox.launcher_") && it.name.endsWith(".jar") }
            ?.absolutePath
        if (launcherJar == null) {
            return LspConnectionConfig.Process(arrayOf(File(lspDir, "bin/jdtls").absolutePath))
        }
        return LspConnectionConfig.Custom { instance ->
            ProcessConnection(arrayOf(
                "java",
                "-Djava.import.generatesMetadataFilesAtProjectRoot=false",
                "-Declipse.application=org.eclipse.jdt.ls.core.id1",
                "-Dosgi.bundles.defaultStartLevel=4",
                "-Declipse.product=org.eclipse.jdt.ls.core.product",
                "-Dlog.level=ALL",
                "-Xmx1G",
                "-jar", launcherJar,
                "-configuration", File(lspDir, "config_linux").absolutePath,
                "-data", File(lspDir, instance.projectRoot.getAbsolutePath()).absolutePath
            ), instance)
        }
    }

    override val customTimeouts = mapOf(Timeouts.INIT to 300000)
}