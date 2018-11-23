import org.json.JSONArray;

import javax.swing.*;

public class Creep extends Card {
    private final CardColor color;
    private final int manaCost;
    private final int attack;
    private final int armor;
    private final int hitPoints;

    public Creep(int cardID, CardType cardType, String cardName, String cardText, ImageIcon miniImage, ImageIcon largeImage,
                 JSONArray references, CardColor color, int manaCost, int attack, int armor, int hitPoints) {
        super(cardID, cardType, cardName, cardText, miniImage, largeImage, references);
        this.color = color;
        this.manaCost = manaCost;
        this.attack = attack;
        this.armor = armor;
        this.hitPoints = hitPoints;
    }

    public CardColor getColor() {
        return color;
    }

    public int getManaCost() {
        return manaCost;
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
