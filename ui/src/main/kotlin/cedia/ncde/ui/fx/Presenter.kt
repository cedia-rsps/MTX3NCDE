package cedia.ncde.ui.fx

import tornadofx.Controller

/**
 * Created on 2016-05-22.
 */
abstract class Presenter : Controller() {

    abstract val view: AbstractView

    abstract fun initialize()


}