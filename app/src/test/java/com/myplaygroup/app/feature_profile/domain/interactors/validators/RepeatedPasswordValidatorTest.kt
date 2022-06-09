package com.myplaygroup.app.feature_profile.domain.interactors.validators

import com.google.common.truth.Truth
import com.myplaygroup.app.core.domain.validation.RepeatedPasswordValidator

import org.junit.Before
import org.junit.Test

class RepeatedPasswordValidatorTest {

    lateinit var repeatedPasswordValidator: RepeatedPasswordValidator

    @Before
    fun setUp() {
        repeatedPasswordValidator = RepeatedPasswordValidator()
    }

    @Test
    fun `password was repeated incorrectly`(){
        val result = repeatedPasswordValidator("B11111111b", "C11111111C")

        Truth.assertThat(result.successful)
            .isFalse()
        Truth.assertThat(result.errorMessage)
            .isNotEmpty()
    }

    @Test
    fun `password was repeated correctly`(){
        val result = repeatedPasswordValidator("B11111111B", "B11111111B")

        Truth.assertThat(result.successful)
            .isTrue()
        Truth.assertThat(result.errorMessage)
            .isNull()
    }
}