using System;
using System.Net;
using System.Net.Sockets;

namespace DummyRTServer
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                Socket m_sListeningSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

                IPHostEntry ipHostInfo = Dns.GetHostEntry("localhost");
                IPAddress ipAddress = ipHostInfo.AddressList[1];
                IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 443);

                Console.WriteLine("Binding to " + localEndPoint);

                m_sListeningSocket.Bind(localEndPoint);

                //m_sListeningSocket.Bind(new IPEndPoint(IPAddress.Parse("127.0.0.1"), 443));
                m_sListeningSocket.Listen(30);
                Console.WriteLine("Bind to port");

                while (true)
                {
                    Console.WriteLine("Waiting for connection");
                    Socket connection = m_sListeningSocket.Accept();
                    Console.WriteLine("Socket Accepted");
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }
        }
    }
}
