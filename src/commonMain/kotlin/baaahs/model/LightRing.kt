package baaahs.model

import baaahs.device.DeviceType
import baaahs.device.PixelArrayDevice
import baaahs.geom.Matrix4F
import baaahs.geom.Vector3F
import baaahs.geom.boundingBox
import baaahs.model.WtfMaths.cross
import baaahs.sim.FixtureSimulation
import baaahs.sim.LightRingSimulation
import baaahs.sim.SimulationEnv
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class LightRing(
    override val name: String,
    override val description: String?,
    val center: Vector3F, // TODO: Represent using transformation translation.
    val radius: Float, // TODO: Represent using transformation scale?
    val planeNormal: Vector3F, // TODO: Represent using transformation rotation.
    val firstPixelRadians: Float = 0f,
    val pixelDirection: PixelDirection = PixelDirection.Clockwise,
    override val transformation: Matrix4F = Matrix4F.identity
) : Model.Entity, LinearPixelArray {
    override val deviceType: DeviceType
        get() = PixelArrayDevice

    override val bounds: Pair<Vector3F, Vector3F>
        get() = boundingBox(calculatePixelLocations(4)) // This oughta cover it, right?

    val circumference: Float
        get() = (radius * PI).toFloat()

    val length: Float
        get() = circumference

    // For calculating pixel positions around a circle, given planeNormal.
    private val pixelPlotVectors by lazy {
        val v3 = planeNormal.normalize()
        val v1 = Vector3F(v3.z, 0f, -v3.x).normalize()
        val v2 = v3.cross(v1)
        v1 to v2
    }

    /** A light ring's pixels are evenly spaced along its circumference. */
    override fun calculatePixelLocation(index: Int, count: Int): Vector3F {
        val pI = when (pixelDirection) {
            PixelDirection.Clockwise -> if (index == 0) 0 else count - index
            PixelDirection.Counterclockwise -> index
        }
        val (v1, v2) = pixelPlotVectors
        val a = 2 * PI * (pI / count.toDouble()) + firstPixelRadians
        return center + (v1 * cos(a) + v2 * sin(a)) * radius
    }

    override fun createFixtureSimulation(simulationEnv: SimulationEnv): FixtureSimulation =
        LightRingSimulation(this, simulationEnv)

    enum class PixelDirection {
        Clockwise,
        Counterclockwise
    }

    companion object {
        val facingForward = Vector3F(0f, 0f, 1f)
    }
}

object WtfMaths {
    fun Vector3F.cross(v: Vector3F): Vector3F {
        val rx = fma(y, v.z, -z * v.y)
        val ry = fma(z, v.x, -x * v.z)
        val rz = fma(x, v.y, -y * v.x)
        return Vector3F(rx, ry, rz)
    }

    private fun fma(a: Double, b: Double, c: Double): Double {
        return a * b + c
    }

    private fun fma(a: Float, b: Float, c: Float): Float {
        return a * b + c
    }
}