package com.myplaygroup.app.feature_main.domain.use_cases

import com.myplaygroup.app.feature_main.domain.use_cases.validators.UsernameValidator

data class MainValidators (
    val usernameValidator: UsernameValidator = UsernameValidator()
)
