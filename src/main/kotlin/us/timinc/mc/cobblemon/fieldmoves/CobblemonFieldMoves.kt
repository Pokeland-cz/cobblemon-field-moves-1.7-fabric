package us.timinc.mc.cobblemon.fieldmoves

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import us.timinc.mc.cobblemon.fieldmoves.config.Config
import us.timinc.mc.cobblemon.fieldmoves.config.ConfigBuilder
import us.timinc.mc.cobblemon.fieldmoves.listeners.FieldMoveCaptureListener

object CobblemonFieldMoves : ModInitializer {
    const val MOD_ID = "field_moves"
    var config: Config = ConfigBuilder.load(Config::class.java, MOD_ID)

    override fun onInitialize() {
        FieldMoveCaptureListener.register()
    }

    fun modIdentifier(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
    }

    fun debug(msg: String) {
        if (!config.debug) return
        println(msg)
    }
}