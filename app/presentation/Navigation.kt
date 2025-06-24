// Navigation.kt
@Composable
fun GroceryAppNavHost(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("main") },
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("main") },
                onBackToLogin = { navController.popBackStack() }
            )
        }
        composable("main") {
            if (isAdmin) {
                AdminDashboard(navController)
            } else {
                UserDashboard(navController)
            }
        }
        composable("chat") {
            ChatScreen()
        }
    }
}