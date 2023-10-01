@file:Suppress("FunctionNaming")

package com.bbbrbetss.comebbrabettt.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bbbrbetss.comebbrabettt.app.ui.lightBlue

@Composable
fun Title(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .background(lightBlue, CircleShape)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Composable
@Preview
fun TitlePreview() {
    Title(modifier = Modifier.width(160.dp), text = "Select Level")
}
