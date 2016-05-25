package cedia.ncde.model

/**
 * Created on 2016-05-22.
 */
data class CombatDefinition(var hitPoints: Int, var attackAnimation: Int,
                            var defenceAnimation: Int, var deathAnimation: Int,
                            var respawnDelay: Int, var attackGraphics: Int,
                            var attackProjectile: Int, var xp: Double,
                            var followClose: Boolean, var poisonImmune: Boolean,
                            var poisonous: Boolean, var aggressive: Boolean,
                            var aggressiveRatio: Int)