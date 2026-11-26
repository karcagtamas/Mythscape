package eu.karcags.mythscape.ui.main.note

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.karcags.mythscape.core.dtos.campaigns.CampaignTagDTO
import eu.karcags.mythscape.core.notes.NoteBlock
import eu.karcags.mythscape.data.note.NoteTab
import eu.karcags.mythscape.enums.EditorViewMode
import eu.karcags.mythscape.ui.components.common.HorizontalLine
import eu.karcags.mythscape.ui.components.common.VerticalLine
import eu.karcags.mythscape.utils.note.NoteDslVisualTransformation
import eu.karcags.mythscape.viewmodel.note.NoteEditorViewModel
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.ic_description_24
import mythscape.app.shared.generated.resources.ic_image_24
import mythscape.app.shared.generated.resources.ic_link_24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteEditorScreen(
    tab: NoteTab,
    tags: List<CampaignTagDTO>,
    onDraftChanged: (title: String, content: String, tagId: Int?) -> Unit,
    onNavigateToNote: (Int) -> Unit = {},
    viewModel: NoteEditorViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(tab.noteId) {
        viewModel.loadTab(tab)
    }

    val selectedTag = remember(state.tagId, tags) {
        tags.find { it.id == state.tagId }
    }
    var showTagMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            BasicTextField(
                value = state.title,
                onValueChange = { newTitle ->
                    viewModel.onTitleChange(newTitle)
                    onDraftChanged(newTitle, state.content, state.tagId)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (state.title.isEmpty()) {
                        Text(
                            text = "Note title...",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.weight(1f),
            )

            Box {
                FilterChip(
                    selected = selectedTag != null,
                    onClick = { showTagMenu = true },
                    label = {
                        Text(
                            text = selectedTag?.caption ?: "No Tag",
                            maxLines = 1,
                            softWrap = false,
                        )
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand),
                )

                DropdownMenu(
                    expanded = showTagMenu,
                    onDismissRequest = { showTagMenu = false },
                ) {
                    if (state.tagId != null) {
                        DropdownMenuItem(
                            text = { Text("Clear Tag") },
                            onClick = {
                                viewModel.onTagChange(null)
                                onDraftChanged(state.title, state.content, null)
                                showTagMenu = false
                            },
                        )
                        HorizontalLine()
                    }

                    tags.forEach { tag ->
                        DropdownMenuItem(
                            text = { Text(tag.caption) },
                            onClick = {
                                viewModel.onTagChange(tag.id)
                                onDraftChanged(state.title, state.content, tag.id)
                                showTagMenu = false
                            },
                        )
                    }
                }
            }
        }

        HorizontalLine()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(state = rememberScrollState())
            ) {
                DslSnippetChip("+ Section") {
                    val updated = viewModel.appendSnippet("""section("Section Title") {\n    text("")\n}""")
                    onDraftChanged(state.title, updated, state.tagId)
                }
                DslSnippetChip("+ Text") {
                    val updated = viewModel.appendSnippet("""text("")""")
                    onDraftChanged(state.title, updated, state.tagId)
                }
                DslSnippetChip("+ Callout") {
                    val updated = viewModel.appendSnippet("""callout(title = "Note", text = "")""")
                    onDraftChanged(state.title, updated, state.tagId)
                }
                DslSnippetChip("+ List") {
                    val updated = viewModel.appendSnippet("""list {\n    item("")\n}""")
                    onDraftChanged(state.title, updated, state.tagId)
                }
                DslSnippetChip("+ Note Link") {
                    val updated = viewModel.appendSnippet("""noteLink(type = "Character", id = 1, label = "Name")""")
                    onDraftChanged(state.title, updated, state.tagId)
                }
                DslSnippetChip("+ Image") {
                    val updated = viewModel.appendSnippet("""image(url = "https://", caption = "Caption")""")
                    onDraftChanged(state.title, updated, state.tagId)
                }
                DslSnippetChip("+ Link") {
                    val updated = viewModel.appendSnippet("""link(label = "Link", url = "https://")""")
                    onDraftChanged(state.title, updated, state.tagId)
                }
            }

            if (state.isEvaluating) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 6.dp),
                )
            }

            SingleChoiceSegmentedButtonRow {
                EditorViewMode.entries.forEachIndexed { index, mode ->
                    SegmentedButton(
                        selected = state.viewMode == mode,
                        onClick = { viewModel.setViewMode(mode) },
                        shape = SegmentedButtonDefaults.itemShape(index, EditorViewMode.entries.size),
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand),
                    ) {
                        Text(
                            text = mode.name,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }
            }
        }

        HorizontalLine()

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            if (state.viewMode == EditorViewMode.Code || state.viewMode == EditorViewMode.Split) {
                val primaryColor = MaterialTheme.colorScheme.primary
                val secondaryColor = MaterialTheme.colorScheme.secondary
                val tertiaryColor = MaterialTheme.colorScheme.tertiary
                val outlineColor = MaterialTheme.colorScheme.outline

                val dslTransformation = remember(primaryColor, secondaryColor, tertiaryColor, outlineColor) {
                    NoteDslVisualTransformation(
                        keywordColor = primaryColor,
                        paramColor = secondaryColor,
                        stringColor = Color(0xFF2E7D32),
                        numberColor = tertiaryColor,
                        commentColor = outlineColor,
                    )
                }

                BasicTextField(
                    value = state.content,
                    onValueChange = { newContent ->
                        viewModel.onContentChange(newContent)
                        onDraftChanged(state.title, newContent, state.tagId)
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    visualTransformation = dslTransformation,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (state.content.isEmpty()) {
                            Text(
                                text = "Write Kotlin DSL blocks (e.g., section(\"Overview\") { text(\"...\") }) or use the toolbar above...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(state = rememberScrollState())
                        .padding(16.dp),
                )
            }

            if (state.viewMode == EditorViewMode.Split) {
                VerticalLine()
            }

            if (state.viewMode == EditorViewMode.Preview || state.viewMode == EditorViewMode.Split) {
                NoteDslPreviewPane(
                    blocks = state.blocks,
                    onNoteLinkClick = onNavigateToNote,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(state = rememberScrollState())
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun DslSnippetChip(
    label: String,
    onClick: () -> Unit,
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label, maxLines = 1, softWrap = false) },
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
    )
}

@Composable
private fun NoteDslPreviewPane(
    blocks: List<NoteBlock>,
    onNoteLinkClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (blocks.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier,
        ) {
            Text(
                text = "Live preview will appear here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        blocks.forEach { block ->
            NoteBlockView(block = block, onNoteLinkClick = onNoteLinkClick)
        }
    }
}

@Composable
private fun NoteBlockView(
    block: NoteBlock,
    onNoteLinkClick: (Int) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    when (block) {
        is NoteBlock.Section -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = block.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 12.dp),
                ) {
                    block.children.forEach { child ->
                        NoteBlockView(child, onNoteLinkClick = onNoteLinkClick)
                    }
                }
            }
        }

        is NoteBlock.Paragraph -> {
            Text(
                text = block.text,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        is NoteBlock.Callout -> {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = block.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = block.text,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        is NoteBlock.BulletList -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                block.items.forEach { item ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "•",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }

        is NoteBlock.Image -> {
            OutlinedCard(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_image_24),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = block.url,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    block.caption?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        is NoteBlock.ExternalLink -> {
            TextButton(
                onClick = {
                    if (block.url.isNotEmpty()) {
                        uriHandler.openUri(block.url)
                    }
                },
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_link_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(block.label)
            }
        }

        is NoteBlock.NoteRef -> {
            AssistChip(
                onClick = { onNoteLinkClick(block.targetId) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_description_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                },
                label = { Text("${block.targetType}:${block.label}") },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
            )
        }

        is NoteBlock.SyntaxError -> {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                val prefix = block.lineNumber?.let { "Line $it" } ?: ""
                Text(
                    text = "$prefix${block.message}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier
                        .padding(8.dp),
                )
            }
        }
    }
}