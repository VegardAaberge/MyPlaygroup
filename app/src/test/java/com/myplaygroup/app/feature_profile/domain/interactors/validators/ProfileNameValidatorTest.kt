package com.myplaygroup.app.feature_profile.domain.interactors.validators

import com.google.common.truth.Truth
import com.myplaygroup.app.core.domain.validation.user.ProfileNameValidator

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