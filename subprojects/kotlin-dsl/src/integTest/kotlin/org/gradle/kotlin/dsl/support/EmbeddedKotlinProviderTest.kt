package org.gradle.kotlin.dsl.support

import org.gradle.integtests.fixtures.ToBeFixedForConfigurationCache
import org.gradle.kotlin.dsl.embeddedKotlinVersion
import org.gradle.kotlin.dsl.fixtures.AbstractKotlinIntegrationTest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class EmbeddedKotlinProviderTest : AbstractKotlinIntegrationTest() {

    @Test
    @ToBeFixedForConfigurationCache(because = ":buildEnvironment")
    fun `no extra dependencies are added to the buildscript classpath`() {

        val result = build("buildEnvironment")

        assertThat(result.output, containsString("No dependencies"))
    }

    @Test
    @ToBeFixedForConfigurationCache(because = ":buildEnvironment")
    fun `embedded kotlin dependencies are pinned to the embedded version`() {

        withBuildScript(
            """
            buildscript {
                $repositoriesBlock
                dependencies {
                    classpath("org.jetbrains.kotlin:kotlin-stdlib:1.0")
                    classpath("org.jetbrains.kotlin:kotlin-reflect:1.0")
                }
            }
            """
        )

        val result = build("buildEnvironment")

        listOf("stdlib", "reflect").forEach { module ->
            assertThat(result.output, containsString("org.jetbrains.kotlin:kotlin-$module:1.0 -> $embeddedKotlinVersion"))
        }
    }

    @Test
    @ToBeFixedForConfigurationCache(because = ":buildEnvironment")
    fun `stdlib and reflect are pinned to the embedded kotlin version for requested plugins`() {
        withBuildScript(
            """
            plugins {
                kotlin("jvm") version "1.4.20"
            }
            """
        )

        // Remove this when we are using a Kotlin version later than 1.6.10, so we can use 1.6.10 above.
        executer.expectDocumentedDeprecationWarning(
            "IncrementalTaskInputs has been deprecated. " +
                "This is scheduled to be removed in Gradle 8.0. " +
                "On method 'AbstractKotlinCompile.execute' use 'org.gradle.work.InputChanges' instead. " +
                "Consult the upgrading guide for further information: https://docs.gradle.org/current/userguide/upgrading_version_7.html#incremental_task_inputs_deprecation"
        )
        executer.withFullDeprecationStackTraceEnabled()
        val result = build("buildEnvironment")

        listOf("stdlib", "reflect").forEach { module ->
            assertThat(result.output, containsString("org.jetbrains.kotlin:kotlin-$module:1.4.20 -> $embeddedKotlinVersion"))
        }
    }

    @Test
    @ToBeFixedForConfigurationCache(because = ":buildEnvironment")
    fun `compiler-embeddable is not pinned`() {
        withBuildScript(
            """
            buildscript {
                $repositoriesBlock
                dependencies {
                    classpath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.31")
                }
            }
            """
        )

        val result = build("buildEnvironment")

        assertThat(result.output, containsString("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.31"))
        assertThat(result.output, not(containsString("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.31 ->")))
    }

    @Test
    fun `fails with a reasonable message on conflict with embedded kotlin`() {
        withBuildScript(
            """
            buildscript {
                $repositoriesBlock
                dependencies {
                    classpath("org.jetbrains.kotlin:kotlin-stdlib") {
                        version { strictly("1.3.31") }
                    }
                }
            }
            """
        )

        val result = buildAndFail("buildEnvironment")

        assertThat(
            result.error,
            containsString("Cannot find a version of 'org.jetbrains.kotlin:kotlin-stdlib' that satisfies the version constraints")
        )
        assertThat(
            result.error,
            containsString("because of the following reason: Pinned to the embedded Kotlin")
        )
    }
}
