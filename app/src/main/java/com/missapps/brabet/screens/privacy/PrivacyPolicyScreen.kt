package com.missapps.brabet.screens.privacy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.missapps.brabet.R
import com.missapps.brabet.ui.components.BackButton
import com.missapps.brabet.ui.components.Title
import com.missapps.brabet.ui.darkBlue
import com.missapps.brabet.ui.lightBlue

@Composable
@Suppress("FunctionNaming")
fun PrivacyPolicyScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(darkBlue)
            .padding(24.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            BackButton(modifier = Modifier.align(Alignment.CenterStart), onBack = onBack)
            Title(
                modifier = Modifier
                    .width(160.dp)
                    .align(Alignment.Center),
                text = stringResource(R.string.privacy_policy_title)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(lightBlue, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.privacy_policy_text),
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}
