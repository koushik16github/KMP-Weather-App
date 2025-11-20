package com.koushik.kmpweatherapp.presentation.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

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
    fun `onUsernameChanged updates username in state`() = testScope.runTest {
        val vm = LoginViewModel()

        vm.onUsernameChanged("koushik")

        assertEquals("koushik", vm.uiState.value.username)
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun `onPasswordChanged updates password in state`() = testScope.runTest {
        val vm = LoginViewModel()

        vm.onPasswordChanged("secret")

        assertEquals("secret", vm.uiState.value.password)
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun `login with empty credentials sets error message`() = testScope.runTest {
        val vm = LoginViewModel()

        assertTrue(vm.uiState.value.username.isEmpty())
        assertTrue(vm.uiState.value.password.isEmpty())

        vm.login()

        assertEquals("Enter username & password", vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isLoading)
        assertFalse(vm.uiState.value.loginSuccess)
    }

    @Test
    fun `login success sets loginSuccess after delay`() = testScope.runTest {
        val vm = LoginViewModel()
        vm.onUsernameChanged("u")
        vm.onPasswordChanged("p")

        vm.login()

        testScheduler.runCurrent()
        assertTrue(vm.uiState.value.isLoading, "Expected isLoading to be true after starting login")

        testScheduler.advanceTimeBy(1500L)

        testScheduler.runCurrent()

        assertFalse(vm.uiState.value.isLoading)
        assertTrue(vm.uiState.value.loginSuccess, "Expected loginSuccess true after delay")
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun `consumeLoginSuccess clears loginSuccess flag`() = testScope.runTest {
        val vm = LoginViewModel()
        vm.onUsernameChanged("u")
        vm.onPasswordChanged("p")

        vm.login()
        testScheduler.runCurrent()
        testScheduler.advanceTimeBy(1500L)
        testScheduler.runCurrent()

        assertTrue(vm.uiState.value.loginSuccess)

        vm.consumeLoginSuccess()
        assertFalse(vm.uiState.value.loginSuccess)
    }

    @Test
    fun `calling login while already loading is ignored`() = testScope.runTest {
        val vm = LoginViewModel()
        vm.onUsernameChanged("u")
        vm.onPasswordChanged("p")

        vm.login()
        testScheduler.runCurrent()
        assertTrue(vm.uiState.value.isLoading, "Expected isLoading true after starting login")

        vm.login()

        testScheduler.advanceTimeBy(1500L)
        testScheduler.runCurrent()

        assertTrue(vm.uiState.value.loginSuccess)
    }
}