package org.sayandev.enhancedvelocity.feature

import java.util.UUID

object Features {
    val features = mutableListOf<Feature>()
    val userFeatures = mutableMapOf<UUID, List<Feature>>()

    @JvmStatic
    inline fun <reified T> getFeature(): T {
        return features.find { it is T } as T
    }

    @JvmStatic
    fun addFeature(feature: Feature) {
        features.add(feature)
    }

    @JvmStatic
    fun features(): List<Feature> {
        return features
    }

}