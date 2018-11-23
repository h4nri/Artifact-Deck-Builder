import org.json.JSONObject;

import java.util.List;

public class Runner {
    public static void main(String[] args) {
        CardLibrary cardLibrary = new CardLibrary();
        JSONObject json = cardLibrary.sendGetRequests("00/");

        if (json != null) {
            List<Card> cards = cardLibrary.readJSON(json);
            cardLibrary.drawCards(cards);
        }
    }
}
