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

import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider

/**
 * Sets the dependencies of this task provider.
 *
 * See [Task.dependsOn] for more details.
 *
 * @author chachako
 */
fun <T: Task> TaskProvider<out T>.dependsOn(paths: Collection<TaskProvider<out Task>>): TaskProvider<out T> {
    if (paths.isNotEmpty()) configure { dependsOn(paths) }
    return this
}

/**
 * Sets the dependencies of this task provider.
 *
 * See [Task.dependsOn] for more details.
 *
 * @author chachako
 */
fun <T: Task> TaskProvider<out T>.dependsOn(vararg paths: Any): TaskProvider<out T> {
    if (paths.isNotEmpty()) configure { dependsOn(*paths) }
    return this
}

/**
 * Sets the dependencies of these task provider.
 *
 * See [Task.dependsOn] for more details.
 *
 * @author chachako
 */
@JvmName("allTaskProvidersDependsOn")
fun <T: Task> Array<TaskProvider<out T>>.dependsOn(vararg paths: Any): Array<TaskProvider<out T>> {
    if (this.isNotEmpty() && paths.isNotEmpty()) forEach { it.configure { dependsOn(*paths) } }
    return this
}

/**
 * Sets the dependencies of these task provider.
 *
 * See [Task.dependsOn] for more details.
 *
 * @author chachako
 */
@JvmName("allTaskProvidersDependsOn")
fun <T: Task> Collection<TaskProvider<out T>>.dependsOn(vararg paths: Any): Collection<TaskProvider<out T>> {
    if (this.isNotEmpty() && paths.isNotEmpty()) forEach { it.configure { dependsOn(*paths) } }
    return this
}

/**
 * Sets the dependencies of these task provider.
 *
 * See [Task.dependsOn] for more details.
 *
 * @author chachako
 */
@JvmName("allTasksDependsOn")
fun <T: Task> Array<out T>.dependsOn(vararg paths: Any): Array<out T> {
    if (this.isNotEmpty() && paths.isNotEmpty()) forEach { it.dependsOn(*paths) }
    return this
}

/**
 * Sets the dependencies of these task provider.
 *
 * See [Task.dependsOn] for more details.
 *
 * @author chachako
 */
@JvmName("allTasksDependsOn")
fun <T: Task> Collection<T>.dependsOn(vararg paths: Any): Collection<T> {
    if (this.isNotEmpty() && paths.isNotEmpty()) forEach { it.dependsOn(*paths) }
    return this
}
