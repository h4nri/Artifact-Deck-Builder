import org.json.JSONArray;

import javax.swing.*;

public class Hero extends Card {
    private final CardColor color;
    private final int attack;
    private final int armor;
    private final int hitPoints;

    public Hero(int cardID, CardType cardType, String cardName, String cardText, ImageIcon miniImage, ImageIcon largeImage,
                JSONArray references, CardColor color, int attack, int armor, int hitPoints) {
        super(cardID, cardType, cardName, cardText, miniImage, largeImage, references);
        this.color = color;
        this.attack = attack;
        this.armor = armor;
        this.hitPoints = hitPoints;
    }

    public CardColor getColor() {
        return color;
    }

    public int getAttack() {
        return attack;
    }

    public int getArmor() {
        return armor;
    }

    public int getHitPoints() {
        return hitPoints;
    }
}
