package eu.karcags.mythscape.ui.components.common.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.karcags.mythscape.dtos.UserDTO
import mythscape.app.shared.generated.resources.Res
import mythscape.app.shared.generated.resources.ic_check_24
import mythscape.app.shared.generated.resources.ic_close_24
import mythscape.app.shared.generated.resources.ic_person_24
import mythscape.app.shared.generated.resources.ic_search_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun UserField(
    selectedUser: UserDTO?,
    users: List<UserDTO>,
    onSelect: (UserDTO?) -> Unit,
    label: String = "User",
    errorMessage: String? = null,
    clearable: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                showDialog = true
            }
        }
    }

    Box(
        modifier = modifier
            .pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true),
    ) {
        AppTextField(
            value = selectedUser?.name ?: "",
            onValueChange = {},
            label = label,
            errorMessage = errorMessage,
            readOnly = true,
            leadingIcon = painterResource(Res.drawable.ic_person_24),
            trailing = if (clearable && selectedUser != null) {
                {
                    IconButton(
                        onClick = { onSelect(null) },
                    ) {
                        Icon(
                            painterResource(Res.drawable.ic_close_24),
                            contentDescription = "Clear",
                        )
                    }

                }
            } else null,
            interactionSource = interactionSource,
        )
    }

    if (showDialog) {
        UserSelectorDialog(
            users = users,
            selectedUser = selectedUser,
            onDismiss = { showDialog = false },
            onSelect = {
                onSelect(it)
                showDialog = false
            }
        )
    }
}

@Composable
fun UserSelectorDialog(
    users: List<UserDTO>,
    selectedUser: UserDTO?,
    onDismiss: () -> Unit,
    onSelect: (UserDTO) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredUsers by remember(users, searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                users
            } else {
                users.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.email.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select User") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name or email") },
                    leadingIcon = { Icon(painterResource(Res.drawable.ic_search_24), contentDescription = "Search") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (filteredUsers.isEmpty()) {
                    Text(
                        text = "No users found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                    ) {
                        items(filteredUsers, key = { it.id }) { user ->
                            val isSelected = user.id == selectedUser?.id
                            ListItem(
                                headlineContent = { Text(user.name) },
                                supportingContent = { Text(user.email) },
                                trailingContent = {
                                    if (isSelected) {
                                        Icon(
                                            painter = painterResource(Res.drawable.ic_check_24),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                        )
                                    }
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = if (isSelected) {
                                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelect(user) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text("Cancel")
            }
        },
    )
}