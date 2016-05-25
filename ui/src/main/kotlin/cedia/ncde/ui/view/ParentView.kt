package cedia.ncde.ui.view

import cedia.ncde.ui.fx.AbstractView
import javafx.scene.control.MenuItem
import javafx.scene.control.RadioMenuItem
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox

/**
 * Created on 2016-05-22.
 */
class ParentView : AbstractView() {
    override val presenter: ParentPresenter by inject()
    override val root: VBox by fxml()

    val openMenuItem: MenuItem by fxid()
    val saveAsMenuItem: MenuItem by fxid()
    val npcDefinitionsPane: StackPane by fxid()
    val editorPane: StackPane by fxid()
    val alwaysQuickDeleteRadio: RadioMenuItem by fxid()

    init {
        title = "MTX3 NPC Combat Definitions Editor"
        openMenuItem.setOnAction { presenter.openAction() }
        saveAsMenuItem.setOnAction { presenter.saveAction() }
    }

}
