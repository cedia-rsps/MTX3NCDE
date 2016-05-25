package cedia.ncde.ui.view

import cedia.ncde.ui.fx.Presenter
import cedia.ncde.io.CombatDefinitionsIO
import cedia.ncde.ui.repository.CombatDefinitionsRepository
import cedia.ncde.model.DefinitionFileType
import cedia.ncde.model.NPC
import cedia.ncde.ui.view.editor.EditorView
import cedia.ncde.ui.view.npcdeflist.NPCDefinitionsView
import javafx.scene.control.Alert
import javafx.scene.control.ChoiceDialog
import javafx.stage.FileChooser
import tornadofx.FileChooserMode
import tornadofx.alert
import tornadofx.chooseFile
import java.io.File
import java.util.*

/**
 * Created on 2016-05-22.
 */
class ParentPresenter : Presenter() {

    override val view: ParentView by inject()
    val npcDefinitionsView: NPCDefinitionsView by inject()
    val editorView: EditorView by inject()
    val masterRepository: CombatDefinitionsRepository by inject()

    var lastDirectory: File? = null

    override fun initialize() {
        setupNPCDefinitionsPane()
        setupEditorPane()
    }

    fun openAction() {
        val definitionsType = askForCombatDefinitionsType()
        definitionsType.ifPresent { type ->
            val filters = when (type) {
                DefinitionFileType.UNPACKED -> FileChooser.ExtensionFilter("Text Files", "*.txt")
                else ->
                    FileChooser.ExtensionFilter("Packed Files", "*.ncd")
            }
            val selectedFileList = listOfNotNull(chooseFile(title = "Choose your Definitions File",
                    filters = arrayOf(filters),
                    owner = view.primaryStage, mode = FileChooserMode.Single).firstOrNull())
            if (selectedFileList.isNotEmpty()) {
                var loadedDefinitions = Collections.emptyList<NPC>()
                val selectedFile = selectedFileList.first()
                lastDirectory = selectedFile.parentFile
                runAsync { // load combat definitions asynchronously
                    loadedDefinitions = CombatDefinitionsIO.load(type, selectedFile)
                } ui { // populate the definitions in the ui thread
                    if (loadedDefinitions.isNotEmpty()) {
                        masterRepository.populate(loadedDefinitions)
                        npcDefinitionsView.presenter.resetSearch()
                    }
                }
            }
        }
    }

    fun saveAction() {
        val definitionsType = askForCombatDefinitionsType()
        definitionsType.ifPresent { type ->
            val saveChooser = FileChooser()
            when (type) {
                DefinitionFileType.PACKED -> {
                    saveChooser.initialFileName = "packedCombatDefinitions"
                    saveChooser.extensionFilters.setAll(FileChooser.ExtensionFilter("Packed Files", "*.ncd"))
                }
                else -> {
                    saveChooser.initialFileName = "unpackedCombatDefinitionsList"
                    saveChooser.extensionFilters.setAll(FileChooser.ExtensionFilter("Text Files", "*.txt"))
                }
            }
            saveChooser.initialDirectory = lastDirectory ?: null
            val chosenFile = saveChooser.showSaveDialog(primaryStage)
            if (chosenFile != null) {
                CombatDefinitionsIO.save(list = masterRepository.definitions, type = type, file = chosenFile)
                alert(Alert.AlertType.INFORMATION, header = "Save Complete", content = "Finished saving your combat definitions.")
                lastDirectory = chosenFile.parentFile
            }
        }
    }

    fun alwaysQuickDelete(): Boolean {
        return view.alwaysQuickDeleteRadio.isSelected
    }

    private fun askForCombatDefinitionsType(): Optional<DefinitionFileType> {
        val dialog: ChoiceDialog<DefinitionFileType> = ChoiceDialog(DefinitionFileType.UNPACKED,
                DefinitionFileType.UNPACKED, DefinitionFileType.PACKED)
        dialog.title = "File Type"
        dialog.headerText = "Choices"
        dialog.contentText = "Choose your definitions file type: "
        val result = dialog.showAndWait()
        return result
    }

    private fun setupNPCDefinitionsPane() {
        view.npcDefinitionsPane.children.setAll(npcDefinitionsView.root)
    }

    private fun setupEditorPane() {
        view.editorPane.children.setAll(editorView.root)
    }
}
