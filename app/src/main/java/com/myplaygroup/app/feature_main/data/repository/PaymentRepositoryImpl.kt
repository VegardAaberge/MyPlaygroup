package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.toPayment
import com.myplaygroup.app.feature_main.data.mapper.toPaymentEntity
import com.myplaygroup.app.feature_main.domain.model.Payment
import com.myplaygroup.app.feature_main.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    private val app: Application
) : PaymentRepository {

    val dao = mainDatabase.mainDao()

    override suspend fun getAllPayments(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Payment>>> {

        return networkBoundResource(
            query = {
                dao.getPayments().map { x -> x.toPayment() }
            },
            fetch = {
                api.getPayments()
            },
            saveFetchResult = { payments ->
                dao.clearSyncedPayments()
                dao.insertPayments(payments)
                dao.getPayments().map { it.toPayment() }
            },
            shouldFetch = {
                fetchFromRemote && checkForInternetConnection(app)
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> "Couldn't reach server: ${r.message}"
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                }
            }
        )
    }

    override suspend fun addPaymentToDatabase(
        payment: Payment
    ): Resource<Payment> {
        return try {
            val paymentEntity = payment.toPaymentEntity()
            dao.insertPayment(paymentEntity)
            Resource.Success(paymentEntity.toPayment())
        } catch (t: Throwable) {
            t.printStackTrace()
            Resource.Error(t.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun uploadPayments(
        payments: List<Payment>
    ): Flow<Resource<List<Payment>>> {

        return fetchNetworkResource(
            fetch = {
                val paymentsToUpload = payments.map { x -> x.clientId }
                val paymentEntities = dao.getPaymentsByClientId(paymentsToUpload)
                api.uploadPayments(paymentEntities)
            },
            processFetch = { paymentEntities ->
                dao.clearAllPayments()
                dao.insertPayments(paymentEntities)
                dao.getPayments().map { it.toPayment() }
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> "Couldn't reach server: ${r.message}"
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                }
            }
        )
    }
}