package com.mikepenz.aboutlibraries.plugin

import com.mikepenz.aboutlibraries.plugin.model.CollectedContainer
import com.mikepenz.aboutlibraries.plugin.util.DependencyCollector
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

@CacheableTask
abstract class AboutLibrariesCollectorTask : DefaultTask() {

    /** holds the collected set of dependencies*/
    @Internal
    protected lateinit var collectedDependencies: CollectedContainer

    val dependencyCache: File
        @OutputFile
        get() {
            val folder = File(project.buildDir, "generated/aboutLibraries/").also {
                it.mkdirs()
            }
            return File(folder, "dependency_cache.json")
        }

    /**
     * Collect the dependencies via the available configurations for the current project
     */
    fun configure() {
        collectedDependencies = DependencyCollector().collect(project)
    }

    @TaskAction
    fun action() {
        if (!::collectedDependencies.isInitialized) {
            configure()
        }
        dependencyCache.writeText(groovy.json.JsonOutput.toJson(collectedDependencies))
    }
}