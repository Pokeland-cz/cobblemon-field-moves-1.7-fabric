package us.timinc.mc.cobblemon.fieldmoves.listeners

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent
import com.cobblemon.mod.common.pokemon.Gender
import us.timinc.mc.cobblemon.fieldmoves.CobblemonFieldMoves.config
import us.timinc.mc.cobblemon.fieldmoves.CobblemonFieldMoves.debug
import kotlin.random.Random.Default.nextDouble

object FieldMoveCaptureListener {

    private val GENDER_BENDER = mapOf(
        Gender.MALE to Gender.FEMALE,
        Gender.FEMALE to Gender.MALE
    )

    fun register() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL) { event ->
            handleCapture(event)
        }
    }

    private fun handleCapture(event: PokemonCapturedEvent) {
        val player = event.player
        val capturedPokemon = event.pokemon

        // 1. Fetch the leader (first slot) safely
        val playerPartyStore = Cobblemon.storage.getParty(player)
        val leader = playerPartyStore.firstOrNull() ?: return
        val leaderAbility = leader.ability?.name ?: return

        // 2. Branch logic based on Ability
        when (leaderAbility) {
            "synchronize" -> {
                if (nextDouble() < config.synchronizeChance) {
                    debug("Synchronizing nature to ${leader.nature.name.path}")
                    capturedPokemon.nature = leader.nature
                }
            }

            "cutecharm" -> {
                val leaderGender = leader.gender
                val targetGender = GENDER_BENDER[leaderGender] ?: return

                // Check if the species can actually be the target gender
                val species = capturedPokemon.species
                val canBeTarget = when (targetGender) {
                    Gender.MALE -> species.maleRatio > 0f
                    Gender.FEMALE -> species.maleRatio < 1f
                    else -> false
                }

                if (canBeTarget && nextDouble() < config.cuteCharmChance) {
                    debug("Cute Charm triggered! Changing gender to $targetGender")
                    capturedPokemon.gender = targetGender
                }
            }
        }
    }
}