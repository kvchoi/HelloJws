import java.io.ByteArrayOutputStream;

import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.html.dom.HTMLOptionElementImpl;
import org.apache.html.dom.HTMLSelectElementImpl;
import org.junit.Test;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import utils.XmlUtils;

public class TestXerces {

    @Test
    public void test() {
        HTMLDocumentImpl doc = new HTMLDocumentImpl();
        HTMLSelectElement select = new HTMLSelectElementImpl(doc, "select");
        select.setName("selKey");
        select.setClassName("className");
        HTMLOptionElement op1 = new HTMLOptionElementImpl(doc, "option");
        op1.setValue("val1");
        op1.setTextContent("text1");
        HTMLOptionElement op2 = new HTMLOptionElementImpl(doc, "option");
        op2.setValue("val2");
        op2.setTextContent("text2");
        op2.setSelected(true);
        HTMLOptionElement op3 = new HTMLOptionElementImpl(doc, "option");
        op3.setValue("val3");
        op3.setTextContent("text3");
        select.appendChild(op1);
        select.appendChild(op2);
        select.appendChild(op3);
        doc.appendChild(select);
        select.setSelectedIndex(0);// 选中

        // ====================================
        System.out.println(XmlUtils.serialize(doc));
    }

}
