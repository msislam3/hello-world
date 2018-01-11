using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace DummyRTClient
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                Thread.Sleep(10 * 1000);

                Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

                IPHostEntry ipHostInfo = Dns.GetHostEntry("localhost");
                IPAddress ipAddress = ipHostInfo.AddressList[1];
                IPEndPoint remoteEP = new IPEndPoint(ipAddress, 443);

                Console.WriteLine("Connecting to server on " + remoteEP);
                socket.Connect(remoteEP);
                //socket.Connect("127.0.0.1", 443);
                Console.WriteLine("Connected to server");
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }

            Console.WriteLine("Exiting");
            Console.ReadKey();
        }
    }
}
