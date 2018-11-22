import org.json.JSONArray;
import org.json.JSONException;
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

    /**
     * Sends two GET requests to:
     * 1) Get URL of set's json file
     * 2) Read from the json file
     *
     * @param set The set to be requested
     * @return A JSONObject containing information on set
     */
    public static JSONObject sendGetRequests(String set) {
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

                JSONObject jsonObject = new JSONObject(response.toString());
                url = new URL((String) jsonObject.get("cdn_root") + jsonObject.get("url"));
                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() == 200) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    response = new StringBuffer();

                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine + "\n");
                    }

                    jsonObject = new JSONObject(response.toString());
                }

                br.close();
                //System.out.println(response);
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a list of playable cards in a set based on its json file
     * Playable cards include: heroes, creeps, items, & spells
     *
     * @param jsonObj A JSONObject created from a set's json file
     * @return A list of playable cards from the set detailed in jsonObj
     */
    public static List<Card> readJSONFile(JSONObject jsonObj) {
        List<Card> cards = new ArrayList<>();
        JSONObject cardSet = (JSONObject) jsonObj.get("card_set");
        JSONArray cardList = (JSONArray) cardSet.get("card_list");

        for (Object object : cardList) {
            JSONObject jsonObject = (JSONObject) object;
            String cardType = (String) jsonObject.get("card_type");

            if (cardType.equals("Hero") || cardType.equals("Creep") || cardType.equals("Item")
                    || cardType.equals("Spell")) {
                int cardID = (int) jsonObject.get("card_id");
                String cardName = (String) ((JSONObject) jsonObject.get("card_name")).get("english");
                String cardText;
                ImageIcon miniImage;
                ImageIcon largeImage;
                int hitPoints;
                JSONArray references = jsonObject.getJSONArray("references");

                try {
                    cardText = (String) ((JSONObject) jsonObject.get("card_text")).get("english");
                } catch (JSONException e) {
                    cardText = "";
                }

                try {
                    URL url = new URL((String) ((JSONObject) jsonObject.get("mini_image")).get("default"));
                    miniImage = new ImageIcon(ImageIO.read(url));
                    url = new URL((String) ((JSONObject) jsonObject.get("large_image")).get("default"));
                    largeImage = new ImageIcon(ImageIO.read(url));
                } catch (IOException e) {
                    miniImage = null;
                    largeImage = null;
                }

                try {
                    hitPoints = (int) jsonObject.get("hit_points");
                } catch (JSONException e) {
                    hitPoints = 0;
                }

                Card card = new Card(cardID, cardType, cardName, cardText, miniImage, largeImage, hitPoints, references);
                cards.add(card);
                //System.out.println(card.getCardName());
            }
        }

        return cards;
    }

    public static void drawCards(List<Card> cards) {
        JPanel cardsPanel = new JPanel();

        for (Card card : cards) {
            BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(card.getMiniImage().getImage(), 0, 0, 100, 100, null);
            g2.dispose();

            JButton button = new JButton(new ImageIcon(resizedImg));
            button.setBackground(Color.WHITE);
            button.setPreferredSize(new Dimension(100, 100));
            cardsPanel.add(button);
        }

        JFrame mainFrame = new JFrame("Artifact Deck Builder");
        mainFrame.setContentPane(cardsPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JSONObject obj = sendGetRequests("00/");
        if (obj != null) {
            List<Card> cards = readJSONFile(obj);
            drawCards(cards);
        }
    }
}
