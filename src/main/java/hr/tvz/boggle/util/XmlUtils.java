package hr.tvz.boggle.util;

import hr.tvz.boggle.model.GameMove;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {

    private static final String GAME_MOVES_XML_FILE_NAME = "xml/gameMoves.xml";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void saveGameMovesToXml(List<GameMove> gameMoves) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("GameMoves");
            doc.appendChild(rootElement);

            for (GameMove gameMove : gameMoves) {
                Element gameMoveElement = doc.createElement("GameMove");

                Element playerNameElement = doc.createElement("PlayerName");
                playerNameElement.setTextContent(gameMove.getPlayerName());
                gameMoveElement.appendChild(playerNameElement);

                Element wordElement = doc.createElement("Word");
                wordElement.setTextContent(gameMove.getWord());
                gameMoveElement.appendChild(wordElement);

                Element localDateTimeElement = doc.createElement("LocalDateTime");
                localDateTimeElement.setTextContent(gameMove.getLocalDateTime().format(dateTimeFormatter));
                gameMoveElement.appendChild(localDateTimeElement);

                rootElement.appendChild(gameMoveElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(GAME_MOVES_XML_FILE_NAME));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GameMove> readGameMovesFromXml() {
        List<GameMove> gameMoves = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(GAME_MOVES_XML_FILE_NAME);
            Element rootElement = dom.getDocumentElement();
            NodeList nl = rootElement.getChildNodes();

            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("GameMove")) {
                    Element el = (Element) node;

                    String playerName = el.getElementsByTagName("PlayerName").item(0).getTextContent();
                    String word = el.getElementsByTagName("Word").item(0).getTextContent();
                    LocalDateTime localDateTime = LocalDateTime.parse(
                            el.getElementsByTagName("LocalDateTime").item(0).getTextContent(), dateTimeFormatter);

                    GameMove gameMove = new GameMove();
                    gameMove.setPlayerName(playerName);
                    gameMove.setWord(word);
                    gameMove.setLocalDateTime(localDateTime);

                    gameMoves.add(gameMove);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            throw new RuntimeException(ex);
        }
        return gameMoves;
    }
}
