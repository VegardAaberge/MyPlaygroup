package com.myplaygroup.app.feature_main.presentation.user.balance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.user.balance.components.BalanceDataItem
import com.myplaygroup.app.feature_main.presentation.user.balance.components.BalanceHeader
import com.myplaygroup.app.feature_main.presentation.user.balance.components.BalanceSumItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun BalanceScreen(
    viewModel: BalanceViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val scaffoldState = collectEventFlow(viewModel = viewModel)
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            DefaultTopAppBar(
                title = "Balance",
                navigationIcon = {
                    AppBarBackButton(navigator)
                },
            )
        },
    ) {
        BalanceDataLazyColumn(
            totalBalance = state.totalBalance,
            balanceData = state.balanceData,
        )
    }
}

@Composable
fun BalanceDataLazyColumn(
    totalBalance: Long,
    balanceData: Map<String, List<BalanceDataItem>>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        balanceData.forEach { balanceGroup ->
            val balanceItemsInMonth = balanceGroup.value

            item {
                BalanceHeader(
                    title = balanceGroup.key
                )
            }

            items(balanceItemsInMonth.size){ i ->
                val balanceItem = balanceItemsInMonth[i]
                BalanceDataItem(
                    balanceItem = balanceItem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                if(i < balanceItemsInMonth.size - 1){
                    Divider(modifier = Modifier.padding(
                        horizontal = 16.dp
                    ))
                }
            }
        }

        item {
            BalanceHeader(
                title = "SUM",
            )
        }

        item {
            BalanceSumItem(
                balance = totalBalance,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            Divider(modifier = Modifier.padding(
                horizontal = 16.dp
            ))
        }
    }
}