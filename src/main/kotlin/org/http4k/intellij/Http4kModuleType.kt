package org.http4k.intellij

import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.util.IconLoader

class Http4kModuleType : ModuleType<Http4kModuleBuilder>("HTTP4k_PROJECT_WIZARD") {

    private val pluginIcon by lazy { IconLoader.getIcon("/images/http4k.png", Http4kModuleType::class.java) }

    override fun createModuleBuilder() = Http4kModuleBuilder()

    @Suppress("DialogTitleCapitalization")
    override fun getName() = "http4k"

    override fun getDescription() = "Configure a new project using all the modules available in the http4k ecosystem"

    override fun getNodeIcon(isOpened: Boolean) = pluginIcon

    override fun getIcon() = pluginIcon
}
