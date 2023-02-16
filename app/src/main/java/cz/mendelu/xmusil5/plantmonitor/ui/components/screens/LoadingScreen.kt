package com.icontio.senscare_peresonal_mobile.ui.components.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.theme.PlantMonitorTheme

@Composable
fun LoadingScreen(){
    val sizeLogo = 200.dp
    val sizeIndicator = 215.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 10.dp,
            modifier = Modifier
                .size(sizeIndicator)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(id = R.string.appLogo),
            modifier = Modifier
                .size(sizeLogo)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    PlantMonitorTheme {
        LoadingScreen()
    }
}