import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ReadXMLFile {

    public ReadXMLFile() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
	try {

	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    SAXParser saxParser = factory.newSAXParser();

	    //saxParser.parse("TextFile.xml", new MyPushParser());   //This one line will scan the whole document
	    saxParser.parse("stop.xml", new MyPushParser());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
