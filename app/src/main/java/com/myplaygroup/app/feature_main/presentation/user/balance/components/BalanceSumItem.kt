package com.myplaygroup.app.feature_main.presentation.user.balance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R
import kotlin.math.absoluteValue

@Composable
fun BalanceSumItem(
    balance: Long,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if(balance >= 0){
                stringResource(R.string.balance_credit)
            } else stringResource(R.string.balance_to_pay),
            fontSize = 20.sp,
            color = MaterialTheme.colors.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            text = "¥" + balance.absoluteValue.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = if(balance >= 0) {
                MaterialTheme.colors.onBackground
            } else MaterialTheme.colors.error
        )
    }
}