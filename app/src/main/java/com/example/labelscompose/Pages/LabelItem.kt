package com.example.labelscompose.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labelscompose.Cases.DisplayXY
import com.example.labelscompose.Clases.Label
import com.example.labelscompose.R
import com.example.labelscompose.ui.theme.BlackLight
import com.example.labelscompose.ui.theme.GreenWhite

@Composable
fun LabelItem(label: Label, displayXY: DisplayXY, color: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.offset(x = label.horizontal.dp, y = label.vertical.dp)
    ) {


        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(10.dp), enabled = false) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(
                        id =
                        when (color) {
                            "green" -> R.drawable.ic_label_green
                            "white" -> R.drawable.ic_label_white
                            "grey" -> R.drawable.ic_label_black
                            else -> R.drawable.ic_label_white
                        }
                    ),
                    contentDescription = "image"
                )
                if (displayXY.x * 0.5 < label.horizontal) {
                    Text(
                        text = label.name,
                        fontSize = 14.sp,
                        color =
                        when (color) {
                            "green" -> GreenWhite
                            "white" -> Color.White
                            "grey" -> BlackLight
                            else -> GreenWhite
                        },
                        textAlign = TextAlign.Center

                    )
                }
            }

        }
        if (displayXY.x * 0.5 > label.horizontal) {
            Text(
                text = label.name,
                fontSize = 18.sp,
                color =
                when (color) {
                    "green" -> GreenWhite
                    "white" -> Color.White
                    "grey" -> BlackLight
                    else -> GreenWhite
                },
                textAlign = TextAlign.Center
            )
        }
    }
}