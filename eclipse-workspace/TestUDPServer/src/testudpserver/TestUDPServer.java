package testudpserver;

/**
 * @author Rifat Shams
 */
import java.net.*;

class TestUDPServer {
	public static void main(String[] args) throws Exception {
		System.out.println("Running server");

		DatagramSocket ds = new DatagramSocket();
		byte[] buf = new byte[1024];
		InetAddress ip = InetAddress.getByName("127.0.0.1");

		String str = "*** Welcome to the Calculation Server (Addition Only) ***";
		DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, 3000);
		ds.send(dp);

		dp = new DatagramPacket(buf, 1024);
		ds.receive(dp);
		String data1 = new String(dp.getData(), 0, dp.getLength());
		
		dp = new DatagramPacket(buf, 1024);
		ds.receive(dp);
		String data2 = new String(dp.getData(), 0, dp.getLength());
		
		int num1 = Integer.parseInt(data1);
		int num2 = Integer.parseInt(data2);

		int result = num1 + num2;

		str = "Addition operation done ";
		dp = new DatagramPacket(str.getBytes(), str.length(), ip, 3000);
		ds.send(dp);

		str = "Result is  : " + result;
		dp = new DatagramPacket(str.getBytes(), str.length(), ip, 3000);
		ds.send(dp);

		ds.close();
	}
}