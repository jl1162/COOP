// BalanceObserver.kt
class BalanceObserver : LifecycleObserver {
    private val socket = SocketManager.getSocket()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startListening() {
        socket?.on("balanceUpdate") { args ->
            val update = Gson().fromJson(args[0].toString(), BalanceUpdate::class.java)
            showBalanceNotification(update)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopListening() {
        socket?.off("balanceUpdate")
    }

    private fun showBalanceNotification(update: BalanceUpdate) {
        // Create system notification
        val notification = NotificationCompat.Builder(context, "balance_channel")
            .setContentTitle("Balance Updated")
            .setContentText("New balance: $${update.newBalance}")
            .setSmallIcon(R.drawable.ic_wallet)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }
}

// In Activity/Fragment:
lifecycle.addObserver(BalanceObserver())