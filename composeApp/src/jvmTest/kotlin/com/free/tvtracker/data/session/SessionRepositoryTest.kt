package com.free.tvtracker.data.session

import io.mockk.mockk
import kotlin.test.Test

class SessionRepositoryTest {
    @Test
    fun `GIVEN create anon session THEN session is kept in reference`() {
        val sut = SessionRepository(mockk())
    }
}
