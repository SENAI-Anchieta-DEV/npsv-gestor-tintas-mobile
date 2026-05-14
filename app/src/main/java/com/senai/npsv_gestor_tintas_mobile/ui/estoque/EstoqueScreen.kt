package com.senai.npsv_gestor_tintas_mobile.ui.estoque

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senai.npsv_gestor_tintas_mobile.R
import com.senai.npsv_gestor_tintas_mobile.data.local.TokenStore
import com.senai.npsv_gestor_tintas_mobile.data.remote.RetrofitCliente
import com.senai.npsv_gestor_tintas_mobile.data.repository.ProdutoRepository
import com.senai.npsv_gestor_tintas_mobile.domain.model.NivelEstoque
import com.senai.npsv_gestor_tintas_mobile.domain.model.Produto
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen(
    onNavigateToUsuarios: () -> Unit
) {
    val context = LocalContext.current
    val tokenStore = remember { TokenStore(context) }
    val apiService = remember { RetrofitCliente.criarServico(tokenStore) }
    val repository = remember { ProdutoRepository(apiService) }

    val viewModel: EstoqueViewModel = viewModel(factory = EstoqueViewModel.Factory(repository))
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.title_estoque),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Button(
                onClick = onNavigateToUsuarios,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("Utilizadores", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por nome ou código de barras...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoriaEstoque.values().forEach { categoria ->
                FilterChip(
                    selected = uiState.categoriaSelecionada == categoria,
                    onClick = { viewModel.onCategoriaSelected(categoria) },
                    label = {
                        Text(
                            text = when(categoria) {
                                CategoriaEstoque.TUDO -> stringResource(R.string.categoria_tudo)
                                CategoriaEstoque.BASE -> stringResource(R.string.categoria_base)
                                CategoriaEstoque.PIGMENTO -> stringResource(R.string.categoria_pigmento)
                                CategoriaEstoque.INSUMO -> stringResource(R.string.categoria_insumo)
                            }
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
                uiState.produtosExibidos.isEmpty() -> {
                    Text(
                        text = "Nenhum produto encontrado.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.produtosExibidos) { produto ->
                            ProdutoCard(produto)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProdutoCard(produto: Produto) {
    val formatoMoeda = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("pt", "BR"))

    val (corFundo, corTexto, icone, textoTag) = when (produto.nivelEstoque) {
        NivelEstoque.BAIXO -> listOf(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            Icons.Default.Warning,
            "Estoque Baixo"
        )
        NivelEstoque.ALTO -> listOf(
            Color(0xFFE8F5E9),
            Color(0xFF2E7D32),
            Icons.Default.CheckCircle,
            "Estoque Adequado"
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = produto.descricao,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Cod: ${produto.codigoBarras} | ${produto.categoria.uppercase()}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(corFundo as Color)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = icone as androidx.compose.ui.graphics.vector.ImageVector,
                        contentDescription = "Status",
                        tint = corTexto as Color,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = textoTag as String,
                        color = corTexto,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${produto.quantidadeEstoque} ${produto.unidadeMedida}",
                    fontWeight = FontWeight.Bold,
                    color = if (produto.nivelEstoque == NivelEstoque.BAIXO) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
                Text(
                    text = formatoMoeda.format(produto.precoVenda),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}