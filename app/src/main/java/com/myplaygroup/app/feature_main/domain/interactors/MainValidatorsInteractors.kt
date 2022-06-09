package com.myplaygroup.app.feature_main.domain.interactors

import com.myplaygroup.app.feature_main.domain.interactors.validators.UsernameValidator
import javax.inject.Inject

class MainValidatorsInteractors @Inject constructor(){
    val usernameValidator: UsernameValidator = UsernameValidator()
}
