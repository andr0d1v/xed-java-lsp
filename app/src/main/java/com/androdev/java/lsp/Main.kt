package com.androdev.java.lsp

import androidx.annotation.Keep
import com.rk.extension.ExtensionAPI
import com.rk.extension.ExtensionContext
import com.rk.file.BuiltinFileType
import com.rk.file.child
import com.rk.lsp.LspRegistry
import com.rk.utils.getTempDir
import kotlinx.coroutines.runBlocking
import java.io.File

@Keep
@Suppress("unused")
class Main(context: ExtensionContext) : ExtensionAPI(context) {
    private var javaServer: JavaServer? = null

    override fun onLoad() {
        val javaFileType = BuiltinFileType.JAVA

        javaServer =
            JavaServer(
                context = context,
                icon = javaFileType.icon!!,
                supportedExtensions = listOf("java"),
                installScript = acquireLspInstallScript()
            ).also {
                LspRegistry.registerServer(it)
            }
    }

    private fun acquireLspInstallScript(): File {
        val javaAssetStreams = context.assets.open("java-lsp.sh")
        val javaAsset = javaAssetStreams.bufferedReader().use { it.readText() }
        val javaLspScript =
            getTempDir().child("java-lsp.sh").also {
                it.writeText(javaAsset)
            }
        return javaLspScript
    }

    override fun onDispose() {
        javaServer?.let {
            LspRegistry.unregisterServer(it)
        }
    }

    override fun onUninstalled() {
        context.currentActivity?.let {
            val isInstalled = runBlocking { javaServer?.isInstalled(it) } ?: false
            if (isInstalled) {
                javaServer?.uninstall(it)
            }
        }
    }
}
