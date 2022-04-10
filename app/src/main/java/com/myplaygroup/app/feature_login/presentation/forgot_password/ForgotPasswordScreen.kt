package com.myplaygroup.app.feature_login.presentation.forgot_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R
import com.myplaygroup.app.feature_login.presentation.forgot_password.components.EmailField
import com.myplaygroup.app.ui.theme.MyPlaygroupTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ForgotPasswoordScreen() {
    val email by remember{
        mutableStateOf("")
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = stringResource(id = R.string.send_email))
                },
                onClick = {

                },
                modifier = Modifier,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Add note"
                    )
                },
                shape = CircleShape,
                contentColor = Color.White,
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) {
        val widthModifier = Modifier
            .width(400.dp)
            .padding(horizontal = 30.dp)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFAAFFFF),
                            Color(0xFFEEFFFF),
                        )
                    )
                ),
        ) {
            Image(
                painter = painterResource(id = R.drawable.lock_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                fontSize = 30.sp,
                text = stringResource(id = R.string.forgot_password_header),
                modifier = widthModifier,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.forgot_password_explanation),
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(20.dp))

            EmailField(
                textValue = email,
                onTextChanged = {
                    
                },
                modifier = widthModifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyPlaygroupTheme {
        ForgotPasswoordScreen()
    }
}