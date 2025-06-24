// NotificationUtils.kt
object NotificationUtils {
    private const val CHANNEL_ID = "balance_channel"
    private const val NOTIFICATION_ID = 1
    
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Balance Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for balance changes"
            }
            
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showBalanceNotification(context: Context, newBalance: Double) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_wallet)
            .setContentTitle("Balance Updated")
            .setContentText("Your new balance: ₹$newBalance")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}

// BalanceObserver.kt
class BalanceObserver(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) : DefaultLifecycleObserver {
    
    override fun onStart(owner: LifecycleOwner) {
        // Listen for balance updates from server
        SocketManager.addBalanceListener { newBalance ->
            NotificationUtils.showBalanceNotification(context, newBalance)
        }
    }
    
    override fun onStop(owner: LifecycleOwner) {
        SocketManager.removeBalanceListeners()
    }
}

// In MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationUtils.createNotificationChannel(this)
        
        lifecycle.addObserver(BalanceObserver(
            context = this,
            notificationManager = NotificationManagerCompat.from(this)
        )
        
        setContent {
            GroceryAppTheme {
                Surface {
                    GroceryApp()
                }
            }
        }
    }
}
