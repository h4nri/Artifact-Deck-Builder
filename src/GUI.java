import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class GUI implements ActionListener {
    JFrame mainFrame;
    JPanel cardLibraryPanel;
    JPanel cardsPanel;
    JPanel decksPanel;
    JPanel deckContentsPanel;
    JPanel mainPanel;
    JPanel filtersPanel;

    JScrollPane cardsScrollPane;

    CardLibrary cardLibrary;

    public GUI() {
        decksPanel = new JPanel();
        decksPanel.setLayout(new BoxLayout(decksPanel, BoxLayout.PAGE_AXIS));
        decksPanel.setPreferredSize(new Dimension(280, 1000));

        deckContentsPanel = new JPanel();
        deckContentsPanel.setBackground(Color.BLACK);
        deckContentsPanel.setLayout(new BoxLayout(deckContentsPanel, BoxLayout.PAGE_AXIS));
        deckContentsPanel.setPreferredSize(new Dimension(280, 1000));

        filtersPanel = new JPanel();
        filtersPanel.setBackground(Color.WHITE);
        filtersPanel.setPreferredSize(new Dimension(1315, 80));

        cardLibraryPanel = new JPanel(new GridLayout(0, 5));
        cardLibrary = new CardLibrary();
        drawCards(cardLibrary.readJSON(cardLibrary.sendGetRequests("00/")));
        cardsScrollPane = new JScrollPane(cardLibraryPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        cardsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        cardsScrollPane.setPreferredSize(new Dimension(1335, 920));

        cardsPanel = new JPanel(new BorderLayout());
        cardsPanel.add(filtersPanel, BorderLayout.NORTH);
        cardsPanel.add(cardsScrollPane, BorderLayout.CENTER);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(decksPanel, BorderLayout.WEST);
        mainPanel.add(deckContentsPanel, BorderLayout.CENTER);
        mainPanel.add(cardsPanel, BorderLayout.EAST);

        mainFrame = new JFrame("Artifact Deck Builder");
        mainFrame.setContentPane(mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        mainFrame.pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - mainFrame.getWidth()) / 2);
        mainFrame.setLocation(x, 0);
    }

    public void drawCards(List<Card> cards) {
        for (Card card : cards) {
            BufferedImage resizedImage = new BufferedImage(263, 445, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(card.getLargeImage().getImage(), 0, 0, 263, 445, null);
            g2.dispose();

            JButton cardButton = new JButton(new ImageIcon(resizedImage));
            cardButton.addActionListener(this);
            cardButton.setActionCommand(Integer.toString(card.getCardID()));
            cardButton.setBackground(Color.WHITE);
            cardButton.setPreferredSize(new Dimension(263, 445));
            cardLibraryPanel.add(cardButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }
}
