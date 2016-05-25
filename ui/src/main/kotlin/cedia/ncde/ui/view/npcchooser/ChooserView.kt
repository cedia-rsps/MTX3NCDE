package cedia.ncde.ui.view.npcchooser

import cedia.ncde.ui.fx.AbstractView
import cedia.ncde.model.NPC
import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage

/**
 * Created on 2016-05-23.
 */
class ChooserView : AbstractView() {
    override val presenter: ChooserPresenter by inject()
    override val root: AnchorPane by fxml()

    val npcListView: ListView<NPC> by fxid()
    val npcIdTextField: TextField by fxid()
    val searchTextField: TextField by fxid()
    val chooseButton: Button by fxid()
    val cancelButton : Button by fxid()

    init {

        chooseButton.setOnAction { presenter.choose() }
        cancelButton.setOnAction { presenter.cancel() }
        npcListView.setOnMousePressed { mouseEvent ->
            if (mouseEvent.clickCount == 2 && mouseEvent.isPrimaryButtonDown) {
                presenter.choose()
            }
        }
        Platform.runLater {
            getStage().setOnShown {
                searchTextField.requestLayout()
                searchTextField.requestFocus()

            }
        }
    }

    fun getStage() : Stage {
        return root.scene.window as Stage
    }
}