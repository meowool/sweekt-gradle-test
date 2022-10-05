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

@file:Suppress("SpellCheckingInspection")

package com.meowool.sweekt.gradle

/**
 * Represents the mirror repository of the maven repository.
 *
 * @author chachako
 */
class MavenMirrorRepository {
    /**
     * An accessor to access the "huaweicloud" mirror repository.
     *
     * See [Home](https://mirrors.huaweicloud.com/home) for more details.
     */
    val huaweicloud: String = "https://repo.huaweicloud.com/repository/maven/"

    /**
     * An accessor to access the "tencent" mirror repository.
     */
    val tencent: Tencent = Tencent()

    /**
     * See [Home](https://mirrors.cloud.tencent.com/) for more details.
     */
    class Tencent {
        private val baseUrl = "https://mirrors.cloud.tencent.com/"

        val maven: String = baseUrl + "maven/"
        val gradle: String = baseUrl + "gradle/"
    }

    /**
     * An accessor to access the "aliyun" mirror repository.
     */
    val aliyun: Aliyun = Aliyun()

    /**
     * See [Guide](https://developer.aliyun.com/mvn/guide) for more details.
     */
    class Aliyun {
        private val baseUrl = "https://maven.aliyun.com/repository/"

        val google: String = baseUrl + "google"
        val public: String = baseUrl + "public"
        val spring: String = baseUrl + "spring"
        val central: String = baseUrl + "central"
        val jcenter: String = baseUrl + "jcenter"
        val grailsCore: String = baseUrl + "grails-core"
        val springPlugin: String = baseUrl + "spring-plugin"
        val gradlePlugin: String = baseUrl + "gradle-plugin"
        val apacheSnapshots: String = baseUrl + "apache-snapshots"
    }
}
