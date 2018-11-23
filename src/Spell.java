import org.json.JSONArray;

import javax.swing.*;

public class Spell extends Card {
    private final CardColor color;
    private final int manaCost;

    public Spell(int cardID, CardType cardType, String cardName, String cardText, ImageIcon miniImage, ImageIcon largeImage,
                JSONArray references, CardColor color, int manaCost) {
        super(cardID, cardType, cardName, cardText, miniImage, largeImage, references);
        this.color = color;
        this.manaCost = manaCost;
    }

    public CardColor getColor() {
        return color;
    }

    public int getManaCost() {
        return manaCost;
    }
}
