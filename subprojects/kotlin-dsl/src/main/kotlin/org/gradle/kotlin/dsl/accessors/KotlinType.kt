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

package org.gradle.kotlin.dsl.accessors

import org.gradle.kotlin.dsl.support.bytecode.KmTypeBuilder

import kotlinx.metadata.Flags
import kotlinx.metadata.KmVariance
import org.gradle.kotlin.dsl.support.bytecode.genericTypeOf


internal
object KotlinType {

    private val array: KmTypeBuilder = { visitClass("kotlin/Array") }

    private val list: KmTypeBuilder = { visitClass("kotlin/collections/List") }

    val string: KmTypeBuilder = { visitClass("kotlin/String") }

    val unit: KmTypeBuilder = { visitClass("kotlin/Unit") }

    val any: KmTypeBuilder = { visitClass("kotlin/Any") }

    val typeParameter: KmTypeBuilder = { visitTypeParameter(0) }

    inline fun <reified T> array(
        argumentFlags: Flags = 0,
        argumentVariance: KmVariance = KmVariance.INVARIANT
    ) = genericTypeOf(array, classOf<T>(), argumentFlags, argumentVariance)

    inline fun <reified T> list(
        argumentFlags: Flags = 0,
        argumentVariance: KmVariance = KmVariance.INVARIANT
    ) = genericTypeOf(list, classOf<T>(), argumentFlags, argumentVariance)

    fun vararg(type: KmTypeBuilder, typeFlags: Flags = 0) = genericTypeOf(array, type, typeFlags, KmVariance.OUT)
}
