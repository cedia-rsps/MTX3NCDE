package cedia.ncde.ui.view.editor

import cedia.ncde.ui.fx.AbstractView
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane

/**
 * Created on 2016-05-23.
 */
class EditorView : AbstractView() {
    override val root: AnchorPane by fxml()
    override val presenter: EditorPresenter by inject()
    val propertyTablePane: StackPane by fxid()
    val saveButton: Button by fxid()
    val editorLabel: Label by fxid()

    init {
        saveButton.isDisable = false
        saveButton.visibleProperty().bind(presenter.editingNpcManager.editingNpcObject.isNotNull)
        presenter.propertyView.root.visibleProperty().bind(presenter.editingNpcManager.editingNpcObject.isNotNull)
        saveButton.setOnAction { presenter.save() }
    }

}