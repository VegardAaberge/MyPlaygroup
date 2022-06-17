package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {

    suspend fun getAllPayments(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<Payment>>>

    suspend fun addPaymentToDatabase(
        payment: Payment
    ) : Resource<Payment>

    suspend fun uploadPayments(
        payments: List<Payment>
    ): Flow<Resource<List<Payment>>>
}