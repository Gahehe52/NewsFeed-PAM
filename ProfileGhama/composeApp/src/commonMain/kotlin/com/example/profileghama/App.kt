package com.example.profileghama

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
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
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import profileghama.composeapp.generated.resources.Res
import profileghama.composeapp.generated.resources.my_profile_pic

@Composable
@Preview
fun App() {
    val customThemeColor = Color(0xFF7B8CB6)
    val customColorScheme = lightColorScheme(
        primary = customThemeColor,
        onPrimary = Color.White,
        primaryContainer = customThemeColor.copy(alpha = 0.12f),
        onPrimaryContainer = customThemeColor,
        secondary = customThemeColor,
        surfaceVariant = Color(0xFFF0F2F8)
    )

    MaterialTheme(colorScheme = customColorScheme) {
        var showContactInfo by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeContent)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ProfileHeader(
                    name = "Muhammad Ghama Al Fajri",
                    subtitle = "Teknik Informatika (123140182)"
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileCard(
                    title = "Tentang Saya",
                    description = "Mahasiswa Teknik Informatika angkatan 2023 di Institut Teknologi Sumatera yang tertarik di bidang Artificial Intelligence dan pengembangan perangkat lunak."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showContactInfo = !showContactInfo },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (showContactInfo) "Sembunyikan Kontak" else "Tampilkan Kontak")
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(visible = showContactInfo) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoItem(icon = Icons.Default.Email, text = "muhammad.123140182@student.itera.ac.id")
                            InfoItem(icon = Icons.Default.Phone, text = "+62 813-XXXX-XXXX")
                            InfoItem(icon = Icons.Default.LocationOn, text = "Bandar Lampung, Lampung, Indonesia")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, subtitle: String) {
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
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = name, 
            fontSize = 24.sp, 
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = subtitle, 
            fontSize = 16.sp, 
            color = Color.Gray
        )
    }
}

@Composable
fun ProfileCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title, 
                fontWeight = FontWeight.Bold, 
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, textAlign = TextAlign.Justify, lineHeight = 22.sp)
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            shape = CircleShape,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
