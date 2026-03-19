package com.example.profileghama

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import profileghama.composeapp.generated.resources.Res
import profileghama.composeapp.generated.resources.my_profile_pic

data class ProfileUiState(
    val name: String = "Muhammad Ghama Al Fajri",
    val subtitle: String = "Teknik Informatika (123140182)",
    val bio: String = "Mahasiswa Teknik Informatika angkatan 2023 di Institut Teknologi Sumatera yang tertarik di bidang Artificial Intelligence dan pengembangan perangkat lunak.",
    val email: String = "muhammad.123140182@student.itera.ac.id",
    val phone: String = "+62 813-XXXX-XXXX",
    val location: String = "Bandar Lampung, Lampung, Indonesia",
    val isEditing: Boolean = false,
    val isDarkMode: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleEdit() {
        _uiState.update { it.copy(isEditing = !it.isEditing) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun updateProfile(name: String, subtitle: String, bio: String) {
        _uiState.update { it.copy(name = name, subtitle = subtitle, bio = bio, isEditing = false) }
    }
}

@Composable
@Preview
fun App(viewModel: ProfileViewModel = viewModel { ProfileViewModel() }) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val customThemeColor = Color(0xFF7B8CB6)
    
    val animatedBgColor by animateColorAsState(
        targetValue = if (uiState.isDarkMode) Color(0xFF121212) else Color.White,
        animationSpec = tween(durationMillis = 500)
    )
    val animatedSurfaceVariant by animateColorAsState(
        targetValue = if (uiState.isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF0F2F8),
        animationSpec = tween(durationMillis = 500)
    )
    val animatedPrimary by animateColorAsState(
        targetValue = customThemeColor,
        animationSpec = tween(durationMillis = 500)
    )
    val animatedOnSurface by animateColorAsState(
        targetValue = if (uiState.isDarkMode) Color.White else Color.Black,
        animationSpec = tween(durationMillis = 500)
    )

    val colorScheme = if (uiState.isDarkMode) {
        darkColorScheme(
            primary = animatedPrimary,
            onPrimary = Color.White,
            surface = animatedBgColor,
            background = animatedBgColor,
            surfaceVariant = animatedSurfaceVariant,
            onSurface = animatedOnSurface
        )
    } else {
        lightColorScheme(
            primary = animatedPrimary,
            onPrimary = Color.White,
            surface = animatedBgColor,
            background = animatedBgColor,
            surfaceVariant = animatedSurfaceVariant,
            onSurface = animatedOnSurface
        )
    }

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .windowInsetsPadding(WindowInsets.safeContent)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isDarkMode) "Dark Mode" else "Light Mode",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        FilledTonalIconToggleButton(
                            checked = uiState.isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (uiState.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Dark Mode"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileHeader(
                        name = uiState.name,
                        subtitle = uiState.subtitle,
                        isDarkMode = uiState.isDarkMode
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (uiState.isEditing) {
                        EditForm(
                            initialName = uiState.name,
                            initialSubtitle = uiState.subtitle,
                            initialBio = uiState.bio,
                            onSave = { n, s, b -> viewModel.updateProfile(n, s, b) },
                            onCancel = { viewModel.toggleEdit() }
                        )
                    } else {
                        ProfileCard(
                            title = "Tentang Saya",
                            description = uiState.bio
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        var showContactInfo by remember { mutableStateOf(false) }
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { showContactInfo = !showContactInfo },
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                            ) {
                                Text(if (showContactInfo) "Sembunyikan Kontak" else "Tampilkan Kontak")
                            }
                            OutlinedButton(onClick = { viewModel.toggleEdit() }) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Edit Profil")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        AnimatedVisibility(visible = showContactInfo) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    InfoItem(icon = Icons.Default.Email, text = uiState.email)
                                    InfoItem(icon = Icons.Default.Phone, text = uiState.phone)
                                    InfoItem(icon = Icons.Default.LocationOn, text = uiState.location)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditForm(
    initialName: String,
    initialSubtitle: String,
    initialBio: String,
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var subtitle by remember { mutableStateOf(initialSubtitle) }
    var bio by remember { mutableStateOf(initialBio) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Edit Profil", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = name, 
                onValueChange = { name = it }, 
                label = { Text("Nama") }, 
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = subtitle, 
                onValueChange = { subtitle = it }, 
                label = { Text("Subtitle") }, 
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = bio, 
                onValueChange = { bio = it }, 
                label = { Text("Bio") }, 
                modifier = Modifier.fillMaxWidth(), 
                minLines = 3
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) { Text("Batal") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onSave(name, subtitle, bio) }) { Text("Simpan") }
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, subtitle: String, isDarkMode: Boolean) {
    val goldColor = Color(0xFFFFD700)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .border(3.dp, goldColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.my_profile_pic),
                contentDescription = "Foto Profil",
                modifier = Modifier.size(140.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(text = subtitle, fontSize = 16.sp, color = if (isDarkMode) Color.LightGray else Color.Gray)
    }
}

@Composable
fun ProfileCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, textAlign = TextAlign.Justify, lineHeight = 22.sp)
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            shape = CircleShape,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
