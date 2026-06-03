package com.androdev.java.lsp

import android.app.Activity
import android.os.Bundle
import androidx.annotation.Keep
import com.rk.extension.ExtensionAPI
import com.rk.extension.ExtensionContext
import com.rk.file.child
import com.rk.file.createDirIfNot
import com.rk.file.localBinDir
import com.rk.icons.Icon
import com.rk.lsp.LspRegistry

@Keep
@Suppress("unused")
class Main(context: ExtensionContext) : ExtensionAPI(context) {
    private var javaServer: JavaServer? = null

    override fun onExtensionLoaded() {
        // Copy LSP install script
        val javaAssetStream = context.assets.open("java-lsp.sh")
        val javaAsset = javaAssetStream.bufferedReader().use { it.readText() }
        val lspScriptDir = localBinDir().child("lsp").createDirIfNot()
        val javaInstallScript = lspScriptDir.child("java-lsp.sh").also {
            it.writeText(javaAsset)
        }

        javaServer = JavaServer(Icon.ExternalResourceIcon(R.drawable.java, context.resources), javaInstallScript).also {
            LspRegistry.registerServer(it)
        }
    }

    private fun dispose() {
        javaServer?.let {
            LspRegistry.unregisterServer(it)
        }
    }

    override fun onUninstalled() {
        javaServer?.uninstall(context.appContext)
        dispose()
    }

    override fun onUpdated() {
        dispose()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}
}
