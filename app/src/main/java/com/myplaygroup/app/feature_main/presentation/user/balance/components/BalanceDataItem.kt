package com.myplaygroup.app.feature_main.presentation.user.balance.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.feature_main.presentation.user.balance.BalanceDataItem
import kotlin.math.absoluteValue

@Composable
fun BalanceDataItem(
    balanceItem: BalanceDataItem,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = balanceItem.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "¥" +  balanceItem.balance.absoluteValue.toString(),
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = balanceItem.detail,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.weight(1f)
                )

                if(balanceItem.balance < 0){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colors.error,
                        contentDescription = null
                    )
                }else{
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        tint = Color.Green,
                        contentDescription = null
                    )
                }
            }
        }
    }
}