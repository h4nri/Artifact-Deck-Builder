import org.json.JSONArray;

import javax.swing.*;

public class Card {
    private final int cardID;
    private final String cardType;
    private final String cardName;
    private final String cardText;
    private final ImageIcon miniImage;
    private final ImageIcon largeImage;
    private final int hitPoints;
    private final JSONArray references;

    public Card(int cardID, String cardType, String cardName, String cardText, ImageIcon miniImage, ImageIcon largeImage,
                int hitPoints, JSONArray references) {
        this.cardID = cardID;
        this.cardType = cardType;
        this.cardName = cardName;
        this.cardText = cardText;
        this.miniImage = miniImage;
        this.largeImage = largeImage;
        this.hitPoints = hitPoints;
        this.references = references;
    }

    public int getCardID() {
        return cardID;
    }

    public String getCardType() {
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

    public int getHitPoints() {
        return hitPoints;
    }

    public JSONArray getReferences() {
        return references;
    }
}
