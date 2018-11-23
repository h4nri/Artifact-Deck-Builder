import org.json.JSONArray;

import javax.swing.*;

public abstract class Card {
    private final int cardID;
    private final CardType cardType;
    private final String cardName;
    private final String cardText;
    private final ImageIcon miniImage;
    private final ImageIcon largeImage;
    private final JSONArray references;

    public Card() {
        cardID = -1;
        cardType = null;
        cardName = "Default";
        cardText = "This card was created using the default constructor of the Card class.";
        miniImage = null;
        largeImage = null;
        references = null;
    }

    public Card(int cardID, CardType cardType, String cardName, String cardText, ImageIcon miniImage, ImageIcon largeImage,
                JSONArray references) {
        this.cardID = cardID;
        this.cardType = cardType;
        this.cardName = cardName;
        this.cardText = cardText;
        this.miniImage = miniImage;
        this.largeImage = largeImage;
        this.references = references;
    }

    public int getCardID() {
        return cardID;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardText() {
        return cardText;
    }

    public ImageIcon getMiniImage() {
        return miniImage;
    }

    public ImageIcon getLargeImage() {
        return largeImage;
    }

    public JSONArray getReferences() {
        return references;
    }
}
