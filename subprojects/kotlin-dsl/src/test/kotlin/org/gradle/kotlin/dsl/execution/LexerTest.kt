/*
 * Copyright 2018 the original author or authors.
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

package org.gradle.kotlin.dsl.execution

import org.gradle.kotlin.dsl.execution.TopLevelBlockId.buildscript
import org.gradle.kotlin.dsl.execution.TopLevelBlockId.plugins
import org.gradle.kotlin.dsl.execution.TopLevelBlockId.kotlinJvm
import org.gradle.kotlin.dsl.execution.TopLevelBlockId.applyKotlinDsl

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test


class LexerTest {

    @Test
    fun `extracts comments and top-level blocks`() {

        assertThat(
            lex(
                "/* ... */" +
                    "\n// ..." +
                    "\nbuildscript { /*" +
                    "\n ... */" +
                    "\n}" +
                    "\nplugins { /*" +
                    "\n ... */" +
                    "\n}" +
                    "\nkotlinJvm { /*" +
                    "\n ... */" +
                    "\n}" +
                    "\napplyKotlinDsl(/* ... */)" +
                    "\n// ...",
                buildscript, plugins, kotlinJvm, applyKotlinDsl
            ),
            equalTo(
                Packaged(
                    null,
                    LexedScript(
                        listOf(
                            0..8,
                            10..15,
                            31..40,
                            54..63,
                            79..88,
                            107..115,
                            118..123,
                        ),
                        listOf(
                            topLevelBlock(buildscript, 17..27, 29..42),
                            topLevelBlock(plugins, 44..50, 52..65),
                            topLevelBlock(kotlinJvm, 67..75, 77..90),
                            topLevelBlock(applyKotlinDsl, 92..105, 106..116),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `extracts package name`() {
        assertThat(
            lex("\n/* ... */\npackage com.example\nplugins { }", plugins),
            equalTo(
                Packaged(
                    "com.example",
                    LexedScript(
                        listOf(1..9),
                        listOf(topLevelBlock(plugins, 31..37, 39..41))
                    )
                )
            )
        )
    }
}
