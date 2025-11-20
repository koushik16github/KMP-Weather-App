package com.koushik.kmpweatherapp.presentation.home

import com.koushik.kmpweatherapp.domain.model.WeatherInfo
import com.koushik.kmpweatherapp.domain.usecase.GetWeatherUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `init triggers load and sets Success when usecase returns data`() = testScope.runTest {
        val expected = WeatherInfo(temperature = 25.0, weatherCode = 100, humidity = 60)
        val mockUseCase = mockk<GetWeatherUseCase>()
        coEvery { mockUseCase() } returns expected

        val vm = HomeViewModel(mockUseCase)

        testScheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is HomeUiState.Success)
        assertEquals(expected.temperature, state.data.temperature)
    }

    @Test
    fun `init results in Error when usecase throws`() = testScope.runTest {
        val mockUseCase = mockk<GetWeatherUseCase>()
        coEvery { mockUseCase() } throws RuntimeException("network failed")

        val vm = HomeViewModel(mockUseCase)
        testScheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is HomeUiState.Error)
        assertEquals("network failed", state.message)
    }

    @Test
    fun `loadWeather reentry is prevented when already loading`() = testScope.runTest {
        val mockUseCase = mockk<GetWeatherUseCase>()
        coEvery { mockUseCase() } coAnswers {
            delay(500)
            WeatherInfo(temperature = 11.0, weatherCode = 1, humidity = 10)
        }

        val vm = HomeViewModel(mockUseCase)

        vm.loadWeather()

        testScheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is HomeUiState.Success)
    }

    @Test
    fun `manual loadWeather after error retries and succeeds`() = testScope.runTest {
        val mockUseCase = mockk<GetWeatherUseCase>()
        var call = 0
        coEvery { mockUseCase() } coAnswers {
            call++
            if (call == 1) throw RuntimeException("first fail")
            else WeatherInfo(temperature = 33.0, weatherCode = 10, humidity = 20)
        }

        val vm = HomeViewModel(mockUseCase)
        testScheduler.advanceUntilIdle()

        val state1 = vm.uiState.value
        assertTrue(state1 is HomeUiState.Error)

        vm.loadWeather()
        testScheduler.advanceUntilIdle()

        val state2 = vm.uiState.value
        assertTrue(state2 is HomeUiState.Success)
        assertEquals(33.0, state2.data.temperature)
    }
}