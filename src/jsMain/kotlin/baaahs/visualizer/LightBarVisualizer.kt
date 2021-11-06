package baaahs.visualizer

import baaahs.model.PixelArray
import three.js.BoxGeometry
import three.js.Mesh
import three.js.MeshNormalMaterial
import three.js.Vector3

class LightBarVisualizer(
    pixelArray: PixelArray,
    vizPixels: VizPixels? = null
) : EntityVisualizer {
    override val title: String = pixelArray.name
    override var mapperIsRunning: Boolean = false

    override var selected: Boolean = false
        set(value) {
            boxMesh.material.wireframeLinewidth = if (value) 3 else 1
            boxMesh.material.needsUpdate = true
            field = value
        }

    private var vizScene: VizScene? = null
    var vizPixels : VizPixels? = vizPixels
        set(value) {
            vizScene?.let { scene ->
                field?.removeFromScene(scene)
                value?.addToScene(scene)
            }

            field = value
        }

    private val boxMesh: Mesh<BoxGeometry, MeshNormalMaterial>

    init {
        val bounds = pixelArray.bounds
        val startVertex = bounds.first
        val endVertex = bounds.second
        val normal = endVertex.minus(startVertex).normalize()

        // TODO: This is wrong.
        val width = endVertex.x - startVertex.x
        val length = endVertex.y - startVertex.y

        val boxGeom = BoxGeometry(width, length, 1)
        boxGeom.translate(width / 2, length / 2, 0)

        Rotator(Vector3(0, 1, 0), normal.toVector3())
            .rotate(boxGeom)

        with(startVertex) { boxGeom.translate(x, y, z) }

        boxMesh = Mesh(boxGeom, MeshNormalMaterial().apply {
            wireframe = true
        })
    }

    override fun addTo(scene: VizScene) {
        scene.add(VizObj(boxMesh))
        vizPixels?.addToScene(scene)
        vizScene = scene
    }
}