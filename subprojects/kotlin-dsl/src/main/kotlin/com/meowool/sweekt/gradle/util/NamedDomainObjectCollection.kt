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

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.UnknownDomainObjectException
import org.gradle.kotlin.dsl.*
import kotlin.reflect.KClass

/**
 * Safe alternative to get the provided value of [NamedDomainObjectCollection.named].
 *
 * @author chachako
 */
fun <T> NamedDomainObjectCollection<T>.getNamedOrNull(name: String): T? = try {
    named(name).orNull
} catch (e: UnknownDomainObjectException) {
    null
}

/**
 * Safe alternative to get the provided value of [NamedDomainObjectCollection.named].
 *
 * @author chachako
 */
fun <T> NamedDomainObjectCollection<T>.getNamed(name: String): T = named(name).get()

/**
 * Safe alternative to get the provided value of [NamedDomainObjectCollection.named].
 *
 * @author chachako
 */
fun <T> NamedDomainObjectCollection<T>.getNamed(
    name: String,
    configurationAction: T.() -> Unit,
): T = named(name, configurationAction).get()

/**
 * Safe alternative to [NamedDomainObjectCollection.named].
 *
 * @author chachako
 */
fun <T> NamedDomainObjectCollection<T>.namedOrNull(name: String): NamedDomainObjectProvider<T>? = try {
    named(name)
} catch (e: UnknownDomainObjectException) {
    null
}

/**
 * Safe alternative to [NamedDomainObjectCollection.named] and [NamedDomainObjectProvider.configure].
 *
 * @author chachako
 */
fun <T> NamedDomainObjectCollection<T>.configureIfPresent(
    name: String,
    configurationAction: T.() -> Unit,
) = try {
    named(name).configure(configurationAction)
} catch (e: UnknownDomainObjectException) {
    null
}

/**
 * Safe alternative to [NamedDomainObjectCollection.named] and [NamedDomainObjectProvider.configure].
 *
 * @author chachako
 */
fun <S : T, T : Any> NamedDomainObjectCollection<*>.configureIfPresent(
    name: String,
    type: KClass<S>,
    configurationAction: S.() -> Unit,
) = try {
    named(name, type).configure(configurationAction)
} catch (e: UnknownDomainObjectException) {
    null
}
