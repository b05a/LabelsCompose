package com.example.labels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labels.Clases.Label
import com.example.labels.Pages.DeleteAlertDialog
import com.example.labels.Pages.LabelItem
import com.example.labels.Pages.OptionsItem
import com.example.labels.ViewModel.MainViewModel
import com.example.labels.ui.theme.GrayDark
import com.example.labels.ui.theme.GrayLight
import com.example.labels.ui.theme.GrayMid
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    val vm: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // состояние drawer
            val state = rememberScaffoldState()
            // корутина
            val scope = rememberCoroutineScope()
            // получаем dpi экрана
            vm.displaySize(this)

            Scaffold(scaffoldState = state,
                modifier = Modifier.fillMaxSize(),
                drawerBackgroundColor = Color.Gray,
                drawerContent = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        // Кнопка добавления новой метки
                        Button(
                            onClick = {
                                vm.addLabel()
                                scope.launch { state.drawerState.close() }
                            }, colors = ButtonDefaults.buttonColors(
                                backgroundColor = GrayDark, contentColor = Color.White
                            ), shape = RoundedCornerShape(15.dp), modifier = Modifier.padding(5.dp)
                        ) {
                            Text(text = "Добавить метку", fontSize = 20.sp)
                        }


                        // текст "Вертикальный экран"
                        Card(
                            backgroundColor = GrayMid,
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(RoundedCornerShape(15.dp))
                        ) {
                            Text(
                                text = "Вертикальный экран:",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        }


                        // если вертикальных меток нет, то паказываем текст "Нет меток"
                        if (vm.verticalIsEmpty()) {
                            Card(
                                backgroundColor = GrayLight,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(15.dp))
                            ) {
                                Text(
                                    text = "Нет меток",
                                    fontSize = 20.sp,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        } else {


                            // кноки вертикальных меток
                            for (i in vm.listLabels) {
                                if (!i.verticalVal) continue
                                Button(
                                    onClick = {
                                        if(!vm.editLabelVertical(i)) return@Button

                                        println(vm.changeListNumber)
                                        scope.launch { state.drawerState.close() }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = GrayDark, contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(15.dp),
                                    modifier = Modifier.padding(5.dp)
                                ) {
                                    Text(text = i.name, fontSize = 20.sp)
                                }
                            }
                        }


                        // текст "Горизонтальный экран"
                        Card(
                            backgroundColor = GrayMid,
                            modifier = Modifier.clip(RoundedCornerShape(15.dp))
                        ) {
                            Text(
                                text = "Горизонтальный экран:",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        }


                        // если горизонтальных меток нет, но показываем текст "Нет меток"
                        if (vm.horizontalIsEmpty()) {
                            Card(
                                backgroundColor = GrayLight,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(15.dp))
                            ) {
                                Text(
                                    text = "Нет меток",
                                    fontSize = 20.sp,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        } else {


                            // кнопки горизонтальных меток
                            for (i in vm.listLabels) {
                                if (i.verticalVal) continue
                                Button(
                                    onClick = {
                                        if(!vm.editLabelHorizontal(i)) return@Button
                                        scope.launch {
                                            state.drawerState.close()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = GrayDark, contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(15.dp),
                                    modifier = Modifier.padding(5.dp)
                                ) {
                                    Text(text = i.name, fontSize = 20.sp)
                                }
                            }
                        }

                    }

                },


                content = {

                    Box(modifier = Modifier.fillMaxSize()) {



                        // кнопка открытия drawer
                        IconButton(
                            onClick = {
                                scope.launch {
                                    state.drawerState.open()
                                }
                            }, modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_menu),
                                contentDescription = "image"
                            )
                        }



                        // устанавливаем метки
                        for (i in vm.listLabels) {
                            if (!i.verticalVal && vm.getDisplaySize().displayVertical) continue
                            if (i.verticalVal && !vm.getDisplaySize().displayVertical) continue
                            LabelItem(
                                label = i,
                                displayXY = vm.getDisplaySize(),
                                vm.colorLabel.value.color
                            )
                        }



                        // окно настроек метки
                        if (vm.optionView.value) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomStart)
                            ) {
                                OptionsItem(
                                    this@MainActivity,
                                    vm
                                )
                            }
                        }



                        // диалоговое окно удаления метки
                        if (vm.deleteDialogAlert.value) {
                            DeleteAlertDialog(vm = vm)
                        }



                        // кнопка изменения цвета меток
                        Box(modifier = Modifier.align(Alignment.TopEnd)) {
                            IconButton(onClick = { vm.changeColorLabel() }) {
                                Text(text = "Цвет", color = Color.White)
                            }
                        }



                    }
                })
        }
    }
}