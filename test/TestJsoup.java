import javax.swing.text.html.HTML.Tag;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class TestJsoup {

    @Test
    public void test() {
        Document doc = new Document("");
        Element select = doc.createElement("select");
        select.attr("name", "selKey");
        select.appendChild(doc.createElement("option"));
        
    }

}
