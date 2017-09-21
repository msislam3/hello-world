using Microsoft.AspNet.SignalR.Client;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;

namespace SignalRChatClient
{
    class Program
    {
        private static readonly string clientId = "CLIENT[MDTADMIN]";
        private static readonly Guid clientGuid = new Guid("B68E1FE5-B860-4E06-B034-11F511AA4E2B");
        private static readonly Guid receiverGuid = new Guid("DE1FDF6C-362B-421E-88A3-7DB9C48C1C81");
        private static readonly Guid groupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81");
        private static readonly string receiverId = "CLIENT[CHIMO]";

        static void Main(string[] args)
        {
            IHubProxy hub;
            string url = @"http://localhost:51611";
            var connection = new HubConnection(url);
            //connection.Credentials = new NetworkCredential("chimo", "superman58");
            hub = connection.CreateHubProxy("ChatHub");
            connection.Start().Wait();

            hub.Invoke("Send", clientId, "Hello World").Wait();

            hub.On<string, string>("broadcastMessage", OnBroadcastMessage);
            //hub.On<MsgAddressBook>("sendAddressbook", OnAddressbook);
            hub.On<string>("sendAddressbook", OnAddressbook);
            hub.On<MessageBase>("sendMessage", OnMessage);
            hub.On<MsgMessage>("sendMsgMessage", OnMsgMessage);
            hub.On<MsgAcknowledge>("sendAckMessage", OnAckMessage);
            hub.On<GroupMessage>("sendGroupMessage", OnGroupMessage);

            Console.Clear();
            Console.WriteLine("SignalR Chat CLient");
            Console.WriteLine();
            Console.WriteLine("[1] Send Register Message");
            Console.WriteLine("[2] Send Chat");
            Console.WriteLine("[3] Send Historical Message Request");
            Console.WriteLine("[4] Send Historical Last Message List");
            Console.WriteLine("[5] Send Group Message");
            Console.WriteLine("[6] Send Group Historical Message Request");
            Console.WriteLine();
            Console.WriteLine("[X] Exit");
            Console.WriteLine();

            while (true)
            {
                Console.Write("Enter Option: ");

                var option = Console.ReadKey().KeyChar;

                Console.WriteLine();

                switch (option)
                {
                    case '1':
                        SendRegisterMessage(hub);
                        break;
                    case '2':
                        SendMessage(hub);
                        break;
                    case '3':
                        SendHistoricalMessageRequest(hub);
                        break;
                    case '4':
                        SendHistoricalLastMessageRequest(hub);
                        break;
                    case '5':
                        SendGroupMessage(hub);
                        break;
                    case '6':
                        SendGroupHistoricalMessageRequest(hub);
                        break;
                    case 'x':
                        connection.Stop();
                        Environment.Exit(0);
                        break;
                    default:
                        Console.WriteLine("Invalid choice. Try again.");
                        break;
                }
            }
        }

        private static void OnAddressbook(string obj)
        {
            var adddressBook = JsonConvert.DeserializeObject<MsgAddressBook>(obj);
            Console.WriteLine($"Received address book {adddressBook}");
        }

        private static void OnMessage(MessageBase msg)
        {
            Console.WriteLine($"Received message {msg}");

            if (msg is MsgAddressBook)
            {
                OnAddressbook((MsgAddressBook)msg);
            }
            else if (msg is MsgMessage)
            {
                OnMsgMessage((MsgMessage)msg);
            }
        }

        private static void OnAckMessage(MsgAcknowledge msg)
        {
            Console.WriteLine($"Received ack message {msg}");
        }

        private static void OnGroupMessage(GroupMessage msg)
        {
            Console.WriteLine($"Received group chat message {msg}");
        }

        private static void OnMsgMessage(MsgMessage msg)
        {
            Console.WriteLine($"Received chat message {msg}");
        }

        private static void OnAddressbook(MsgAddressBook addressBook)
        {
            Console.WriteLine($"Received address book message {addressBook}");
        }

        private static void SendGroupHistoricalMessageRequest(IHubProxy hub)
        {
            Console.WriteLine("Sending Group Historical Message Request");
            GroupMessageRequestHistoricalData msgRequest = new GroupMessageRequestHistoricalData()
            {
                Type = "HistoricalGroupMessageList",
                SenderGuid = clientGuid,
                SenderId = clientId,
                HourLimit = 24,
                GroupGuid = groupGuid
            };
            hub.Invoke("SendGroupMsgRequestHistoricalData", msgRequest).Wait();
        }

        private static void SendGroupMessage(IHubProxy hub)
        {
            Console.WriteLine("Sending Group Message");
            GroupMessage msg = new GroupMessage()
            {
                Type = "GroupMessage",
                SenderGuid = clientGuid,
                SenderId = clientId,
                MessageGuid = Guid.NewGuid(),
                MessageText = "Hello Group",
                TimeStamp = DateTime.UtcNow.Ticks,
                Status = MessageStatus.Online,
                GroupGuid = groupGuid,
                ImportantMessage = true,
                GroupMembers = new List<string>() { "CLIENT[CHIMO]", "CLIENT[MDTADMIN]", "CLIENT[CHIMOTST]" }
            };
            hub.Invoke("SendGroupMessage", msg).Wait();
        }

        private static void SendHistoricalLastMessageRequest(IHubProxy hub)
        {
            Console.WriteLine("Sending Historical Last Message Request");
            MsgRequestLastMessageList msgRequest = new MsgRequestLastMessageList()
            {
                Type = "LastMessageList",
                SenderGuid = clientGuid,
                SenderId = clientId
            };
            hub.Invoke("SendMsgRequestLastMessageList", msgRequest).Wait();
        }

        private static void SendHistoricalMessageRequest(IHubProxy hub)
        {
            Console.WriteLine("Sending Historical Message Request");
            MsgRequestHistoricalData msgRequest = new MsgRequestHistoricalData()
            {
                Type = "HistoricalMessageList",
                SenderGuid = clientGuid,
                SenderId = clientId,
                ReceiverId = receiverId,
                HourLimit = 24
            };
            hub.Invoke("SendMsgRequestHistoricalData", msgRequest).Wait();
        }

        private static void SendRegisterMessage(IHubProxy hub)
        {
            Console.WriteLine("Sending Register Message");
            MsgRegister msgRegister = new MsgRegister()
            {
                Type = "Register",
                SenderGuid = clientGuid,
                SenderId = clientId
            };
            hub.Invoke("SendMsgRegister", msgRegister).Wait();
        }

        private static void SendMessage(IHubProxy hub)
        {
            Console.WriteLine("Sending  Chat Message");
            MsgMessage msgMessage = new MsgMessage()
            {
                Type = "Message",
                SenderGuid = clientGuid,
                SenderId = clientId,
                ReceiverGuid = receiverGuid,
                ReceiverId = receiverId,
                MessageGuid = Guid.NewGuid(),
                MessageText = "Hello World",
                TimeStamp = DateTime.UtcNow.Ticks,
                Status = MessageStatus.Online
            };
            hub.Invoke("SendMsgMessage", msgMessage).Wait();
        }

        private static void OnBroadcastMessage(string name, string message)
        {
            Console.WriteLine($"Received broadcast message {name} {message}");
        }
    }
}
