package eu.karcags.mythscape.notedsl

import eu.karcags.mythscape.core.notes.NoteBlock
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import kotlin.time.Duration.Companion.milliseconds

class NoteDslEvaluator(
    private val timeoutMillis: Long = 3_000L,
) {
    private val engine by lazy {
        ScriptEngineManager().getEngineByExtension("kts")
            ?: error("kts engine not found")
    }

    suspend fun evaluate(rawDsl: String): List<NoteBlock> = withContext(Dispatchers.IO) {
        if (rawDsl.isBlank()) return@withContext emptyList()

        val wrappedScript = """
            import eu.karcags.mythscape.notedsl.*
            
            note {
                $rawDsl
            }
        """.trimIndent()

        try {
            withTimeout(timeoutMillis.milliseconds) {
                @Suppress("UNCHECKED_CAST")
                engine.eval(wrappedScript) as? List<NoteBlock> ?: emptyList()
            }
        } catch (e: ScriptException) {
            val adjustedLine = (e.lineNumber - 3).takeIf { it > 0 }
            listOf(
                NoteBlock.SyntaxError(
                    lineNumber = adjustedLine,
                    message = e.message ?: "DSL syntax error",
                )
            )
        } catch (e: Exception) {
            if (e is CancellationException) {
                ensureActive()
            }
            listOf(
                NoteBlock.SyntaxError(
                    message = e.message ?: "DSL evaluation failed",
                )
            )
        }
    }
}