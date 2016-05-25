package cedia.ncde.ui.view.editor.properties

import cedia.ncde.ui.fx.AbstractView
import javafx.scene.control.TitledPane
import javafx.scene.layout.AnchorPane
import org.controlsfx.control.PropertySheet

/**
 * Created on 2016-05-23.
 */
class PropertyView : AbstractView() {
    override val presenter: PropertyPresenter by inject()
    override val root: AnchorPane by fxml()

    val propertiesPane: TitledPane by fxid()
    val propertySheet: PropertySheet = PropertySheet()


    init {
        propertySheet.isModeSwitcherVisible = false
        with(propertiesPane) {
            this.content = propertySheet
        }

    }

}
