/*
 * Copyright 2022 the original author or authors.
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

package com.meowool.sweekt.gradle.util

import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import java.io.File
import java.util.Properties

/**
 * If this file exists, read it as [Properties] and return it, otherwise return `null`.
 *
 * @author chachako
 */
fun File.readProperties(): Properties? = when {
    exists() -> Properties().also {
        it.load(bufferedReader())
    }
    else -> null
}


/**
 * Finds the properties of the `local.properties` file in the project.
 *
 * @author chachako
 */
fun Project.findLocalProperties(): Properties? = projectDir.resolve("local.properties").readProperties()


/**
 * Finds the properties of the `local.properties` file in the root directory
 * of this settings.
 *
 * @author chachako
 */
fun Settings.findLocalProperties(): Properties? = rootDir.resolve("local.properties").readProperties()

/**
 * Return the properties of the `local.properties` file in the project.
 *
 * @author chachako
 */
val Project.localProperties: Properties
    get() = findLocalProperties()
        ?: error("There is no `local.properties` file in the project(${projectDir.absolutePath})")

/**
 * Return the properties of the `local.properties` file in the root directory
 * of this settings.
 *
 * @author chachako
 */
val Settings.localProperties: Properties
    get() = findLocalProperties()
        ?: error("There is no `local.properties` file in the directory: ${rootDir.absolutePath}")

/**
 * Locate the property from a touchable location (possibly system environment variable) of the
 * current project and return its value, or `null` if it cannot be found.
 *
 * ## Explain
 *
 * This function searches for and returns the property of the current project as well as its ancestor
 * projects by the following behavior:
 *
 * 1. Search by [Project.findProperty] for the property defined from [Project.extra] or
 *    `gradle.properties` file, etc.
 *
 * 2. Search for the property defined by `local.properties` file.
 *
 * 3. Search in system environment variables.
 *
 * @author chachako
 */
fun Project.findPropertyOrEnv(key: String): Any? {
    findProperty(key)?.let { return it }

    selfAndAncestors.forEach { project ->
        project.findLocalProperties()?.getProperty(key)?.let { return it }
    }

    return System.getenv(key)
}

/**
 * Returns the property value from the touchable position (maybe system env) of the current project,
 * and return [defaultValue] if it can't find it.
 *
 * @author chachako
 */
fun Project.getPropertyOrEnv(key: String, defaultValue: Any): Any =
    findPropertyOrEnv(key) ?: defaultValue
