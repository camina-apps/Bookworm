package com.caminaapps.bookworm.features.searchBookOnline.domain

import com.caminaapps.bookworm.core.data.repository.OnlineSearchBookRepository
import com.caminaapps.bookworm.core.model.Book
import javax.inject.Inject

class SearchBookByTitleUseCase @Inject constructor(
    private val onlineSearchBookRepository: OnlineSearchBookRepository,
) {
    suspend operator fun invoke(title: String): List<Book> {
        return if (title.isBlank()) {
            emptyList()
        } else {
            onlineSearchBookRepository.getBooksByTitle(title)
        }
    }
}
