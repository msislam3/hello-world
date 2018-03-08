package testsocket;

/**
 *
 * @author Rifat Shams
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class TestSocket {

	public static void main(String argv[]) {
		try {
			Socket socketClient = new Socket("localhost", 5555);
			System.out.println("Client: " + "Connection Established");

			BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
			String serverMsg;

			Scanner input = new Scanner(System.in);

			writer.write(input.next() + "\r\n");
			input.nextLine();
			writer.write(input.next() + "\r\n");
			input.nextLine();
			writer.flush();

			while ((serverMsg = reader.readLine()) != null) {
				System.out.println("Client: " + serverMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}