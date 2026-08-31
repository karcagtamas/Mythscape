package eu.karcags.mythscape.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.mythscape.ui.components.common.AppButton
import eu.karcags.mythscape.ui.components.common.LoadingBox
import eu.karcags.mythscape.ui.components.common.PrimaryCard
import eu.karcags.mythscape.ui.components.common.SecondaryCard
import eu.karcags.mythscape.viewmodel.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(18.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        LoadingBox(
            isLoading = viewModel.isLoading,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                PrimaryCard(
                    title = "Chronicler Profile"
                ) {
                    ProfileMetaRow(label = "Full Name", value = viewModel.fullname)
                    ProfileMetaRow(label = "Username", value = viewModel.username)
                    ProfileMetaRow(label = "E-mail Address", value = viewModel.email)
                    ProfileMetaRow(label = "Joined", value = viewModel.registration)

                    viewModel.globalError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 4.dp),
                        )
                    }
                }

                SecondaryCard(
                    title = "Controls"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            AppButton(
                                text = "Edit Profile",
                                onClick = {},
                                enabled = false,
                            )
                        }

                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            AppButton(
                                text = "Change E-mail",
                                onClick = {},
                                enabled = false,
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            AppButton(
                                text = "Change Password",
                                onClick = {},
                                enabled = false,
                            )
                        }

                        Box(
                            modifier = Modifier.weight(1f),
                        ) {
                            AppButton(
                                text = "Delete Account",
                                onClick = {},
                                enabled = false,
                                color = MaterialTheme.colorScheme.error,
                                textColor = MaterialTheme.colorScheme.onError,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileMetaRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label.uppercase(),
            color = Color.Gray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}