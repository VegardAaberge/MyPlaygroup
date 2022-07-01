package com.myplaygroup.app.feature_main.data.repository

import android.content.Context
import com.myplaygroup.app.R
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    @ApplicationContext private val context: Context
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
                fetchFromRemote && checkForInternetConnection(context)
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> context.getString(R.string.error_could_not_reach_server, r.message)
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> context.getString(R.string.error_no_internet_connection)
                    else -> {
                        t.localizedMessage?.let {
                            context.getString(R.string.error_server_exception, t.localizedMessage)
                        } ?: context.getString(R.string.error_unknown_error)
                    }
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
            Resource.Error(t.localizedMessage ?: context.getString(R.string.error_unknown_error))
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
                    else -> context.getString(R.string.error_could_not_reach_server, r.message)
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> context.getString(R.string.error_no_internet_connection)
                    else -> {
                        t.localizedMessage?.let {
                            context.getString(R.string.error_server_exception, t.localizedMessage)
                        } ?: context.getString(R.string.error_unknown_error)
                    }
                }
            }
        )
    }
}