package cedia.ncde.ui.view.editor

import cedia.ncde.ui.fx.Presenter
import cedia.ncde.ui.manager.EditingNPCManager
import cedia.ncde.model.NPC
import cedia.ncde.ui.view.editor.properties.PropertyView

/**
 * Created on 2016-05-23.
 */
class EditorPresenter : Presenter() {
    override val view: EditorView by inject()
    val propertyView: PropertyView by inject()
    val editingNpcManager: EditingNPCManager by inject()

    override fun initialize() {
        editingNpcManager.editingNpcObject.addListener({ observableValue, old, new ->
            //every time the current editing npc changes, we will update the property table. )
            if (new != null) {
                createPropertyTable()
                /* Create a copy of the selected npc to modify */
                val copiedNpc = new.copy(combatDefinition = new.combatDefinition.copy())
                propertyView.presenter.loadProperties(copiedNpc)
            }
            updateEditorLabel(new)
        })
    }

    fun save() {
        val currentNpc = editingNpcManager.getCurrentNpc() //unmodified npc
        val editedNpc = propertyView.presenter.npc
        currentNpc?.combatDefinition = editedNpc.combatDefinition
    }

    private fun createPropertyTable() {
        if (!view.propertyTablePane.children.contains(propertyView.root)) {
            view.propertyTablePane.children.add(propertyView.root)
        }
    }

    private fun updateEditorLabel(npc: NPC?) {
        view.editorLabel.text = if (npc == null) "Editor" else "Editor(${npc.id} - ${npc.name})"
    }
}