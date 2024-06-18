package com.free.tvtracker.ui.settings

import com.free.tvtracker.ui.settings.signup.SignupViewModel
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SignupViewModelTest {

    @Test
    fun isEmailValid() {
        val sut = SignupViewModel(mockk(), mockk())
        assertTrue(sut.looksLikeEmail("asd@a"))
        assertTrue(sut.looksLikeEmail("asd@asd.com"))
        assertFalse(sut.looksLikeEmail("asd@"))
        assertFalse(sut.looksLikeEmail("@a"))
        assertFalse(sut.looksLikeEmail("@"))
    }
}
