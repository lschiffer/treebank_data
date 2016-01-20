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

public class NLPConverterPOS extends DefaultHandler{

	private String currentValue = "";
	private boolean specialChars;
	private String file;
	String parserClass = "org.apache.xerces.parsers.SAXParser";

	public void NLPConverter(){}

	public void startElement(String uri, String localName,
	                         String qName, Attributes atts)
			throws SAXException
	{
		//boolean id = false;
		boolean insert = true;
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("outputPOS.txt", true)));
			for (int i = 0; i < atts.getLength(); i++) {
				qName = atts.getQName(i);
				if (qName.equals("insertion_id")) {
					String value = atts.getValue(qName);
					if (value.contains("0")) {
						insert = false;
					} else insert = true;
				}
			/*	if (qName.equals("id")) {
					String value = atts.getValue(qName);
					if (value.equals("1")) id = true;
					else id = false;*/

				if (qName.equals("form") && insert) {
					String value = atts.getValue(qName);
					if(value.equals(".")){
						System.out.print(value+"_. ");
						out.print(value+"_. ");
					}
					else if(value.equals(",")){
						System.out.print(value+"_, ");
						out.print(value+"_, ");
					}
					else if(value.equals(":")){
						System.out.print(value+"_: ");
						out.print(value+"_: ");
					}
					else if(value.equals(";")){
						System.out.print(value+"_; ");
						out.print(value+"_; ");
					}
					else if(value.equals("?")){
						System.out.print(value+"_? ");
						out.print(value+"_? ");
					}
					else if(value.equals("!")){
						System.out.print(value+"_! ");
						out.print(value+"_! ");
					}
					else if(value.equals("'")){
						System.out.print(value+"_' ");
						out.print(value+"_' ");
					}
					else {
						System.out.print(value);
						out.print(value);
					}
				}
				if (qName.equals("postag")) {
					String value = atts.getValue(qName);
					if(value.isEmpty()){
						System.out.print("_" + "FW" + " ");
						out.print("_" + "FW" + " ");
					}
					else if(value.charAt(0)=='v'){
						if(value.charAt(3)=='r' && value.charAt(4)=='p'){
							System.out.print("_" + "VBN" + " ");
							out.print("_" + "VBN" + " ");
						}
						else if(value.charAt(4)=='n'){
							System.out.print("_" + "VB" + " ");
							out.print("_" + "VB" + " ");
						}
						else if(value.charAt(4)=='d' ||value.charAt(4)=='g'){
							System.out.print("_" + "VBG" + " ");
							out.print("_" + "VBG" + " ");
						}
						else if(value.charAt(3)=='p' || value.charAt(3)=='t' || value.charAt(3)=='f'){
							System.out.print("_" + "VBP" + " ");
							out.print("_" + "VBP" + " ");
						}
						else if(value.charAt(3)=='i' || value.charAt(3)=='r' || value.charAt(3)=='l'){
							System.out.print("_" + "VBD" + " ");
							out.print("_" + "VBD" + " ");
						}
					}

					else if(value.charAt(0)=='n'){
						System.out.print("_" + "NN" + " ");
						out.print("_" + "NN" + " ");
					}
					else if(value.charAt(0)=='t'){
						System.out.print("_" + "RP" + " ");
						out.print("_" + "RP" + " ");
					}
					else if(value.charAt(0)=='a'){
						System.out.print("_" + "JJ" + " ");
						out.print("_" + "JJ" + " ");
					}
					else if(value.charAt(0)=='d'){
						System.out.print("_" + "RB" + " ");
						out.print("_" + "RB" + " ");
					}
					else if(value.charAt(0)=='c'){
						System.out.print("_" + "CC" + " ");
						out.print("_" + "CC" + " ");
					}
					else if(value.charAt(0)=='r'){
						System.out.print("_" + "IN" + " ");
						out.print("_" + "IN" + " ");
					}
					else if(value.charAt(0)=='p'){
						System.out.print("_" + "PRP" + " ");
						out.print("_" + "PRP" + " ");
					}
					else if(value.charAt(0)=='m'){
						System.out.print("_" + "CD" + " ");
						out.print("_" + "CD" + " ");
					}
					else if(value.charAt(0)=='i'){
						System.out.print("_" + "UH" + " ");
						out.print("_" + "UH" + " ");
					}
					else if(value.charAt(0)=='e'){
						System.out.print("_" + "UH" + " ");
						out.print("_" + "UH" + " ");
					}
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
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("outputPOS.txt", true)));
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

			File f = new File("outputPOS.txt");
			f.delete();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			FileReader fr = new FileReader("v2.0/Latin/perseus-lattb.2219.1.xml");
		/*	FileReader fr2 = new FileReader("v2.0/Latin/perseus-lattb.1248.1.xml");
			FileReader fr3 = new FileReader("v2.0/Latin/phi0448.phi001.perseus-lat1.tb.xml");
			FileReader fr4 = new FileReader("v2.0/Latin/phi0474.phi013.perseus-lat1.tb.xml");
			FileReader fr5 = new FileReader("v2.0/Latin/phi0620.phi001.perseus-lat1.tb.xml");
			FileReader fr6 = new FileReader("v2.0/Latin/phi0631.phi001.perseus-lat1.tb.xml");
			FileReader fr7 = new FileReader("v2.0/Latin/phi0690.phi003.perseus-lat1.tb.xml");
			FileReader fr8 = new FileReader("v2.0/Latin/phi0959.phi006.perseus-lat1.tb.xml");
			FileReader fr9 = new FileReader("v2.0/Latin/phi0972.phi001.perseus-lat1.tb.xml");
			FileReader fr0 = new FileReader("v2.0/Latin/tlg0031.tlg027.perseus-lat1.tb.xml");*/

		//	InputSource is2 = new InputSource(fr2);
			InputSource is = new InputSource(fr);
		/*	InputSource is3 = new InputSource(fr3);
			InputSource is4 = new InputSource(fr4);
			InputSource is5 = new InputSource(fr5);
			InputSource is6 = new InputSource(fr6);
			InputSource is7 = new InputSource(fr7);
			InputSource is8 = new InputSource(fr8);
			InputSource is9 = new InputSource(fr9);
			InputSource is0 = new InputSource(fr0);*/

			NLPConverterPOS converter = new NLPConverterPOS();

			saxParser.parse(is,converter);
/*			saxParser.parse(is2,converter);
			saxParser.parse(is3,converter);
			saxParser.parse(is4,converter);
			saxParser.parse(is5,converter);
			saxParser.parse(is6,converter);
			saxParser.parse(is7,converter);
			saxParser.parse(is8,converter);
			saxParser.parse(is9,converter);
			saxParser.parse(is0,converter);*/



		}
		catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}

	}





}
