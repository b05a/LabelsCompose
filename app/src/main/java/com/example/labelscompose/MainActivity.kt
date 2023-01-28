package com.example.labelscompose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.labelscompose.Pages.DeleteAlertDialog
import com.example.labelscompose.Pages.LabelItem
import com.example.labelscompose.Pages.OptionsItem
import com.example.labelscompose.ViewModel.MainViewModel
import com.example.labelscompose.ui.theme.GrayDark
import com.example.labelscompose.ui.theme.GrayLight
import com.example.labelscompose.ui.theme.GrayMid
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService

    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            shouldShowCamera.value = true
            Log.d("hello", "true")
        } else {
            Log.d("hello", "false")
        }
    }

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
                        if (vm.listLabelsCreate.value) {
                            Log.d("hello", "${vm.listLabelsCreate.value} hello")
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
                                            if (!vm.editLabelVertical(i)) return@Button

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

                        if (vm.listLabelsCreate.value) {
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
                                            if (!vm.editLabelHorizontal(i)) return@Button
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

                    }

                },


                content = {
                    SideEffect {
                        println("hello")
                    }
                    val lensFacing = CameraSelector.LENS_FACING_BACK
                    val context = LocalContext.current
                    val lifecycleOwner = LocalLifecycleOwner.current

                    val preview = Preview.Builder().build()
                    val previewView = remember { PreviewView(context) }
                    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()

                    // 2
                    LaunchedEffect(lensFacing) {
                        val cameraProvider = context.getCameraProvider()
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )

                        preview.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    Box(modifier = Modifier.fillMaxSize()) {

                        if (shouldShowCamera.value) {
                            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
                        }

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
        requestCameraPermission()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("hello", "Permission previously granted")
                shouldShowCamera.value = true
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                Log.d("hello", "Show camere permissions dialog")
            }
            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}