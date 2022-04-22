package com.caminaapps.bookworm.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.caminaapps.bookworm.domain.model.Book

@Entity(tableName = "book")
data class BookEntity(

    @PrimaryKey
    val id: String,

    val title: String,
    val subtitle: String,
    val author: String,

    @ColumnInfo(name = "published_date")
    val publishedDate: String,

    @ColumnInfo(name = "cover_url")
    val coverUrl: String?,

    @ColumnInfo(name = "finished_reading")
    val finishedReading: Boolean,

    @ColumnInfo(name = "favourite")
    val isFavourite: Boolean,
) {
    companion object {
        fun fromBook(book: Book): BookEntity {
            return BookEntity(
                id = book.id,
                title = book.title,
                subtitle = book.subtitle,
                author = book.author,
                publishedDate = book.publishedDate,
                coverUrl = book.coverUrl,
                finishedReading = book.finishedReading,
                isFavourite = book.isFavourite
            )
        }
    }

    fun toBook(): Book {
        return Book(
            id = id,
            title = title,
            subtitle = subtitle,
            author = author,
            publishedDate = publishedDate,
            coverUrl = coverUrl,
            finishedReading = finishedReading,
            isFavourite = isFavourite
        )
    }
}
