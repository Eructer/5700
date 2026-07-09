package src.main.kotlin.music

import src.main.kotlin.music.effects.ADS
import src.main.kotlin.music.effects.AudioSource
import src.main.kotlin.music.effects.Volume
import src.main.kotlin.music.effects.ClipDistortion
import src.main.kotlin.music.effects.TahnDistortion
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.math.abs
import kotlin.math.tanh

// --- Test double: a fake AudioSource that returns a known, fixed buffer ---
// This isolates ADS's math from Wave's actual sine/square generation.
class FakeAudioSource(private val value: Double, private val size: Int) : AudioSource {
    override fun render(): DoubleArray = DoubleArray(size) { value }
}

class PianoNotesTest {

    @Test
    fun `known note returns correct frequency`() {
        // A4 is the tuning reference and should be exactly 440.0
        val freq = PianoNotes["A4"]
        assertNotNull(freq)
        assertEquals(440.0, freq!!, 0.001)
    }

    @Test
    fun `sharp and flat enharmonic equivalents match`() {
        val sharp = PianoNotes["C#4"]
        val flat = PianoNotes["Db4"]
        assertNotNull(sharp)
        assertNotNull(flat)
        assertEquals(sharp!!, flat!!, 0.001)
    }

    @Test
    fun `rest symbol maps to zero`() {
        assertEquals(0.0, PianoNotes["-"])
    }

    @Test
    fun `unknown note returns null`() {
        // e.g. malformed or out-of-range note name
        assertNull(PianoNotes["H9"])
        assertNull(PianoNotes[""])
    }

    @Test
    fun `octave increases produce doubling frequency relationship`() {
        // Same pitch class one octave apart should be exactly 2x frequency
        val c4 = PianoNotes["C4"]
        val c5 = PianoNotes["C5"]
        assertNotNull(c4)
        assertNotNull(c5)
        assertEquals(c4!! * 2.0, c5!!, 0.01)
    }
}

class AdsTest {

    private val sampleRate = 100.0 // small round number, makes sample-count math easy to verify by hand

    @Test
    fun `first sample of attack stage is near zero`() {
        // attack = 0.1s -> 10 samples; sample 0 should be at (or very near) silence
        val fake = FakeAudioSource(value = 1.0, size = 50)
        val ads = ADS(fake, attackEnd = 0.1, decayEnd = 0.2, sustain = 0.4, sampleRate = sampleRate)
        val result = ads.render()

        assertTrue(abs(result[0]) < 0.05, "Expected near-zero at n=0, got ${result[0]}")
    }

    @Test
    fun `end of attack stage reaches full amplitude`() {
        // attack = 0.1s -> 10 samples; sample 9 (last attack sample) should be close to 1.0 * wave value
        val fake = FakeAudioSource(value = 1.0, size = 50)
        val ads = ADS(fake, attackEnd = 0.1, decayEnd = 0.2, sustain = 0.4, sampleRate = sampleRate)
        val result = ads.render()

        val lastAttackSample = 9
        assertTrue(result[lastAttackSample] > 0.85, "Expected near-peak at end of attack, got ${result[lastAttackSample]}")
    }

    @Test
    fun `decay stage ends at sustain level`() {
        // attack = 0.1s (10 samples), decay = 0.2s (20 samples) -> decay ends at sample 29
        val fake = FakeAudioSource(value = 1.0, size = 50)
        val sustainLevel = 0.4
        val ads = ADS(fake, attackEnd = 0.1, decayEnd = 0.2, sustain = sustainLevel, sampleRate = sampleRate)
        val result = ads.render()

        val lastDecaySample = 29
        assertEquals(sustainLevel, result[lastDecaySample], 0.05)
    }

    @Test
    fun `sustain stage holds flat at sustain level`() {
        val fake = FakeAudioSource(value = 1.0, size = 50)
        val sustainLevel = 0.4
        val ads = ADS(fake, attackEnd = 0.1, decayEnd = 0.2, sustain = sustainLevel, sampleRate = sampleRate)
        val result = ads.render()

        // Everything from sample 30 onward should be exactly sustainLevel (since wave value is 1.0)
        for (n in 30 until result.size) {
            assertEquals(sustainLevel, result[n], 0.001, "Mismatch at sample $n")
        }
    }

    @Test
    fun `envelope scales the underlying wave value, not just the index`() {
        // Regression test for the "multiplying by n instead of samples[n]" bug.
        // With a constant wave value of 0.5, sustain-stage output must be exactly 0.5 * sustain,
        // regardless of how large n is.
        val fake = FakeAudioSource(value = 0.5, size = 200)
        val ads = ADS(fake, attackEnd = 0.01, decayEnd = 0.01, sustain = 0.6, sampleRate = sampleRate)
        val result = ads.render()

        val expected = 0.5 * 0.6
        assertEquals(expected, result[150], 0.01)
    }

    @Test
    fun `silent wave produces silent output regardless of envelope`() {
        val fake = FakeAudioSource(value = 0.0, size = 50)
        val ads = ADS(fake, attackEnd = 0.1, decayEnd = 0.2, sustain = 0.4, sampleRate = sampleRate)
        val result = ads.render()

        assertTrue(result.all { it == 0.0 })
    }

    @Test
    fun `output buffer is same length as input buffer`() {
        val fake = FakeAudioSource(value = 1.0, size = 123)
        val ads = ADS(fake, attackEnd = 0.05, decayEnd = 0.05, sustain = 0.5, sampleRate = sampleRate)
        val result = ads.render()

        assertEquals(123, result.size)
    }
}

class VolumeTest {

    @Test
    fun `scales every sample by the level`() {
        val fake = FakeAudioSource(value = 0.5, size = 10)
        val volume = Volume(fake, level = 2.0)
        val result = volume.render()

        assertTrue(result.all { abs(it - 1.0) < 0.0001 }, "Expected every sample to be 1.0, got ${result.toList()}")
    }

    @Test
    fun `level of 1 leaves samples unchanged`() {
        val fake = FakeAudioSource(value = 0.3, size = 10)
        val volume = Volume(fake, level = 1.0)
        val result = volume.render()

        assertTrue(result.all { abs(it - 0.3) < 0.0001 })
    }

    @Test
    fun `level of 0 silences everything`() {
        val fake = FakeAudioSource(value = 0.9, size = 10)
        val volume = Volume(fake, level = 0.0)
        val result = volume.render()

        assertTrue(result.all { it == 0.0 })
    }

    @Test
    fun `negative wave values scale correctly too`() {
        val fake = FakeAudioSource(value = -0.5, size = 10)
        val volume = Volume(fake, level = 2.0)
        val result = volume.render()

        assertTrue(result.all { abs(it - (-1.0)) < 0.0001 })
    }

    @Test
    fun `output length matches input length`() {
        val fake = FakeAudioSource(value = 1.0, size = 77)
        val result = Volume(fake, level = 0.5).render()

        assertEquals(77, result.size)
    }
}

class ClipDistortionTest {

    @Test
    fun `values within threshold pass through unchanged`() {
        val fake = FakeAudioSource(value = 0.3, size = 10)
        val clip = ClipDistortion(fake, threshold = 0.8)
        val result = clip.render()

        assertTrue(result.all { abs(it - 0.3) < 0.0001 })
    }

    @Test
    fun `positive values above threshold are clamped to threshold`() {
        val fake = FakeAudioSource(value = 0.95, size = 10)
        val clip = ClipDistortion(fake, threshold = 0.5)
        val result = clip.render()

        assertTrue(result.all { it == 0.5 })
    }

    @Test
    fun `negative values below negative threshold are clamped to negative threshold`() {
        val fake = FakeAudioSource(value = -0.95, size = 10)
        val clip = ClipDistortion(fake, threshold = 0.5)
        val result = clip.render()

        assertTrue(result.all { it == -0.5 })
    }

    @Test
    fun `value exactly at threshold is unaffected`() {
        val fake = FakeAudioSource(value = 0.5, size = 10)
        val clip = ClipDistortion(fake, threshold = 0.5)
        val result = clip.render()

        assertTrue(result.all { it == 0.5 })
    }

    @Test
    fun `output length matches input length`() {
        val fake = FakeAudioSource(value = 1.0, size = 42)
        val result = ClipDistortion(fake, threshold = 0.5).render()

        assertEquals(42, result.size)
    }
}

class TanhDistortionTest {

    @Test
    fun `small drive on small signal stays close to original value`() {
        // tanh(x) ≈ x for small x, so low drive on a quiet signal should barely change it
        val fake = FakeAudioSource(value = 0.1, size = 10)
        val distortion = TahnDistortion(fake, drive = 1.0)
        val result = distortion.render()

        assertEquals(tanh(0.1), result[0], 0.0001)
    }

    @Test
    fun `high drive saturates output close to plus or minus one`() {
        val fake = FakeAudioSource(value = 1.0, size = 10)
        val distortion = TahnDistortion(fake, drive = 20.0)
        val result = distortion.render()

        assertTrue(result.all { abs(it - 1.0) < 0.01 }, "Expected near +1.0 saturation, got ${result.toList()}")
    }

    @Test
    fun `negative signal saturates toward negative one under high drive`() {
        val fake = FakeAudioSource(value = -1.0, size = 10)
        val distortion = TahnDistortion(fake, drive = 20.0)
        val result = distortion.render()

        assertTrue(result.all { abs(it - (-1.0)) < 0.01 })
    }

    @Test
    fun `output never exceeds range negative one to one regardless of drive`() {
        // tanh's defining property: no matter how extreme the input, output stays in (-1, 1)
        val fake = FakeAudioSource(value = 5.0, size = 10)
        val distortion = TahnDistortion(fake, drive = 2.0)
        val result = distortion.render()

        assertTrue(result.all { it > -1.0 && it < 1.0 })
    }

    @Test
    fun `zero input produces zero output regardless of drive`() {
        val fake = FakeAudioSource(value = 0.0, size = 10)
        val distortion = TahnDistortion(fake, drive = 50.0)
        val result = distortion.render()

        assertTrue(result.all { it == 0.0 })
    }

    @Test
    fun `output length matches input length`() {
        val fake = FakeAudioSource(value = 0.5, size = 33)
        val result = TahnDistortion(fake, drive = 2.0).render()

        assertEquals(33, result.size)
    }
}

class DecoratorChainingTest {
    // Verifies that decorators actually compose - wrapping one inside another
    // produces the combined effect, in the order they're applied.

    @Test
    fun `volume then clip applies volume first, then clamps the result`() {
        val fake = FakeAudioSource(value = 0.6, size = 5)
        val chain = ClipDistortion(Volume(fake, level = 2.0), threshold = 0.9)
        val result = chain.render()

        // 0.6 * 2.0 = 1.2, clamped to 0.9
        assertTrue(result.all { it == 0.9 })
    }

    @Test
    fun `clip then volume clamps first, then scales the clamped result`() {
        val fake = FakeAudioSource(value = 0.6, size = 5)
        val chain = Volume(ClipDistortion(fake, threshold = 0.4), level = 2.0)
        val result = chain.render()

        // 0.6 clamped to 0.4, then * 2.0 = 0.8
        assertTrue(result.all { abs(it - 0.8) < 0.0001 })
    }

    @Test
    fun `order of decorators changes the result - order matters`() {
        val fakeA = FakeAudioSource(value = 0.6, size = 5)
        val fakeB = FakeAudioSource(value = 0.6, size = 5)

        val volumeThenClip = ClipDistortion(Volume(fakeA, level = 2.0), threshold = 0.9).render()
        val clipThenVolume = Volume(ClipDistortion(fakeB, threshold = 0.9), level = 2.0).render()

        // These should differ, proving the chain isn't order-independent
        assertNotEquals(volumeThenClip[0], clipThenVolume[0], 0.0001)
    }
}