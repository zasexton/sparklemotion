package baaahs.app.ui.model

import kotlinx.css.*
import materialui.styles.muitheme.MuiTheme
import styled.StyleSheet

class ModelEditorStyles(val theme: MuiTheme) : StyleSheet("app-model", isStatic = true) {
    val editorPanes by css {
        display = Display.grid
        gridTemplateColumns = GridTemplateColumns(
            GridAutoRows(20.pct),
            GridAutoRows(60.pct),
            GridAutoRows(20.pct)
        )
        gridTemplateRows = GridTemplateRows(100.pct)
        height = 100.pct
    }

    val navigatorPane by css {
        display = Display.grid
        gridTemplateRows = GridTemplateRows(GridAutoRows.minContent, GridAutoRows.auto)
        height = 100.pct
    }
    val navigatorPaneContent by css {
        minHeight = 0.px
        overflow = Overflow.scroll
    }

    val entityList by css {
        child("li") {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.inherit
        }
    }

    val visualizerPane by css {
        position = Position.relative
        height = 100.pct
    }

    val visualizer by css {
        position = Position.absolute
        top = 0.px
        left = 0.px
        bottom = 0.px
        right = 0.px
    }

    val propertiesPane by css {
        display = Display.grid
        gridTemplateRows = GridTemplateRows(GridAutoRows.minContent, GridAutoRows.auto)
        height = 100.pct
    }
    val propertiesPaneContent by css {
        minHeight = 0.px
        overflow = Overflow.scroll
    }

    val transformEditBox by css {
        whiteSpace = WhiteSpace.nowrap
//        color = theme.palette.primary.contrastText
//        backgroundColor = theme.palette.primary.main

        input {
//            color = theme.palette.primary.contrastText
            fontSize = .8.em
            width = 5.em
            textAlign = TextAlign.right
        }
    }

    val partialUnderline by css {
        before {
            right = LinearDimension.inherit
            width = 4.em
        }
    }
}