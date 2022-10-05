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

import be.vbgn.gradle.cidetect.CiInformation
import org.gradle.api.Project

private val IdeaPrefix get() = System.getProperty("idea.platform.prefix")

/**
 * Returns `true` if the current ide is Intellij-IDEA.
 *
 * @author chachako
 */
val isIntelliJ: Boolean get() = IdeaPrefix?.startsWith("IDEA", ignoreCase = true) ?: false

/**
 * Returns `true` if the current ide is Android Studio.
 *
 * @author chachako
 */
val isAndroidStudio: Boolean get() = IdeaPrefix?.startsWith("AndroidStudio", ignoreCase = true) ?: false

/**
 * Returns true if it is currently running in a CI environment.
 *
 * @author chachako
 */
val isCiEnvironment: Boolean get() = ciEnvironment.isCi

/**
 * Returns the information of CI environment.
 *
 * @author chachako
 */
val ciEnvironment: CiEnvironment get() = CiEnvironment(CiInformation.detect())

/**
 * Returns true if it is currently running in a CI environment.
 *
 * @author chachako
 */
val Project.isCiEnvironment: Boolean get() = ciEnvironment.isCi

/**
 * Returns the information of CI environment.
 *
 * @author chachako
 */
val Project.ciEnvironment: CiEnvironment get() = CiEnvironment(CiInformation.detect(this))

/**
 * Delegate class of [CiInformation].
 *
 * @author chachako
 */
class CiEnvironment(private val actual: CiInformation) {

    /**
     * Returns `true` if the build is running in a CI environment.
     */
    val isCi: Boolean get() = actual.isCi

    /**
     * A unique identifier of the build.
     * Usually a sequential buildnumber, but can be anything depending on the CI environment.
     */
    val buildNumber: String? get() = actual.buildNumber

    /**
     * Returns the current branch for which a build is being executed.
     */
    val branch: String? get() = actual.branch

    /**
     * Returns the SCM reference that is currently being built.
     * Either a tag or a branch, depending on what is being built.
     */
    val reference: String? get() = if (isTag) tag else branch

    /**
     * Returns `true` if the current build is a pull request
     */
    val isPullRequest: Boolean get() = pullRequest != null

    /**
     * Returns a unique identifier of the pull request.
     * Usually a sequential number, but can be anything depending on the SCM platform.
     */
    val pullRequest: String? get() = actual.pullRequest

    /**
     * Returns the branch where the pull request will be merged into if it is merged.
     */
    val pullRequestTargetBranch: String? get() = actual.pullRequestTargetBranch

    /**
     * Returns `true` if the current build is a build of a tag
     */
    val isTag: Boolean get() = tag != null

    /**
     * Returns the current tag for which a build is being executed
     */
    val tag: String? get() = actual.tag

    /**
     * @return A string that identifies the CI platform that the build is being run on
     */
    val platform: String?
        get() {
            val className = javaClass.simpleName
            return if (className.endsWith("Information")) {
                className.substring(0, className.length - "Information".length)
            } else className
        }
}
