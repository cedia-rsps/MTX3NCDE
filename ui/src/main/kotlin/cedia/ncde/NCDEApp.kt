package cedia.ncde

import cedia.ncde.io.store.NPCNamesStore
import cedia.ncde.ui.view.ParentView
import tornadofx.App

/**
 * Created on 2016-05-22.
 */
class NCDEApp : App() {

    override val primaryView = ParentView::class

    override fun init() {
        super.init()
        NPCNamesStore.init()
    }

}