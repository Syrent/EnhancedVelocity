package org.sayandev.enhancedvelocity.feature.category

enum class FeatureCategories(override val directory: String?) : FeatureCategory {
    HOOK("hooks"),
    DEFAULT(null),
    CUSTOM("custom")
}