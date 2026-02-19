package com.example.newsfeedsimulator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview

import newsfeedsimulator.composeapp.generated.resources.Res
import newsfeedsimulator.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    val repository = remember { NewsRepository() }
    val viewModel = remember { NewsViewModel(repository) }

    val newsList = remember { mutableStateListOf<NewsArticle>() }

    val readCount by viewModel.readCount.collectAsState()
    val readArticles by viewModel.readArticles.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNews("Tech").collect { article ->
            newsList.add(0, article)
        }
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "News Feed Simulator",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        "Berita Unik Dibaca: $readCount",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(newsList) { article ->
                        val isRead = readArticles.contains(article.id)
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = article.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isRead) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = article.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isRead) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = article.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Button(
                                        onClick = {
                                            viewModel.markAsRead(article.id)
                                        },
                                        enabled = !isRead,
                                        contentPadding = PaddingValues(horizontal = 12.dp)
                                    ) {
                                        Text(if (isRead) "Sudah Dibaca" else "Baca")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
