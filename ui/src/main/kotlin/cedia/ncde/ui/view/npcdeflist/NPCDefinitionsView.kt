package cedia.ncde.ui.view.npcdeflist

import cedia.ncde.ui.fx.AbstractView
import cedia.ncde.model.NPC
import javafx.beans.binding.Bindings
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import tornadofx.onUserSelect

/**
 * Created on 2016-05-22.
 */
class NPCDefinitionsView : AbstractView() {
    override val presenter: NPCDefinitionsPresenter by inject()
    override val root: AnchorPane by fxml()

    val searchTextField : TextField by fxid()
    val addButton: Button by fxid()
    val npcListView: ListView<NPC> by fxid()
    val deleteMenuButton: SplitMenuButton by fxid()

    /* Delete Menu Button Items */
    val deleteMenuItem: MenuItem by fxid()
    val deleteAllMenuItem: MenuItem by fxid()
    /* NPC List View Menu Items*/
    val copyMenuItem: MenuItem by fxid()
    val quickDeleteMenuItem: MenuItem by fxid()

    init {
        quickDeleteMenuItem.disableProperty().bind(Bindings.isEmpty(npcListView.selectionModel.selectedItems))
        copyMenuItem.disableProperty().bind(Bindings.isEmpty(npcListView.selectionModel.selectedItems))
        deleteMenuButton.disableProperty().bind(Bindings.isEmpty(npcListView.selectionModel.selectedItems))
        deleteMenuButton.setOnAction { presenter.delete(confirmation = true) }
        deleteMenuItem.setOnAction { presenter.delete() }
        quickDeleteMenuItem.setOnAction { presenter.delete() }
        deleteAllMenuItem.setOnAction { presenter.delete(all = true) }
        addButton.setOnAction { presenter.add() }
        copyMenuItem.setOnAction { presenter.copy() }
        npcListView.onUserSelect { presenter.onNpcSelect(it) }

    }
}