package es.upm.ging.sobreteleco

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import es.upm.ging.sobreteleco.ui.theme.SobreTelecoTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val tag: String? = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SobreTelecoTheme {
                sobreTelecoUI(tag= this.tag)
            }
        }
    }
}

@Composable
fun sobreTelecoUI(modifier: Modifier = Modifier, tag: String?) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var changed by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column ( modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            // Imagen que cambia al tocarse
            Image(
                painter = painterResource(
                    if (changed) R.drawable.etsit_a else R.drawable.etsit_b
                ),
                contentDescription = "Imagen",
                modifier = Modifier
                    //.size(150.dp)
                    .fillMaxWidth()
                    .clickable { changed = !changed
                                 Log.i(tag, "Pulsamos sobre la imagen")

                    },
                contentScale = ContentScale.Crop // mantener relación de aspecto
            )

            Spacer(Modifier.height(32.dp))

            // Botón que muestra un Snackbar con acción "Deshacer"
            Button(
                onClick = {
                    changed = !changed

                    Log.i(tag, "Cambiamos imagen" + if (changed) " a original" else " a alternativa" )

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Imagen cambiada",
                            actionLabel = "Deshacer",
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )

                        // Si el usuario pulsa "Deshacer"
                        if (result == SnackbarResult.ActionPerformed) {
                            changed = !changed
                        }
                    }
                }
            ) {
                Text("Cambiar imagen")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SobreTelecoTheme {
        sobreTelecoUI(tag= "SobreTeleco: SB")
    }
}