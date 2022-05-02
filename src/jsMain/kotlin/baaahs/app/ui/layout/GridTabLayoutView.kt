package baaahs.app.ui.layout

import baaahs.app.ui.appContext
import baaahs.app.ui.editor.AddControlToGrid
import baaahs.app.ui.editor.Editor
import baaahs.show.live.ControlProps
import baaahs.show.live.OpenIGridLayout
import baaahs.show.mutable.MutableIGridLayout
import baaahs.show.mutable.MutableShow
import baaahs.ui.and
import baaahs.ui.gridlayout.*
import baaahs.ui.unaryMinus
import baaahs.ui.unaryPlus
import baaahs.ui.xComponent
import baaahs.util.useResizeListener
import baaahs.window
import external.react_resizable.buildResizeHandle
import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.html.js.onClickFunction
import materialui.icon
import mui.icons.material.Add
import mui.material.ListItemIcon
import mui.material.ListItemText
import mui.material.Menu
import mui.material.MenuItem
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.Props
import react.RBuilder
import react.RHandler
import react.dom.div
import react.useContext
import styled.StyleSheet
import styled.inlineStyles

private val GridTabLayoutView = xComponent<GridTabLayoutProps>("GridTabLayout") { props ->
    val appContext = useContext(appContext)
    val styles = appContext.allStyles.layout

    val gridLayoutContext = useContext(dragNDropContext).gridLayoutContext
    observe(gridLayoutContext)

    var layoutDimens by state { window.innerWidth to window.innerHeight }
    val gridLayout = props.tab
    val columns = gridLayout.columns
    val rows = gridLayout.rows

    var showAddMenu by state<AddMenuContext?> { null }

    val editMode = observe(appContext.showManager.editMode)
    var draggingItem by state<String?> { null }

    val gridLayoutEditor = props.tabEditor
    val handleLayoutChange by handler(gridLayout, gridLayoutEditor) { newLayout: Layout ->
        appContext.showManager.openShow?.edit {
            val mutableShow = this
            gridLayoutEditor.edit(mutableShow) {
                applyChanges(gridLayout.items, newLayout, mutableShow)
            }
            appContext.showManager.onEdit(mutableShow)
        }
        Unit
    }

    val handleDragStart: ItemCallback by handler {
            layout, oldItem, newItem, placeholder, e, element ->
        draggingItem = newItem.i
    }
    val handleDragStop: ItemCallback by handler {
            layout, oldItem, newItem, placeholder, e, element ->
        draggingItem = null
    }

    val handleEmptyGridCellClick by eventHandler { e ->
        val target = e.currentTarget as HTMLElement
        val dataset = target.dataset.asDynamic()
        val x = (dataset.cellX as String).toInt()
        val y = (dataset.cellY as String).toInt()
        showAddMenu = AddMenuContext(target, x, y, 1, 1)
    }

    val containerDiv = ref<HTMLDivElement>()
    useResizeListener(containerDiv) {
        layoutDimens = with(containerDiv.current!!) { clientWidth to clientHeight }
    }
    val (layoutWidth, layoutHeight) = layoutDimens
    val margin = 5
    val itemPadding = 5
    val gridRowHeight = (layoutHeight.toDouble() - margin) / rows - itemPadding

    val layoutGrid = memo(columns, rows, gridLayout, draggingItem) {
        LayoutGrid(columns, rows, gridLayout.items, draggingItem)
    }

    val genericControlProps = memo(props.onShowStateChange) {
        ControlProps(props.onShowStateChange, null)
    }


    div(+styles.gridOuterContainer and
            (+if (editMode.isOn) styles.editModeOn else styles.editModeOff) and
            +if (gridLayoutContext.dragging) styles.dragging else styles.notDragging
    ) {
        ref = containerDiv

        if (editMode.isAvailable) {
            div(+styles.gridBackground) {
                val positionParams = PositionParams(
                    margin to margin,
                    itemPadding to itemPadding,
                    layoutWidth,
                    columns,
                    gridRowHeight,
                    rows
                )

                layoutGrid.forEachCell { column, row ->
                    val position = positionParams.calcGridItemPosition(column, row, 1, 1)

                    div(+styles.emptyGridCell) {
                        inlineStyles {
                            top = position.top.px
                            left = position.left.px
                            width = position.width.px
                            height = position.height.px
                        }
                        attrs["data-cell-x"] = column
                        attrs["data-cell-y"] = row
                        attrs.onClickFunction = handleEmptyGridCellClick

                        icon(Add)
                    }
                }
            }
        }

        gridLayout {
            attrs.id = "top"
            attrs.className = +styles.gridContainer
            attrs.width = layoutDimens.first.toDouble()
            attrs.autoSize = false
            attrs.cols = columns
            attrs.rowHeight = gridRowHeight
            attrs.maxRows = rows
            attrs.margin = 5 to 5
            attrs.layout = layoutGrid.layout
            attrs.onLayoutChange = handleLayoutChange
            attrs.compactType = CompactType.none
            attrs.resizeHandle = ::buildResizeHandle
            attrs.disableDrag = !editMode.isOn
            attrs.disableResize = !editMode.isOn
            attrs.isDroppable = editMode.isOn
            attrs.onDragStart = handleDragStart
            attrs.onDragStop = handleDragStop

            gridLayout.items.forEachIndexed { index, item ->
                div(+styles.gridCell) {
                    key = item.control.id

                    val editor = object : Editor<MutableIGridLayout> {
                        override fun edit(mutableShow: MutableShow, block: MutableIGridLayout.() -> Unit) {
                            mutableShow.editLayouts {
                                props.tabEditor.edit(mutableShow) {
                                    block(items[index].layout
                                        ?: error("Couldn't find item for ${item.control.id}."))
                                }
                            }
                        }
                    }

                    gridItem {
                        attrs.control = item.control
                        attrs.controlProps = genericControlProps.withLayout(item.layout, editor)
                        attrs.className = -styles.controlBox
                    }
                }
            }
        }
    }

    showAddMenu?.let { addMenuContext ->
        Menu {
            attrs.anchorEl = { addMenuContext.anchorEl }
            attrs.open = true
            attrs.onClose = { showAddMenu = null }

            appContext.plugins.addControlMenuItems.forEach { addControlMenuItem ->
                MenuItem {
                    attrs.onClick = {
                        val editIntent = AddControlToGrid(
                            gridLayoutEditor,
                            addMenuContext.column, addMenuContext.row,
                            addMenuContext.width, addMenuContext.height,
                            addControlMenuItem.createControlFn
                        )
                        appContext.openEditor(editIntent)
                        showAddMenu = null
                    }

                    ListItemIcon { icon(addControlMenuItem.icon) }
                    ListItemText { +addControlMenuItem.label }
                }
            }
        }
    }
}

private class AddMenuContext(
    val anchorEl: HTMLElement,
    val column: Int,
    val row: Int,
    val width: Int = 1,
    val height: Int = 1
)


object Styles : StyleSheet("ui-layout-grid", isStatic = true) {
    val gridItem by css {
        border(1.px, BorderStyle.solid, Color.orange)
    }
}

external interface GridTabLayoutProps : Props {
    var tab: OpenIGridLayout
    var controlProps: ControlProps
    var tabEditor: Editor<MutableIGridLayout>
    var onShowStateChange: () -> Unit
}

fun RBuilder.gridTabLayout(handler: RHandler<GridTabLayoutProps>) =
    child(GridTabLayoutView, handler = handler)