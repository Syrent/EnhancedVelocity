package org.sayandev.enhancedvelocity.feature

import org.sayandev.enhancedvelocity.feature.category.FeatureCategories
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable
import org.sayandev.stickynote.lib.spongepowered.configurate.serialize.TypeSerializerCollection
import org.sayandev.stickynote.velocity.registerListener

@ConfigSerializable
abstract class ListenedFeature(
    id: String,
    enabled: Boolean = true,
    category: FeatureCategories = FeatureCategories.DEFAULT,
    additionalSerializers: TypeSerializerCollection = TypeSerializerCollection.defaults()
) : Feature(id, enabled, category, additionalSerializers) {

    override fun enable() {
        if (!condition) return
        registerListener(this)
        super.enable()
    }

    override fun disable() {
        super.disable()
    }

}