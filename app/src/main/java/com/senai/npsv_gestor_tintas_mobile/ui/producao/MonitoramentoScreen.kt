package com.senai.npsv_gestor_tintas_mobile.ui.producao

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
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
import com.senai.npsv_gestor_tintas_mobile.data.repository.MonitoramentoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonitoramentoScreen(
    producaoId: String, // O ID da produção selecionada
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember {
        MonitoramentoRepository(RetrofitCliente.criarServico(TokenStore(context)))
    }
    val viewModel: MonitoramentoViewModel = viewModel(factory = MonitoramentoViewModel.Factory(repository))
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(producaoId) {
        viewModel.iniciarMonitoramento(producaoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monitoramento da Balança", fontWeight = FontWeight.Bold) },
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is MonitoramentoUiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("A conectar à máquina IoT...", color = Color.Gray)
                    }
                }
                is MonitoramentoUiState.Error -> {
                    val erro = (uiState as MonitoramentoUiState.Error).message
                    ErrorCard(erro) { viewModel.iniciarMonitoramento(producaoId) }
                }
                is MonitoramentoUiState.Success -> {
                    val dados = (uiState as MonitoramentoUiState.Success).pesagem
                    DashboardPesagem(dados.pesoLido, dados.pesoAlvo, dados.resultadoRN01)
                }
            }
        }
    }
}

@Composable
fun DashboardPesagem(pesoLido: Double, pesoAlvo: Double, resultadoRN: String?) {
    // Lógica de cores baseada no status IoT.
    // Supondo que "DENTRO_DA_MARGEM" é verde, e outros indicam alerta (amarelo/vermelho).
    val isMargemSegura = resultadoRN?.contains("DENTRO", ignoreCase = true) == true
    val isAcima = resultadoRN?.contains("ACIMA", ignoreCase = true) == true

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isMargemSegura -> Color(0xFFE8F5E9) // Verde Claro
            isAcima -> Color(0xFFFFEBEE)        // Vermelho Claro
            else -> Color(0xFFFFF3E0)           // Amarelo/Laranja Claro (Abaixo/Aguardando)
        },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isMargemSegura -> Color(0xFF2E7D32)
            isAcima -> Color(0xFFC62828)
            else -> Color(0xFFE65100)
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Progresso da Produção", fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        // Card Principal: PESO LIDO
        Card(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f), // Card quadrado e grande
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Peso Atual na Balança",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor.copy(alpha = 0.8f)
                )
                Text(
                    text = "${pesoLido}g", // Assumindo gramas como unidade comum de IoT de tintas
                    fontSize = 72.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = resultadoRN ?: "AGUARDANDO LEITURA...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Card Secundário: PESO ALVO (DOD NPSV-204)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Peso Alvo a Atingir:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = "${pesoAlvo}g",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ErrorCard(mensagem: String, onRetry: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Warning, contentDescription = "Erro", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(mensagem, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Tentar Novamente", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}