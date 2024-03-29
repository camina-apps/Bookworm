package com.caminaapps.bookworm.features.searchBookOnline.presentation.searchTitle

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isInstanceOf
import com.caminaapps.bookworm.core.model.Book
import com.caminaapps.bookworm.core.model.BookshelfSortOrder
import com.caminaapps.bookworm.features.searchBookOnline.domain.SaveBookFromOnlineSearchUseCase
import com.caminaapps.bookworm.features.searchBookOnline.domain.SearchBookByTitleUseCase
import com.caminaapps.bookworm.testing.FakeBookRepository
import com.caminaapps.bookworm.testing.FakeOnlineSearchBookRepository
import com.caminaapps.bookworm.testing.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchForBookTitleViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var fakeSearchRepository: FakeOnlineSearchBookRepository
    private lateinit var fakeBookRepository: FakeBookRepository
    private lateinit var searchBookByTitleUseCase: SearchBookByTitleUseCase
    private lateinit var saveBookUseCase: SaveBookFromOnlineSearchUseCase
    private lateinit var viewModel: SearchForBookTitleViewModel

    @Before
    fun setUp() {
        fakeSearchRepository = FakeOnlineSearchBookRepository()
        fakeBookRepository = FakeBookRepository()
        searchBookByTitleUseCase = SearchBookByTitleUseCase(fakeSearchRepository)
        saveBookUseCase = SaveBookFromOnlineSearchUseCase(fakeBookRepository)

        viewModel = SearchForBookTitleViewModel(
            searchBookByTitleUseCase,
            saveBookUseCase
        )
    }

    @Test
    fun uiState_whenInitialized_thenEmpty() = runTest {
        assertThat(viewModel.uiState.value)
            .isInstanceOf(SearchForBookTitleUiState.Empty::class)
    }

    @Test
    fun uiState_whenSearchStart_thenLoading() = runTest {
        viewModel.uiState.test {
            skipItems(1) // empty
            viewModel.search("test")
            assertThat(awaitItem()).isInstanceOf(SearchForBookTitleUiState.Loading::class)
            skipItems(1) // success
        }
    }

    @Test
    fun uiState_whenSearchFinishedWithItems_thenResult() = runTest {
        viewModel.uiState.test {
            skipItems(1) // empty
            viewModel.search("test")
            skipItems(1) // loading
            assertThat(awaitItem()).isInstanceOf(SearchForBookTitleUiState.Success::class)
        }
    }

    @Test
    fun uiState_whenQueryChanged_thenFromResultBackToEmpty() = runTest {
        viewModel.uiState.test {
            skipItems(1) // empty
            viewModel.search("test")
            skipItems(2) // loading, success
            viewModel.onQueryChanged()
            assertThat(awaitItem()).isInstanceOf(SearchForBookTitleUiState.Empty::class)
        }
    }

    @Test
    fun uiState_whenQueryEmpty_thenEmpty() = runTest {
        viewModel.uiState.test {
            skipItems(1) // empty
            viewModel.search("test")
            skipItems(2) // loading, success
            viewModel.search("")
            assertThat(awaitItem()).isInstanceOf(SearchForBookTitleUiState.Empty::class)
        }
    }

    @Test
    fun uiState_whenSearchFinishedWithoutItems_thenNoResult() = runTest {
        fakeSearchRepository.shouldReturnResult = false
        viewModel.uiState.test {
            skipItems(1) // empty
            viewModel.search("test")
            skipItems(1) // loading
            assertThat(awaitItem()).isInstanceOf(SearchForBookTitleUiState.NoResults::class)
        }
    }

    @Test
    fun uiState_whenSearchThrowsIOException_thenError() = runTest {
        fakeSearchRepository.throwException = IOException()
        viewModel.uiState.test {
            skipItems(1) // empty
            viewModel.search("test")
            skipItems(1) // loading
            val item = awaitItem()
            assertThat(item).isInstanceOf(SearchForBookTitleUiState.Error::class)
        }
    }

    @Test
    fun uiState_whenSearchThrowsHttpException_thenError() = runTest {
        fakeSearchRepository.throwException = HttpException(Response.success(null))
        viewModel.uiState.test {
            skipItems(1) // empty
            viewModel.search("test")
            skipItems(1) // loading
            val item = awaitItem()
            assertThat(item).isInstanceOf(SearchForBookTitleUiState.Error::class)
        }
    }

    @Test
    fun saveBook_whenAddBook_thenStoredInBookRepo() = runTest {
        assertThat(
            fakeBookRepository.getAllBooksStream(BookshelfSortOrder.TITLE_ASC).first()
        ).isEmpty()
        viewModel.onAddBook(testBook)
        advanceUntilIdle()
        assertThat(
            fakeBookRepository.getAllBooksStream(BookshelfSortOrder.TITLE_ASC).first()
        ).hasSize(1)
    }
}

private val testBook =
    Book(
        id = "123",
        title = "Android and more",
        author = "Zoro",
        publishedDate = "2023",
        subtitle = "",
        coverUrl = null
    )
