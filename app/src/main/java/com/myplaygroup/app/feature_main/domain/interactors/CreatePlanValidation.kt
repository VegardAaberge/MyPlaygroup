package com.myplaygroup.app.feature_main.domain.interactors

import com.myplaygroup.app.core.domain.validation.monthly_plans.*
import com.myplaygroup.app.core.domain.validation.user.UsernameValidator
import javax.inject.Inject

class CreatePlanValidation @Inject constructor(){
    val usernameValidator: UsernameValidator = UsernameValidator()
    val kidNameValidator: KidNameValidator = KidNameValidator()
    val startDateValidator: StartDateValidator = StartDateValidator()
    val endDateValidator: EndDateValidator = EndDateValidator()
    val planNameValidator: PlanNameValidator = PlanNameValidator()
    val dayOfWeekValidator: DayOfWeekValidator = DayOfWeekValidator()
    val planPriceValidator: PlanPriceValidator = PlanPriceValidator()
}