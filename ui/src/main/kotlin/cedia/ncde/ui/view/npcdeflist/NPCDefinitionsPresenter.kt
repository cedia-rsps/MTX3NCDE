package cedia.ncde.ui.view.npcdeflist

import cedia.ncde.ui.fx.Presenter
import cedia.ncde.ui.repository.CombatDefinitionsRepository
import cedia.ncde.ui.manager.EditingNPCManager
import cedia.ncde.model.NPC
import cedia.ncde.ui.view.ParentPresenter
import cedia.ncde.ui.view.npcchooser.ChooserView
import javafx.collections.ListChangeListener
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.Modality
import javafx.stage.Stage
import tornadofx.SortedFilteredList
import tornadofx.alert
import tornadofx.selectedItem

/**
 * Created on 2016-05-22.
 */
class NPCDefinitionsPresenter : Presenter() {
    override val view: NPCDefinitionsView by inject()
    val chooserView: ChooserView by inject()
    val masterRepository: CombatDefinitionsRepository by inject()
    val editingNpcManager: EditingNPCManager by inject()

    val chooserStage: Stage by lazy {
        val stage = Stage()
        stage.title = "Choose Your NPC"
        stage.scene = Scene(chooserView.root)
        stage.initOwner(primaryStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage
    }

    override fun initialize() {
        val sortedFilteredList = SortedFilteredList(masterRepository.definitions).bindTo(view.npcListView)
        sortedFilteredList.filterWhen(observable = view.searchTextField.textProperty(),
                filterExpr = { string, npc -> filter(string, npc) });

        masterRepository.definitions.addListener(ListChangeListener { change ->
            while (change.next()) {
                if (change.wasRemoved()) {
                    val removedList = change.removed
                    val removedNPC = removedList.first()
                    if (editingNpcManager.getCurrentNpc() != null) {
                        if (removedNPC == editingNpcManager.getCurrentNpc()
                                || masterRepository.definitions.isEmpty()) {
                            editingNpcManager.reset()
                        }
                    }
                }
            }
        })
    }

    fun add() {
        val chosenNpc: NPC? = openChooser()
        if (chosenNpc != null) {
            val chosenNpcCopy = chosenNpc.copy()
            val success = masterRepository.add(chosenNpcCopy)
            if (success) {
                resetSearch()
                view.npcListView.scrollTo(chosenNpcCopy)
                view.npcListView.selectionModel.select(chosenNpcCopy)
            }
        }
    }

    /**
     * Copy the selected npc's definition with another npc chosen from the Chooser window.
     */
    fun copy() {
        val selectedNPC: NPC = view.npcListView.selectedItem!!
        val chosenNpc: NPC? = openChooser()
        if (chosenNpc != null) {
            val npcToCopy = chosenNpc.copy(combatDefinition = selectedNPC.combatDefinition)
            val success = masterRepository.add(npcToCopy)
            if (success) {
                resetSearch()
                view.npcListView.scrollTo(npcToCopy)
                view.npcListView.selectionModel.select(npcToCopy)
            }
        }
    }

    val parentPresenter: ParentPresenter by inject()

    fun delete(confirmation: Boolean = false, all: Boolean = false) {
        var canDelete = true
        if (confirmation && !parentPresenter.alwaysQuickDelete()) {
            alert(Alert.AlertType.CONFIRMATION, header = "Are you sure?",
                    content = "Are you sure you want to delete this combat definition?",
                    actionFn = {
                        if (it != ButtonType.OK) {
                            canDelete = false
                        }
                    })
        }
        if (!canDelete)
            return

        if (all) {
            masterRepository.clear()
            resetSearch()
        } else {
            val selectedNpc = view.npcListView.selectedItem
            //no need to check for nulls since disable button is disabled if there is no selection.
            masterRepository.remove(selectedNpc!!)
        }

    }

    fun onNpcSelect(definition: NPC) {
        editingNpcManager.editNewNpc(definition)
    }

    fun resetSearch() {
        view.searchTextField.text = ""
    }

    private fun openChooser(): NPC? {
        chooserView.presenter.onOpen()
        chooserStage.showAndWait()
        return chooserView.presenter.getChosenNpc()
    }

    private fun filter(string: String, npc: NPC): Boolean {
        if (string.isEmpty()) {
            return true
        }
        if (npc.name.contains(string, ignoreCase = true) || npc.id.toString().equals(string)) {
            return true
        }
        return false
    }


}