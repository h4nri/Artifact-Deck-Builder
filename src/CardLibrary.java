import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CardLibrary {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String SET_BASE_PATH = "https://playartifact.com/cardset/";

    public CardLibrary() {

    }

    /**
     * Sends two GET requests to:
     * 1) Get URL of set's json file
     * 2) Read from the json file
     *
     * @param set The set to be requested
     * @return A JSONObject containing information on set
     */
    public JSONObject sendGetRequests(String set) {
        try {
            URL url = new URL(SET_BASE_PATH + set);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);

            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String responseLine;
                StringBuffer response = new StringBuffer();

                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine + "\n");
                }

                JSONObject json = new JSONObject(response.toString());
                url = new URL((String) json.get("cdn_root") + json.get("url"));
                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() == 200) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    response = new StringBuffer();

                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine + "\n");
                    }

                    json = new JSONObject(response.toString());
                }

                br.close();
                //System.out.println(json.toString(4));
                return json;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a list of playable cards in a set based on its JSON file
     * Playable cards include: creeps, heroes, items, and spells
     *
     * @param jsonObj A JSONObject created from a set's JSON file
     * @return A list of playable cards from the set detailed in jsonObj
     */
    public List<Card> readJSON(JSONObject jsonObj) {
        List<Card> cards = new ArrayList<>();
        JSONObject cardSet = jsonObj.getJSONObject("card_set");
        JSONArray cardList = cardSet.getJSONArray("card_list");

        for (Object object : cardList) {
            JSONObject json = (JSONObject) object;

            try {
                Card card;

                // Initialize variables that all Cards should have
                int cardID = json.getInt("card_id");
                CardType cardType = CardType.valueOf(json.getString("card_type").toUpperCase());
                String cardName = json.getJSONObject("card_name").getString("english");
                String cardText;
                ImageIcon miniImage;
                ImageIcon largeImage;
                JSONArray references = json.getJSONArray("references");

                if (json.getJSONObject("card_text").has("english")) {
                    cardText = json.getJSONObject("card_text").getString("english");
                } else {
                    cardText = "";
                }

                try {
                    URL url = new URL(json.getJSONObject("mini_image").getString("default"));
                    miniImage = new ImageIcon(ImageIO.read(url));
                } catch (IOException e) {
                    miniImage = null;
                }

                try {
                    URL url = new URL(json.getJSONObject("large_image").getString("default"));
                    largeImage = new ImageIcon(ImageIO.read(url));
                } catch (IOException e) {
                    largeImage = null;
                }

                // Initialize card type specific variables
                if (cardType == CardType.CREEP || cardType == CardType.HERO || cardType == CardType.SPELL) {
                    CardColor color;
                    int manaCost;
                    int attack;
                    int armor;
                    int hitPoints;

                    if (json.has("is_black")) {
                        color = CardColor.BLACK;
                    } else if (json.has("is_blue")) {
                        color = CardColor.BLUE;
                    } else if (json.has("is_green")) {
                        color = CardColor.GREEN;
                    } else if (json.has("is_red")) {
                        color = CardColor.RED;
                    } else {
                        throw new IllegalArgumentException("No valid color field was detected in the set's JSON file.");
                    }

                    switch (cardType) {
                        case CREEP:
                            manaCost = json.getInt("mana_cost");

                            if (json.has("attack")) {
                                attack = json.getInt("attack");
                            } else {
                                attack = 0;
                            }

                            if (json.has("armor")) {
                                armor = json.getInt("armor");
                            } else {
                                armor = 0;
                            }

                            hitPoints = json.getInt("hit_points");
                            card = new Creep(cardID, cardType, cardName, cardText, miniImage,
                                    largeImage, references, color, manaCost, attack, armor, hitPoints);
                            cards.add(card);
                            System.out.println("Creep: " + card.getCardName());
                            break;
                        case HERO:
                            attack = json.getInt("attack");

                            if (json.has("armor")) {
                                armor = json.getInt("armor");
                            } else {
                                armor = 0;
                            }

                            hitPoints = json.getInt("hit_points");
                            card = new Hero(cardID, cardType, cardName, cardText, miniImage,
                                    largeImage, references, color, attack, armor, hitPoints);
                            cards.add(card);
                            System.out.println("Hero: " + card.getCardName());
                            break;
                        case SPELL:
                            manaCost = json.getInt("mana_cost");
                            card = new Spell(cardID, cardType, cardName, cardText, miniImage,
                                    largeImage, references, color, manaCost);
                            cards.add(card);
                            System.out.println("Spell: " + card.getCardName());
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown input for card type detected.");
                    }
                } else if (cardType == CardType.ITEM) {
                    SubType subType = SubType.valueOf(json.getString("sub_type").toUpperCase());
                    int goldCost = json.getInt("gold_cost");
                    card = new Item(cardID, cardType, cardName, cardText, miniImage,
                            largeImage, references, subType, goldCost);
                    cards.add(card);
                    System.out.println("Item: " + card.getCardName());
                } else {
                    throw new IllegalArgumentException("Unknown input for card type detected.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("ILLEGAL: " + json.getJSONObject("card_name").getString("english"));
            }
        }

        return cards;
    }
}
