package hr.tvz.boggle.chat;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImplementation implements ChatService {

    private List<String> chatMessagesHistory = new ArrayList<>();

    @Override
    public void sendChatMessage(String chatMessage) throws RemoteException {
        chatMessagesHistory.add(chatMessage);
    }

    @Override
    public List<String> returnChatHistory() throws RemoteException {
        return chatMessagesHistory;
    }

}
