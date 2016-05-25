package cedia.ncde.ui.repository

import cedia.ncde.model.NPC
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.Alert
import tornadofx.Injectable
import tornadofx.alert

/**
 * Created on 2016-05-22.
 */
class CombatDefinitionsRepository : Injectable {

    val definitions: ObservableList<NPC> = FXCollections.observableArrayList()


    fun populate(collection: Collection<NPC>) {
        definitions.clear()
        definitions.addAll(collection)
    }

    fun add(npc: NPC): Boolean {
        if (hasDuplicate(npc)) {
            alert(type = Alert.AlertType.ERROR, header = "Duplicate Found for $npc", content =
            "You cannot have duplicate npcs.")
            return false
        }
        return definitions.add(npc)
    }

    fun remove(npc: NPC) {
        definitions.remove(npc)
    }

    fun clear() {
        definitions.clear()
    }

    private fun hasDuplicate(npc: NPC): Boolean {
        return (definitions.any { it.id == npc.id })
    }


}