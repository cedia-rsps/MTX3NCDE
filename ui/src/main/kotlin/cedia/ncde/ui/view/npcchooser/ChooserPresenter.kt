package cedia.ncde.ui.view.npcchooser

import cedia.ncde.ui.fx.Presenter
import cedia.ncde.io.store.NPCNamesStore
import cedia.ncde.model.NPC
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.Alert
import org.controlsfx.validation.Severity
import org.controlsfx.validation.ValidationResult
import org.controlsfx.validation.ValidationSupport
import org.controlsfx.validation.Validator
import tornadofx.SortedFilteredList
import tornadofx.alert

/**
 * Created on 2016-05-23.
 */
class ChooserPresenter : Presenter() {
    override val view: ChooserView by inject()

    val list: ObservableList<NPC> = FXCollections.observableArrayList()

    val validationSupport: ValidationSupport = ValidationSupport()

    val chosenNpc: ObjectProperty<NPC> = SimpleObjectProperty()

    private val validator: Validator<String> = Validator { control, value ->
        val condition: Boolean = if (value != null) !value.matches(
                Regex("^\\d+$"))
        else false
        ValidationResult.fromMessageIf(control, "Not a valid input. Input must be a integer.",
                Severity.ERROR, condition)
    }

    override fun initialize() {
        populateList()
        validationSupport.registerValidator(view.npcIdTextField, true, validator)
        listenForItemSelectionChange()
        val sortedFilteredList = SortedFilteredList(list).bindTo(view.npcListView)
        sortedFilteredList.filterWhen(observable = view.searchTextField.textProperty(),
                filterExpr = { string, npc -> filter(string, npc) });
        view.npcListView.selectionModel.selectFirst()
    }

    fun onOpen() {
        view.searchTextField.text = ""
        chosenNpc.value = null
    }

    fun cancel() {
        view.getStage().hide()
    }

    fun choose() {
        if (validationSupport.isInvalid) {
            alert(Alert.AlertType.ERROR, header = "Invalid Input", content = "Invalid input! You must input a integer.")
            return
        }
        chosenNpc.value = list.find { it.id == view.npcIdTextField.text.toInt() }
        view.getStage().hide()
    }

    fun getChosenNpc(): NPC? {
        return chosenNpc.get()
    }

    private fun populateList() {
        for ((id, name) in NPCNamesStore.getNames()) {
            list.add(NPC(id))
        }
    }

    private fun listenForItemSelectionChange() {
        view.npcListView.selectionModel.selectedItemProperty().addListener({ observableValue, old, new ->
            if (new != null) {
                view.npcIdTextField.text = new.id.toString()
            }
        })
    }

    private fun filter(string: String, npc: NPC): Boolean {
        if (string.isEmpty()) {
            return true
        }
        if (npc.name.contains(string, ignoreCase = true) || npc.id.toString().contains(string)) {
            return true
        }
        return false
    }

}