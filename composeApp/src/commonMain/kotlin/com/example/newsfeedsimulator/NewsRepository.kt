package com.example.newsfeedsimulator

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class NewsRepository {
    private val allNews = listOf(
        NewsArticle(0, "Teknologi AI Terbaru", "Tech", "AI sekarang bisa menulis kode Android dalam hitungan detik."),
        NewsArticle(0, "Hasil Pertandingan Bola", "Sports", "Pertandingan semalam berakhir dengan skor imbang 2-2."),
        NewsArticle(0, "Update KMP 2026", "Tech", "Kotlin Multiplatform akan mendukung target baru di tahun 2026."),
        NewsArticle(0, "Resep Masakan Baru", "Food", "Cobalah resep nasi goreng rempah yang menggugah selera ini.")
    )

    fun getNewsStream(categoryFilter: String): Flow<NewsArticle> = flow {
        var index = 0
        var uniqueIdCounter = 1 // Counter untuk membuat ID unik setiap kali berita dikirim
        while (true) {
            val news = allNews[index % allNews.size]
            if (news.category == categoryFilter) {
                // Kirim copy berita dengan ID yang unik untuk emisi ini
                emit(news.copy(id = uniqueIdCounter++))
                delay(3000) // Beri jeda sedikit lebih lama agar user sempat membaca
            }
            index++
        }
    }
}
