package cedia.ncde.model

import cedia.ncde.io.store.NPCNamesStore

/**
 * Created on 2016-05-22.
 */
data class NPC(val id: Int, val name: String = NPCNamesStore.getNameForId(id),
               var combatDefinition: CombatDefinition =
               CombatDefinition(0, -1, -1, -1, 0, -1, -1, 0.0, false, false, false, false, 1)) {

    override fun toString(): String {
        return "[$id] $name"
    }
}