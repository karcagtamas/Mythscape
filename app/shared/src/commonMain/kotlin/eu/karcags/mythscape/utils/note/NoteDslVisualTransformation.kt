package eu.karcags.mythscape.utils.note

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class NoteDslVisualTransformation(
    private val keywordColor: Color,
    private val paramColor: Color,
    private val stringColor: Color,
    private val numberColor: Color,
    private val commentColor: Color,
) : VisualTransformation {

    private val keywordRegex = Regex("""\b(section|text|callout|list|item|image|link|noteLink|val|var|for|if|else)\b""")
    private val paramRegex = Regex("""\b(title|text|url|caption|label|type|id)\s*=""")
    private val numberRegex = Regex("""\b\d+\b""")
    private val stringRegex = Regex(""""([^"\\]|\\.)*"""")
    private val commentRegex = Regex("""//.*$""", RegexOption.MULTILINE)

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val styled = buildAnnotatedString {
            append(raw)

            keywordRegex.findAll(raw).forEach {
                addStyle(
                    SpanStyle(
                        color = keywordColor,
                        fontWeight = FontWeight.Bold,
                    ),
                    it.range.first,
                    it.range.last + 1,
                )
            }
            paramRegex.findAll(raw).forEach {
                addStyle(
                    SpanStyle(
                        color = paramColor,
                    ),
                    it.range.first,
                    it.range.last + 1,
                )
            }
            numberRegex.findAll(raw).forEach {
                addStyle(
                    SpanStyle(
                        color = numberColor,
                    ),
                    it.range.first,
                    it.range.last + 1,
                )
            }
            stringRegex.findAll(raw).forEach {
                addStyle(
                    SpanStyle(
                        color = stringColor,
                    ),
                    it.range.first,
                    it.range.last + 1,
                )
            }
            commentRegex.findAll(raw).forEach {
                addStyle(
                    SpanStyle(
                        color = commentColor,
                        fontStyle = FontStyle.Italic,
                    ),
                    it.range.first,
                    it.range.last + 1,
                )
            }
        }
        return TransformedText(styled, OffsetMapping.Identity)
    }
}