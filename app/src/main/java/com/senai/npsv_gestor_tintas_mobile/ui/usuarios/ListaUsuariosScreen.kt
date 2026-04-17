package com.senai.npsv_gestor_tintas_mobile.ui.usuarios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senai.npsv_gestor_tintas_mobile.data.local.TokenStore
import com.senai.npsv_gestor_tintas_mobile.data.remote.RetrofitCliente
import com.senai.npsv_gestor_tintas_mobile.data.remote.UsuarioResponseDTO
import com.senai.npsv_gestor_tintas_mobile.data.repository.UsuarioRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaUsuariosScreen(
    onNavigateToCadastro: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val tokenStore = remember { TokenStore(context) }
    val apiService = remember { RetrofitCliente.criarServico(tokenStore) }
    val repository = remember { UsuarioRepository(apiService) }
    val viewModel: ListaUsuariosViewModel = viewModel(factory = ListaUsuariosViewModel.Factory(repository))

    val uiState by viewModel.uiState.collectAsState()

    // Carrega os dados sempre que a tela for aberta (útil ao voltar do ecrã de cadastro)
    LaunchedEffect(Unit) {
        viewModel.carregarUsuarios()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestão de Utilizadores") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCadastro,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Utilizador")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is ListaUsuariosUiState.Loading -> {

                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is ListaUsuariosUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as ListaUsuariosUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { viewModel.carregarUsuarios() }) {
                            Text("Tentar Novamente")
                        }
                    }
                }
                is ListaUsuariosUiState.Success -> {
                    val usuarios = (uiState as ListaUsuariosUiState.Success).usuarios

                    if (usuarios.isEmpty()) {

                        Text(
                            text = "Nenhum utilizador encontrado.",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {

                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(usuarios) { usuario ->
                                UsuarioCard(usuario)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UsuarioCard(usuario: UsuarioResponseDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = usuario.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = usuario.email,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Surface(
                color = if (usuario.ativo) MaterialTheme.colorScheme.primary else Color.Gray,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = usuario.role,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}