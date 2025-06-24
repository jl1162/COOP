// OrderListScreen.kt
@Composable
fun OrderListScreen(viewModel: OrderViewModel = hiltViewModel()) {
    val orders by viewModel.orders.collectAsState()
    
    LazyColumn {
        items(orders) { order ->
            OrderItemCard(order)
        }
    }
}

// OrderViewModel.kt
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _orders = mutableStateListOf<Order>()
    val orders: List<Order> get() = _orders
    
    init {
        viewModelScope.launch {
            _orders.addAll(orderRepository.getOrders())
        }
    }
}