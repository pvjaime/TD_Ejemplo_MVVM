package perez.jaime.tdejemplomvvm.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import perez.jaime.tdejemplomvvm.R
import perez.jaime.tdejemplomvvm.model.ApiService
import perez.jaime.tdejemplomvvm.viewmodel.UserViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //Aca se configura el viewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.loadUserList()

        //Se crea el adaptador con data vacia
        userAdapter = UserAdapter(emptyList()) // Inicialmente, la lista está vacía

        //Se configura el recyclerView
        recyclerView = findViewById(R.id.listUser)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        //Se configura el observador que va a estar observando al sujeto "userList"
        userViewModel.userList.observe(
            this
        ) { listUsers ->
            //Recibi datos!!! actualizo
            userAdapter = UserAdapter(listUsers)
            recyclerView.adapter = userAdapter
        }

        //Otro sapo mas
        userViewModel.userList.observe(
            this
        ) { listUsers ->
            //Toast.makeText(this, "Tengo ${listUsers.size} usuarios", Toast.LENGTH_LONG).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //ejecucionHilo()

        ejemploCorrutinas()

    }

    private fun ejemploCorrutinas() {

        //Es la solicitud de la llamada al servicio
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Iniciar una coroutine en el hilo principal
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Realizar una solicitud HTTP en segundo plano
                val user = withContext(Dispatchers.IO) {
                    apiService.getUser("GenesisAhumada")
                }

                // Actualizar la interfaz de usuario con el resultado
                Toast.makeText(
                    this@MainActivity,
                    "Nombre de usuario: ${user.login}\nNombre: ${user.name}",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                // Manejar errores, como problemas de red
                Toast.makeText(this@MainActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    private fun ejecucionHilo() {
        val url = "https://www.emol.com" // Reemplaza con la URL que desees
        //Creamos un cliente que se va encargar de hacer la conexion al sitio web
        val client = OkHttpClient()
        //Es la solicitud de la llamada al sitio
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar errores de conexión
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    // Procesar los datos de la respuesta (responseData)
                    runOnUiThread {
                        // Actualizar la interfaz de usuario con los datos
                        Toast.makeText(this@MainActivity, "Carge la pagina", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    // Manejar errores en la respuesta
                }
            }
        })

    }
}