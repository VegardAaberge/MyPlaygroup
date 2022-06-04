package com.myplaygroup.app.feature_profile.domain.use_cases.validators

import com.google.common.truth.Truth
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ProfileNameValidatorTest {

    lateinit var profileNameValidator: ProfileNameValidator;

    @Before
    fun setUp() {
        profileNameValidator = ProfileNameValidator()
    }

    @Test
    fun `profile name is not empty`(){
        val result = profileNameValidator("")

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isNotEmpty()
    }
}