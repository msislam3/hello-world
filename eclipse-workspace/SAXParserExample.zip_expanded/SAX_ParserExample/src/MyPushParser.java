import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyPushParser extends DefaultHandler {

	public MyPushParser() {
		// TODO Auto-generated constructor stub
	}

	boolean bfname = false;
	boolean blname = false;
	boolean bnname = false;
	boolean bsalary = false;

	boolean stopNo = false;
	boolean stopDescription = false;
	boolean routeNo = false;
	boolean routeDirection = false;
	boolean routeHeading = false;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		//System.out.println("Start Element :" + qName);

		if (qName.equalsIgnoreCase("FIRSTNAME")) {
			bfname = true;
		}

		if (qName.equalsIgnoreCase("LASTNAME")) {
			blname = true;
		}

		if (qName.equalsIgnoreCase("NICKNAME")) {
			bnname = true;
		}

		if (qName.equalsIgnoreCase("SALARY")) {
			bsalary = true;
		}

		if(qName.equalsIgnoreCase("STOPNO")) {stopNo = true;}
		
		if(qName.equalsIgnoreCase("STOPDESCRIPTION")) {stopDescription = true;}
		
		if(qName.equalsIgnoreCase("ROUTENO")) {routeNo = true;}
		
		if(qName.equalsIgnoreCase("DIRECTION")) {routeDirection = true;}
		
		if(qName.equalsIgnoreCase("ROUTEHEADING")) {routeHeading = true;}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		//System.out.println("End Element :" + qName);
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		if (bfname) {
			System.out.println("First Name : " + new String(ch, start, length));
			bfname = false;
		}

		if (blname) {
			System.out.println("Last Name : " + new String(ch, start, length));
			blname = false;
		}

		if (bnname) {
			System.out.println("Nick Name : " + new String(ch, start, length));
			bnname = false;
		}

		if (bsalary) {
			System.out.println("Salary : " + new String(ch, start, length));
			bsalary = false;
		}
		
		if(stopNo) {
			System.out.println("StopNo : " + new String(ch, start, length));
			stopNo = false;
		}

		if(stopDescription) {
			System.out.println("StopDescription : " + new String(ch, start, length));
			stopDescription = false;
		}
		
		if(routeNo) {
			System.out.println("RouteNo : " + new String(ch, start, length));
			routeNo = false;
		}
		
		if(routeDirection) {
			System.out.println("RouteDirection : " + new String(ch, start, length));
			routeDirection = false;
		}
		
		if(routeHeading) {
			System.out.println("RouteHeading : " + new String(ch, start, length));
			routeHeading = false;
		}
	}

	@Override
	public void startDocument() {
		//System.out.println("Started reading document");
	}

	@Override
	public void endDocument() {
		//System.out.println("Finished reading document");
	}
}
