package cedia.ncde.ui.manager

import cedia.ncde.model.NPC
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.Injectable

/**
 * Created on 2016-05-23.
 */
class EditingNPCManager : Injectable {

    val editingNpcObject: ObjectProperty<NPC> = SimpleObjectProperty()

    fun editNewNpc(npc: NPC) {
        editingNpcObject.value = npc
    }

    fun reset() {
        editingNpcObject.value = null
    }

    fun getCurrentNpc(): NPC? {
        return editingNpcObject.get()
    }


}