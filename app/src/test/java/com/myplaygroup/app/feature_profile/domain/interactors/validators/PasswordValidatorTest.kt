package com.myplaygroup.app.feature_profile.domain.interactors.validators

import com.google.common.truth.Truth
import com.myplaygroup.app.core.domain.validation.PasswordValidator
import org.junit.Before
import org.junit.Test

class PasswordValidatorTest {

    lateinit var passwordValidator: PasswordValidator

    @Before
    fun setUp() {
        passwordValidator = PasswordValidator()
    }

    @Test
    fun `too short password return false with error`(){
        val result = passwordValidator("123456")

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isNotEmpty()
    }

    @Test
    fun `password with no letters return false with error`(){
        val result = passwordValidator("12345678")

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isNotEmpty()
    }

    @Test
    fun `password with no numbers return false with error`(){
        val result = passwordValidator("abcdefghi")

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isNotEmpty()
    }

    @Test
    fun `password with no lowercase letter return false with error`(){
        val result = passwordValidator("G33424566H")

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isNotEmpty()
    }

    @Test
    fun `Valid password return true without error`(){
        val result = passwordValidator("g87654321D")

        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorMessage).isNull()
    }
}