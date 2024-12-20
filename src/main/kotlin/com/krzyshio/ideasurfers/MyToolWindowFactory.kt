package com.krzyshio.ideasurfers

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.openapi.util.io.FileUtil
import javax.swing.JComponent
import javax.swing.JLabel
import java.io.File

class MyToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val panel = createVideoPanel()
        val content = contentFactory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun createVideoPanel(): JComponent {
        if (!JBCefApp.isSupported()) {
            return JLabel("JCEF is not supported in this environment. Please use a newer version of IntelliJ.")
        }


        val tempDir = FileUtil.createTempDirectory("idesurfers_video", null)
        copyResourceToFile("/videos/index.html", File(tempDir, "index.html"))

        val indexFile = File(tempDir, "index.html")
        val browser = JBCefBrowser(indexFile.toURI().toString())

        return browser.component
    }

    private fun copyResourceToFile(resourcePath: String, destination: File) {
        val inputStream = this::class.java.getResourceAsStream(resourcePath)
            ?: throw IllegalArgumentException("Resource $resourcePath not found!")

        inputStream.use { input ->
            FileUtil.createParentDirs(destination)
            destination.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}
