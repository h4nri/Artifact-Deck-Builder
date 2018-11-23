import org.json.JSONArray;

import javax.swing.*;

enum SubType { ACCESSORY, ARMOR, CONSUMABLE, WEAPON };

public class Item extends Card {
    private final SubType subType;
    private final int goldCost;

    public Item(int cardID, CardType cardType, String cardName, String cardText, ImageIcon miniImage, ImageIcon largeImage,
                JSONArray references, SubType subType, int goldCost) {
        super(cardID, cardType, cardName, cardText, miniImage, largeImage, references);
        this.subType = subType;
        this.goldCost = goldCost;
    }

    public SubType getSubType() {
        return subType;
    }

    public int getGoldCost() {
        return goldCost;
    }
}
