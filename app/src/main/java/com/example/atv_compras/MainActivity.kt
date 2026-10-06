package com.example.atv_compras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atv_compras.ui.theme.Atv_ComprasTheme

class Produto(
    val nome: String,
    val preco: Double,
    val quantidade: Int
) {
    fun calcularTotal(): Double {
        return preco * quantidade
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Atv_ComprasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListaCompras()
                }
            }
        }
    }
}

@Composable
fun Titulo() {
    Text(
        text = "🛒 Lista de Compras",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun Formulario(
    nome: String,
    preco: String,
    quantidade: String,
    onNomeChange: (String) -> Unit,
    onPrecoChange: (String) -> Unit,
    onQuantidadeChange: (String) -> Unit,
    onCadastrar: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = nome,
            onValueChange = onNomeChange,
            label = { Text("Nome do Produto") },
            placeholder = { Text("Ex: Arroz 5kg") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = preco,
            onValueChange = onPrecoChange,
            label = { Text("Preço Unitário (R$)") },
            placeholder = { Text("Ex: 24.90") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = quantidade,
            onValueChange = onQuantidadeChange,
            label = { Text("Quantidade") },
            placeholder = { Text("Ex: 2") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = onCadastrar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text(
                text = "Cadastrar Produto",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ListaProdutos(
    produtos: List<Produto>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (produtos.isNotEmpty()) {
            Text(
                text = "Itens Cadastrados:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        for (produto in produtos) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "${produto.nome} - ${produto.quantidade}x R$ ${"%.2f".format(produto.preco)} = R$ ${"%.2f".format(produto.calcularTotal())}",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun Resumo(total: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = "Total da compra: R$ ${"%.2f".format(total)}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(14.dp)
        )
    }
}


@Composable
fun ListaCompras() {
    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    val produtos = remember { mutableStateListOf<Produto>() }
    var total by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Titulo()

        Formulario(
            nome = nome,
            preco = preco,
            quantidade = quantidade,
            onNomeChange = { nome = it },
            onPrecoChange = { preco = it },
            onQuantidadeChange = { quantidade = it },
            onCadastrar = {
                val precoDouble = preco.replace(",", ".").toDoubleOrNull()
                val qtdInt = quantidade.toIntOrNull()

                if (nome.isNotBlank() && precoDouble != null && qtdInt != null) {
                    val produto = Produto(
                        nome = nome.trim(),
                        preco = precoDouble,
                        quantidade = qtdInt
                    )
                    produtos.add(produto)
                    total += produto.calcularTotal()

                    nome = ""
                    preco = ""
                    quantidade = ""
                }
            }
        )

        ListaProdutos(produtos)

        Resumo(total)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTitulo() {
    Titulo()
}

@Preview(showBackground = true)
@Composable
fun PreviewFormulario() {
    Formulario(
        nome = "Arroz",
        preco = "25.0",
        quantidade = "2",
        onNomeChange = {},
        onPrecoChange = {},
        onQuantidadeChange = {},
        onCadastrar = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewListaProdutos() {
    val exemplo = listOf(
        Produto("Arroz 5kg", 25.0, 2),
        Produto("Feijão 1kg", 8.5, 3)
    )
    ListaProdutos(produtos = exemplo)
}

@Preview(showBackground = true)
@Composable
fun PreviewResumo() {
    Resumo(total = 75.5)
}

@Preview(showBackground = true)
@Composable
fun PreviewListaCompras() {
    Atv_ComprasTheme {
        ListaCompras()
    }
}
