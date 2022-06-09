package com.myplaygroup.app.feature_profile.domain.interactors.validators

import com.google.common.truth.Truth
import com.myplaygroup.app.core.domain.validation.PhoneNumberValidator

import org.junit.Before
import org.junit.Test

class PhoneNumberValidatorTest {

    lateinit var phoneNumberValidator: PhoneNumberValidator

    @Before
    fun setUp() {
        phoneNumberValidator = PhoneNumberValidator()
    }

    @Test
    fun `phone number is not empty`(){
        val result = phoneNumberValidator("")

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isNotEmpty()
    }
}