/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.kotlin.dsl

import org.gradle.api.Incubating
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderConvertible
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency
import org.gradle.plugin.use.PluginDependencySpec


/**
 * Receiver for the `plugins` block.
 *
 * @see [PluginDependenciesSpec]
 *
 * @author chachako
 */
class PluginDependenciesSpecScope internal constructor(
    private val plugins: PluginDependenciesSpec
) : PluginDependenciesSpec {

    override fun id(id: String): PluginDependencySpec =
        plugins.id(id)

    override fun alias(notation: Provider<PluginDependency>) =
        plugins.alias(notation)

    override fun alias(notation: ProviderConvertible<PluginDependency>) =
        plugins.alias(notation)

    /**
     * A shortcut to declare [id].
     *
     * ```
     * plugins {
     *   +"org.company.myplugin" version "1.3"
     * }
     * ```
     */
    operator fun String.unaryPlus(): PluginDependencySpec = plugins.id(this)

    /**
     * A shortcut to declare [alias].
     *
     * ```
     * plugins {
     *   +libs.plugins.gradleKotlinDsl
     * }
     * ```
     */
    operator fun Provider<PluginDependency>.unaryPlus(): PluginDependencySpec = plugins.alias(this)

    /**
     * A shortcut to declare [alias].
     *
     * ```
     * plugins {
     *   +libs.plugins.gradleKotlinDsl
     * }
     * ```
     */
    operator fun ProviderConvertible<PluginDependency>.unaryPlus(): PluginDependencySpec = plugins.alias(this)
}


/**
 * Specify the version of the plugin to depend on.
 *
 * Infix version of [PluginDependencySpec.version].
 */
infix fun PluginDependencySpec.version(version: String?): PluginDependencySpec = version(version)


/**
 * Specify the version of the plugin to depend on.
 *
 * Infix version of [PluginDependencySpec.version].
 *
 * @since 7.2
 */
@Incubating
infix fun PluginDependencySpec.version(version: Provider<String>): PluginDependencySpec = version(version)


/**
 * Specifies whether the plugin should be applied to the current project. Otherwise it is only put
 * on the project's classpath.
 *
 * Infix version of [PluginDependencySpec.apply].
 */
infix fun PluginDependencySpec.apply(apply: Boolean): PluginDependencySpec = apply(apply)
