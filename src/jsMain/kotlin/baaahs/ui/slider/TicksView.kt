//package baaahs.ui.slider
//
//import baaahs.ui.xComponent
//import js.core.jso
//import react.*
//
////external interface TicksObject : RcsProps {
////    var ticks: Array<Handle>
////}
////
////external interface RcsProps : Props, StandardEventEmitters {
////    var scale: LinearScale?
////    var handles: Array<Handle>
////    var activeHandleId: String?
////    var getEventData: GetEventData?
////}
//
//private val BetterTicks = xComponent<TicksProps>("BetterTicks") { props ->
//    val scale = props.scale
//    val ticks = memo(props.values, scale, props.count) {
//        (props.values ?: scale.getTicks(props.count)).map { value ->
//            jso<Handle> {
//                id = "$$-$value"
//                this.value = value
//                percent = scale.getValue(value)
//            }
//        }.toTypedArray()
//    }
//
//    val ticksObject = jso<TicksObject> {
//        this.scale = props.scale
//        this.ticks = ticks
//        activeHandleId = props.activeHandleId
//        getEventData = props.getEventData
//
//    }
//
//    +Children.only(props.children.invoke(ticksObject))
//}
//
//external interface TicksProps : Props {
//    /** @ignore */
//    var scale: LinearScale
//    /**
//     * Approximate number of ticks you want to render.
//     * If you supply your own ticks using the values prop this prop has no effect.
//     */
//    var count: Int?
//    /**
//     * The values prop should be an array of unique numbers.
//     * Use this prop if you want to specify your own tick values instead of ticks generated by the slider.
//     * The numbers should be valid numbers in the domain and correspond to the step value.
//     * Invalid values will be coerced to the closet matching value in the domain.
//     */
//    var values: Array<Double>?
//    /** @ignore */
//    var getEventData: GetEventData?
//    /** @ignore */
//    var activeHandleId: String?
////    /** @ignore */
////    var emitMouse: EmitMouse?
////    /** @ignore */
////    var emitTouch: EmitTouch?
//    /**
//     * A function to render the ticks.
//     * The function receives an object with an array of ticks. Note: `getEventData` can be called with an event and get the value and percent at that location (used for tooltips etc). `activeHandleID` will be a string or null.  Function signature:
//     * `({ getEventData, activeHandleID, ticks  }): element`
//     */
//    var children: (ticksObject: TicksObject) -> ReactElement<*>
//}
//
//fun RBuilder.ticks(handler: RHandler<TicksProps>) =
//    child(BetterTicks, handler = handler)
