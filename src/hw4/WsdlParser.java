/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hw4;

import com.ibm.wsdl.OperationImpl;
import com.ibm.wsdl.PartImpl;
import com.ibm.wsdl.PortTypeImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author alfredo
 */
public class WsdlParser {

    Types types;
    Map messages;
    Map portTypes;
    Map bindings;
    Map services;

    public WsdlParser(String filename) throws WSDLException {
        // get hold the WSDLFactory
        WSDLFactory factory = WSDLFactory.newInstance();

        // create an object to read the WSDL file
        WSDLReader reader = factory.newWSDLReader();

        // pass the URL to the reader for parsing and get back a WSDL definiton
        Definition wsdlInstance = reader.readWSDL(null, filename);

        // get a map of the five specific parts a WSDL file
        types = wsdlInstance.getTypes();
        messages = wsdlInstance.getMessages();
        portTypes = wsdlInstance.getPortTypes();
        bindings = wsdlInstance.getBindings();
        services = wsdlInstance.getServices();
    }

    public String getServiceName() {
        String s = "";
        Iterator it = services.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            s = ((QName) pairs.getKey()).getLocalPart();
        }
        return s;
    }

    public List<OperationImpl> getOperations() {
        List<OperationImpl> operations = new ArrayList<>();
        Iterator it = portTypes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            PortTypeImpl pti = (PortTypeImpl) pairs.getValue();
            operations = (List<OperationImpl>) pti.getOperations();
        }

        return operations;
    }

    public ArrayList<QName> getOutputElements(OperationImpl op) {

        ArrayList<QName> elementNames = new ArrayList<>();

        Map parts = op.getOutput().getMessage().getParts();
        Iterator it2 = parts.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pairs2 = (Map.Entry) it2.next();
            PartImpl part = (PartImpl) pairs2.getValue();


            QName elementName = part.getElementName();
            if (elementName == null) {
                elementName = part.getTypeName();
            }
            //System.out.println("par" + part);
            elementNames.add(elementName);

        }
        //System.out.println("aaaaaaaaa" + elementNames);
        return elementNames;
    }

    public ArrayList<QName> getInputElements(OperationImpl op) {

        ArrayList<QName> elementNames = new ArrayList<>();
        Map parts = op.getInput().getMessage().getParts();
        Iterator it2 = parts.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pairs2 = (Map.Entry) it2.next();
            PartImpl part = (PartImpl) pairs2.getValue();


            QName elementName = part.getElementName();
            if (elementName == null) {
                elementName = part.getTypeName();
            }

            elementNames.add(elementName);

        }
        return elementNames;
    }

    public List getExtensibilityElements() {
        return types.getExtensibilityElements();
    }

    public ArrayList<String> getElementsMatchingQnames(QName qname) {

        ArrayList<String> names = new ArrayList<>();

        for (Object o : getExtensibilityElements()) {
            if (o instanceof javax.wsdl.extensions.schema.Schema) {
                org.w3c.dom.Element elt = ((javax.wsdl.extensions.schema.Schema) o).getElement();

                
                NodeList nl = elt.getChildNodes();
                ArrayList<Node> all = new ArrayList<>();

                for (int i = 0; i < nl.getLength(); i++) {
                    all.addAll(returnAllNodes(nl.item(i)));
                }

                for (Node ComplexElement : all) {

                    if (ComplexElement.getAttributes() != null && ComplexElement.getAttributes().getNamedItem("name") != null && qname != null && qname.getLocalPart() != null && qname.getLocalPart().equals(ComplexElement.getAttributes().getNamedItem("name").getTextContent())) {

                        for (Node n : returnAllNodes(ComplexElement)) {


                            if (!n.hasChildNodes() && n.getLocalName() != null && n.getLocalName().compareTo("element") == 0) {

                                NamedNodeMap attributes = n.getAttributes();
                                String textContent = attributes.getNamedItem("name").getTextContent();
                                String type = attributes.getNamedItem("type").getNodeValue();
                                String[] split = type.split(":");
                                int index = 0;
                                if (split.length == 2) {
                                    index = 1;
                                }
                                String unqualifiedtype = split[index];
                                //System.out.println("type: " + type);
                                //System.out.println("qname:" + qname.getLocalPart());

                                if (isValidType(unqualifiedtype)) {
                                    //System.out.println("type: " + type);
                                    //System.out.println("qname:" + qname.getLocalPart());
                                    //System.out.println(textContent);
                                    names.add(textContent);
                                }

                            }

                        }
                    }

                }
            }
        }
        return names;
    }

    private static void addAllNodes(Node node, List<Node> listOfNodes) {
        if (node != null) {
            listOfNodes.add(node);
            NodeList childNodes = node.getChildNodes();

            List<Node> children = null;

            if (childNodes != null) {
                children = new ArrayList<>();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    children.add(childNodes.item(i));
                }
            }


            if (children != null) {
                for (Node child : children) {
                    addAllNodes(child, listOfNodes);
                }
            }
        }
    }

    private static boolean isValidType(String t) {
        if (t.equals("string") || t.equals("int") || t.equals("double") || t.equals("date")) {
            return true;
        }
        return false;
    }

    public static List<Node> returnAllNodes(Node node) {
        List<Node> listOfNodes = new ArrayList<Node>();
        addAllNodes(node, listOfNodes);
        return listOfNodes;
    }
}