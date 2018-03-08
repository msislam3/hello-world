package testudp;

/**
 * @author Rifat Shams
 */
import java.io.*;
import java.net.*;

public class TestUDP {

	public static void main(String argv[]) throws IOException {
		System.out.println("Running Client");

		DatagramSocket ds = new DatagramSocket(3000);
		byte[] buf = new byte[1024];

		DatagramPacket dp = new DatagramPacket(buf, 1024);
		ds.receive(dp);
		String str = new String(dp.getData(), 0, dp.getLength());
		System.out.println(str);

		str = "12";
		DatagramPacket dpOut = new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
		ds.send(dpOut);

		str = "14";
		dpOut = new DatagramPacket(str.getBytes(), str.length(), dp.getAddress(), dp.getPort());
		ds.send(dpOut);

		dp = new DatagramPacket(buf, 1024);
		ds.receive(dp);
		str = new String(dp.getData(), 0, dp.getLength());
		System.out.println(str);

		dp = new DatagramPacket(buf, 1024);
		ds.receive(dp);
		str = new String(dp.getData(), 0, dp.getLength());
		System.out.println(str);

		ds.close();
	}
}