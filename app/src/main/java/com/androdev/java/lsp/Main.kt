package com.androdev.java.lsp

import androidx.annotation.Keep
import com.rk.extension.ExtensionAPI
import com.rk.extension.ExtensionContext
import com.rk.file.child
import com.rk.file.createDirIfNot
import com.rk.file.localBinDir
import com.rk.icons.Icon
import com.rk.lsp.LspRegistry
import kotlinx.coroutines.runBlocking

@Keep
@Suppress("unused")
class Main(context: ExtensionContext) : ExtensionAPI(context) {
    private var javaServer: JavaServer? = null

    override fun onLoad() {
        // Copy LSP install script
        val javaAssetStream = context.assets.open("java-lsp.sh")
        val javaAsset = javaAssetStream.bufferedReader().use { it.readText() }
        val lspScriptDir = localBinDir().child("lsp").createDirIfNot()
        val javaInstallScript = lspScriptDir.child("java-lsp.sh").also {
            it.writeText(javaAsset)
        }

        javaServer =
            JavaServer(
                context = context,
                icon = Icon.ExternalResourceIcon(R.drawable.java, context.resources),
                supportedExtensions = listOf("java"),
                installScript = javaInstallScript
            ).also {
                LspRegistry.registerServer(it)
            }
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
