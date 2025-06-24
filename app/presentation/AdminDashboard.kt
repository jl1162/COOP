// AdminDashboard.kt
@Composable
fun AdminDashboard(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Admin Dashboard") }) },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                NavigationRail {
                    items.forEach { item ->
                        NavigationRailItem(
                            selected = currentRoute == item.route,
                            onClick = { navController.navigate(item.route) },
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.label) }
                        )
                    }
                }
                NavHost(navController, "admin/users") {
                    composable("admin/users") { UserManagementScreen() }
                    composable("admin/products") { ProductManagementScreen() }
                    composable("admin/orders") { OrderListScreen() }
                    composable("admin/balance") { BalanceManagementScreen() }
                }
            }
        }
    )
}

// UserManagementScreen.kt
@Composable
fun UserManagementScreen(viewModel: AdminViewModel = hiltViewModel()) {
    val users by viewModel.users.collectAsState()
    
    LazyColumn {
        items(users) { user ->
            UserCard(user, onEditBalance = { viewModel.updateBalance(user.id, it) })
        }
        item { AddUserButton { viewModel.createUser(it) } }
    }
}
