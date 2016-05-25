package cedia.ncde.ui.view.editor.properties

import cedia.ncde.ui.fx.Presenter
import cedia.ncde.model.NPC
import cedia.ncde.ui.view.editor.properties.PropertyView
import org.controlsfx.property.BeanPropertyUtils

/**
 * Created on 2016-05-23.
 */
class PropertyPresenter : Presenter() {
    override val view: PropertyView by inject()

    lateinit var npc: NPC

    override fun initialize() {
        //empty
    }

    fun loadProperties(npc: NPC) {
        this.npc = npc
        updatePropertiesTitle(npc.toString())
        //getting bean properties can take awhile so we run this in the background
        view.runAsync { BeanPropertyUtils.getProperties(npc.combatDefinition) } ui {
            view.propertySheet.items.setAll(it)
        }
    }

    private fun updatePropertiesTitle(title: String) {
        view.propertiesPane.text = "Properties for: $title"
    }
}