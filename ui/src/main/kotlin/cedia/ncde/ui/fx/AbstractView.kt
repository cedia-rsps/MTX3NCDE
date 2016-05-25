package cedia.ncde.ui.fx

import javafx.application.Platform
import tornadofx.View

/**
 * Created on 2016-05-22.
 */
abstract class AbstractView : View() {
    abstract val presenter: Presenter

    init {
        Platform.runLater { presenter.initialize() }
    }


}