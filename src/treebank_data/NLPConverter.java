package treebank_data;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import opennlp.tools.tokenize.*;

public class NLPConverter extends DefaultHandler{

    private String currentValue = "";
    private boolean specialChars;
	private String file;
    String parserClass = "org.apache.xerces.parsers.SAXParser";

    public void NLPConverter(){}

	public void startElement(String uri, String localName,
	                         String qName, Attributes atts)
			throws SAXException
	{
		boolean id = false;
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)));
			for (int i = 0; i < atts.getLength(); i++) {
				qName = atts.getQName(i);
				localName = atts.getLocalName(i);
				uri = atts.getURI(i);
				if (qName.equals("id")) {
					String value = atts.getValue(qName);
					if (value.equals("1")) id = true;
					else id = false;
				}
				if (qName.equals("form")) {
					String value = atts.getValue(qName);
					if (value.equals(",") || value.equals(";")) System.out.print("<SPLIT>" + value);
					else if (id && value.equals("'")) System.out.print("'<SPLIT>");
					else if (value.equals("!") || value.equals("?") || value.equals(".") || value.equals(":") || value.equals("'") || value.equals("·"))
						System.out.print("<SPLIT>" + value);
					else if (value.equals("-que")) System.out.print("<SPLIT>que");
					else if (id) System.out.print(value);
					else System.out.print(" " + value);

					if (value.equals(",") || value.equals(";")) out.print("<SPLIT>" + value);
					else if (id && value.equals("'")) out.print("'<SPLIT>");
					else if (value.equals("!") || value.equals("?") || value.equals(".") || value.equals(":") || value.equals("'") || value.equals("·"))
						out.print("<SPLIT>" + value);
					else if (value.equals("-que")) out.print("<SPLIT>que");
					else if (id) out.print(value);
					else out.print(" " + value);



				}


			}
			out.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}

	public void endElement(String uri, String localName,
	                       String qName) throws SAXException
	{
		try{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)));
			if (qName.equals("sentence")) System.out.println("");
			if (qName.equals("sentence")) out.println("");
			out.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}



	/*public void parse(String file){

		XMLReader parser = null;
		try {
			parser = XMLReaderFactory.createXMLReader(parserClass);
		}
		catch(SAXException e){
			System.out.println("Parser Initialisierungsfehler");
			System.exit(1);
		}
		parser.setContentHandler(this);
		parser.setErrorHandler(this);
		try {
			parser.parse(file);
		} catch (SAXException | IOException e) {

		}

	}*/

	public static void main(String[] args){

		try{

			File f = new File("output.txt");
			f.delete();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			FileReader fr = new FileReader("v2.0/Latin/perseus-lattb.2219.1.xml");
			FileReader fr2 = new FileReader("v2.0/Latin/perseus-lattb.1248.1.xml");
			InputSource is2 = new InputSource(fr2);
			InputSource is = new InputSource(fr);

			NLPConverter converter = new NLPConverter();

			saxParser.parse(is,converter);
			saxParser.parse(is2,converter);

		}
		catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}

	}





}
