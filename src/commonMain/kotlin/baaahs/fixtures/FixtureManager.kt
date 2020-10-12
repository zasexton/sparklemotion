package baaahs.fixtures

import baaahs.Pixels
import baaahs.RenderPlan
import baaahs.ShowRunner
import baaahs.gl.glsl.GlslProgram
import baaahs.gl.patch.LinkedPatch
import baaahs.gl.render.FixtureRenderPlan
import baaahs.gl.render.ModelRenderer

class FixtureManager(
    private val modelRenderer: ModelRenderer
) {
    private val changedFixtures = mutableListOf<ShowRunner.FixturesChanges>()
    internal val fixtureRenderPlans: MutableMap<Fixture, FixtureRenderPlan> = hashMapOf()
    private var totalFixtureReceivers = 0

    fun getFixtureRenderPlans_ForTestOnly(): Map<Fixture, FixtureRenderPlan> {
        return fixtureRenderPlans
    }

    fun fixturesChanged(addedFixtures: Collection<ShowRunner.FixtureReceiver>, removedFixtures: Collection<ShowRunner.FixtureReceiver>) {
        changedFixtures.add(ShowRunner.FixturesChanges(ArrayList(addedFixtures), ArrayList(removedFixtures)))
    }

    fun requiresRemap(): Boolean {
        var anyChanges = false

        for ((added, removed) in changedFixtures) {
            ShowRunner.logger.info { "ShowRunner surfaces changed! ${added.size} added, ${removed.size} removed" }
            for (receiver in removed) removeReceiver(receiver)
            for (receiver in added) addReceiver(receiver)
            anyChanges = true
        }
        changedFixtures.clear()
        return anyChanges
    }

    fun remap(renderPlan: RenderPlan) {
        fixtureRenderPlans.forEach { (fixture, fixtureRenderPlan) ->
            renderPlan.programs.forEach { (patch: LinkedPatch, program: GlslProgram) ->
                if (patch.matches(fixture)) {
                    fixtureRenderPlan.useProgram(program)
                }
            }
        }
    }

    fun clearRenderPlans() {
        fixtureRenderPlans.values.forEach { it.release() }
    }

    fun getFixtureCount(): Int {
        return fixtureRenderPlans.size
    }

    fun sendFrame() {
        fixtureRenderPlans.values.forEach { fixtureRenderPlan ->
//            if (shaderBuffers.size != 1) {
//                throw IllegalStateException("Too many shader buffers for ${surface.describe()}: $shaderBuffers")
//            }

            fixtureRenderPlan.receivers.forEach { receiver ->
                // TODO: The send might return an error, at which point this receiver should be nuked
                // from the list of receivers for this fixture. I'm not quite sure the best way to do
                // that so I'm leaving this note.
                receiver.send(fixtureRenderPlan.renderResult as Pixels)
            }
        }
    }

    private fun addReceiver(receiver: ShowRunner.FixtureReceiver) {
        val fixture = receiver.fixture
        val fixtureRenderPlan = fixtureRenderPlans.getOrPut(fixture) {
            modelRenderer.addFixture(fixture)
        }
        fixtureRenderPlan.receivers.add(receiver)

        totalFixtureReceivers++
    }

    private fun removeReceiver(receiver: ShowRunner.FixtureReceiver) {
        val fixture = receiver.fixture
        val fixtureRenderPlan = fixtureRenderPlans[fixture]
            ?: throw IllegalStateException("huh? no SurfaceBinder for $fixture")

        if (!fixtureRenderPlan.receivers.remove(receiver)) {
            throw IllegalStateException("huh? receiver not registered for $fixture")
        }

        if (fixtureRenderPlan.receivers.isEmpty()) {
            modelRenderer.removeFixture(fixtureRenderPlan)
            fixtureRenderPlan.release()
            fixtureRenderPlans.remove(fixture)
        }

        totalFixtureReceivers--
    }
}