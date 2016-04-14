package utils;

import java.io.OutputStream;
import java.io.StringWriter;

import jws.Logger;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class XmlUtils {

    private static final LSSerializer writer;
    static {
        writer = init();
    }

    public static LSSerializer getDefaultWriter() {
        if (writer == null) {
            Logger.warn("no available writer");
        }
        return writer;
    }

    private static LSSerializer init() {
        LSSerializer serializer;
        try {
            // System.setProperty(DOMImplementationRegistry.PROPERTY,
            // "org.apache.xerces.dom.DOMImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry
                    .getDOMImplementation("LS");
            serializer = impl.createLSSerializer();
            DOMConfiguration config = serializer.getDomConfig();
            config.setParameter("xml-declaration", Boolean.FALSE);
            // config.setParameter("split-cdata-sections",Boolean.FALSE);
            config.setParameter("format-pretty-print", Boolean.TRUE);
            return serializer;
        } catch (Exception e) {
            Logger.error(e, "createLSSerializer fail");
            return null;
        }
    }

    public static String serialize(Document document) {
        return getDefaultWriter() != null ? getDefaultWriter().writeToString(document)
                : StringUtils.EMPTY;
    }

    public static void serialize(Document document, OutputStream output, boolean withXmlDeclaration)
            throws LSException {
        final DOMImplementationLS ls = (DOMImplementationLS) document.getImplementation()
                .getFeature("LS", "3.0");
        final LSOutput out = ls.createLSOutput();
        out.setByteStream(output);
        final LSSerializer serializer = ls.createLSSerializer();
        serializer.getDomConfig().setParameter("xml-declaration", withXmlDeclaration);
        serializer.getDomConfig().setParameter("format-pretty-print", true);
        serializer.write(document, out);
    }

    public static String format(Document doc) {
        DOMImplementation domImplementation = doc.getImplementation();
        if (domImplementation.hasFeature("LS", "3.0")
                && domImplementation.hasFeature("Core", "2.0")) {
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation
                    .getFeature("LS", "3.0");
            LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
            DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
            if (domConfiguration.canSetParameter("format-pretty-print", Boolean.TRUE)) {
                lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
                LSOutput lsOutput = domImplementationLS.createLSOutput();
                lsOutput.setEncoding("UTF-8");
                StringWriter stringWriter = new StringWriter();
                lsOutput.setCharacterStream(stringWriter);
                lsSerializer.write(doc, lsOutput);
                return stringWriter.toString();
            } else {
                throw new RuntimeException(
                        "DOMConfiguration 'format-pretty-print' parameter isn't settable.");
            }
        } else {
            throw new RuntimeException("DOM 3.0 LS and/or DOM 2.0 Core not supported.");
        }
    }

}
