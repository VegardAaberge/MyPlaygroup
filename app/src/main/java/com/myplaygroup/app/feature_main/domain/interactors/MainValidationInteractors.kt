package com.myplaygroup.app.feature_main.domain.interactors

import com.myplaygroup.app.core.domain.validation.daily_class.DateValidator
import com.myplaygroup.app.core.domain.validation.payments.AmountValidator
import com.myplaygroup.app.core.domain.validation.user.UsernameValidator
import javax.inject.Inject

class MainValidationInteractors @Inject constructor(){
    val usernameValidator: UsernameValidator = UsernameValidator()
    val amountValidator: AmountValidator = AmountValidator()
    val dateValidator: DateValidator = DateValidator()
}
