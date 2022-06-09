package com.myplaygroup.app.feature_profile.domain.interactors.validators

import org.junit.Before
import org.junit.Test

import com.google.common.truth.Truth.assertThat;
import com.myplaygroup.app.core.domain.validation.EmailValidator

class EmailValidatorTest {

    private lateinit var emailValidator: EmailValidator

    @Before
    fun setUp() {
        emailValidator = EmailValidator()
    }

    @Test
    fun `empty email return false`(){
        val result = emailValidator("")

        assertThat(result.successful).isFalse()
        assertThat(result.errorMessage).isNotEmpty()
    }

    @Test
    fun `non valid email return false`(){
        val result = emailValidator("example.com")

        assertThat(result.successful).isFalse()
        assertThat(result.errorMessage).isNotEmpty()
    }

    @Test
    fun `valid email return true`(){
        val result = emailValidator("example@test.com")

        assertThat(result.successful).isTrue()
        assertThat(result.errorMessage).isNull()
    }
}