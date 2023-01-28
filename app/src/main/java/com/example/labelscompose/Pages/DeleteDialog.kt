package com.example.labelscompose.Pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labelscompose.ViewModel.MainViewModel
import com.example.labelscompose.ui.theme.GrayMid
import com.example.labelscompose.ui.theme.RedLight

@Composable
fun DeleteAlertDialog(vm: MainViewModel) {
    AlertDialog(
        onDismissRequest = { vm.deleteDialogAlert.value = !vm.deleteDialogAlert.value },
        title = { Text(text = "Удалить метку?") },
        backgroundColor = Color.Gray,
        modifier = Modifier.clip(RoundedCornerShape(10.dp)),
        buttons = {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        vm.delLabel()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = RedLight),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "Удалить", fontSize = 20.sp)
                }
                Button(
                    onClick = { vm.deleteDialogAlert.value = !vm.deleteDialogAlert.value },
                    colors = ButtonDefaults.buttonColors(backgroundColor = GrayMid),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.padding(5.dp)

                ) {
                    Text(text = "Отмена", fontSize = 20.sp)
                }
            }
        })
}
