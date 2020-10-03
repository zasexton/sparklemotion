package baaahs.app.ui.editor

import baaahs.app.ui.ControlEditIntent
import baaahs.show.mutable.MutableButtonControl
import baaahs.show.mutable.MutableShow
import describe
import org.spekframework.spek2.Spek
import kotlin.test.expect

object EditableSpec : Spek({
    describe<ControlEditIntent> {
        val baseShow by value {
            MutableShow("test show") {
                addButton("main", "main button") { }
            }.getShow()

                .also { println("it = ${it}") }
        }
        val editIntent by value { ControlEditIntent("mainButtonButton") }
        val editableManager by value {
            EditableManager { }
                .apply { openEditor(baseShow, editIntent) }
        }
        val mutableButton by value {
            editableManager.session!!.mutableEditable as MutableButtonControl
        }

        context("when the id of its control changes") {
            beforeEachTest {
                mutableButton.title = "new title for button"
                editableManager.onChange()
                editableManager.undo()
                editableManager.redo()
            }

            it("provides an appropriate refreshed edit intent for the undo stack") {
                val newEditIntent = (editableManager.session!!.editIntent as ControlEditIntent)
                expect("newTitleForButtonButton") { newEditIntent.controlId }
            }
        }
    }
})