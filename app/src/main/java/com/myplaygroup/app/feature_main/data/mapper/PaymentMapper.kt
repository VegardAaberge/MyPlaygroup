package com.myplaygroup.app.feature_main.data.mapper

import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.feature_main.data.model.PaymentEntity
import com.myplaygroup.app.feature_main.domain.model.Payment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun PaymentEntity.toPayment() : Payment {

    val dateFormat = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT, Locale("en"))
    val parsedDate = LocalDate.parse(date, dateFormat) ?: LocalDate.now()

    return Payment(
        id = id,
        clientId = clientId,
        date = parsedDate,
        username = username,
        amount = amount,
        cancelled = cancelled,
        modified = modified
    )
}

fun Payment.toPaymentEntity() : PaymentEntity {

    val dateFormat = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT, Locale("en"))

    return PaymentEntity(
        id = id,
        clientId = clientId,
        date = date.format(dateFormat),
        username = username,
        amount = amount,
        cancelled = cancelled,
        modified = modified
    )
}