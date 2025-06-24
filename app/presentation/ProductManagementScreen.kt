// ProductManagementScreen.kt
@Composable
fun ProductManagementScreen(viewModel: AdminViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState()
    
    LazyVerticalGrid(columns = GridCells.Adaptive(128.dp)) {
        items(products) { product ->
            ProductCard(product)
        }
        item { AddProductButton { viewModel.createProduct(it) } }
    }
}

// AddProductDialog.kt
@Composable
fun AddProductDialog(onCreate: (Product) -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = { /* Close */ },
        title = { Text("Add New Product") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                TextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
                TextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onCreate(Product(
                    name = name,
                    price = price.toDouble(),
                    stock = stock.toInt()
                ))
            }) {
                Text("Add Product")
            }
        }
    )
}