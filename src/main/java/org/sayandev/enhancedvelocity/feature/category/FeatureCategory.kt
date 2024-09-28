package org.sayandev.enhancedvelocity.feature.category

import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
interface FeatureCategory {
    val directory: String?
}