// SocketManager.kt
object SocketManager {
    private var socket: Socket? = null
    
    fun initialize(token: String) {
        try {
            val options = IO.Options().apply {
                this.query = "token=$token"
            }
            socket = IO.socket("https://your-api-url", options)
            socket?.connect()
        } catch (e: Exception) {
            Log.e("SocketManager", "Connection error", e)
        }
    }

    fun listenForMessages(callback: (Message) -> Unit) {
        socket?.on("newMessage") { args ->
            val message = Gson().fromJson(args[0].toString(), Message::class.java)
            callback(message)
        }
    }

    fun sendMessage(message: Message) {
        socket?.emit("sendMessage", Gson().toJson(message))
    }
}

// ChatScreen.kt
@Composable
fun ChatScreen(receiverId: String) {
    val messages = remember { mutableStateListOf<Message>() }
    val viewModel: ChatViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        // Load existing messages
        viewModel.loadMessages(receiverId)
        
        // Listen for new messages
        SocketManager.listenForMessages { message ->
            if (message.senderId == receiverId || message.receiverId == receiverId) {
                messages.add(message)
            }
        }
    }

    Column {
        LazyColumn {
            items(messages) { message ->
                MessageBubble(message)
            }
        }
        
        MessageInput { text ->
            viewModel.sendMessage(receiverId, text)
        }
    }
}