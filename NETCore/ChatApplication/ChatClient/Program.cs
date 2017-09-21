using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Service;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Threading.Tasks;
using System.Timers;
using WebSocketSharp;


namespace ChatClient
{
    class Program
    {
        private static readonly string clientLogin = "mdtadmin";
        private static readonly string password = "password";
        private static readonly string clientId = ContactInfoHelper.CreateClientContactID(clientLogin);
        private static readonly Guid clientGuid = new Guid("B68E1FE5-B860-4E06-B034-11F511AA4E2B");
        private static readonly Guid receiverGuid = new Guid("DE1FDF6C-362B-421E-88A3-7DB9C48C1C81");
        private static readonly Guid groupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81");
        private static readonly string receiverId = "CLIENT[CHIMO]";
        private static string token;

        private static WebSocket ws;

        static void Main(string[] args)
        {
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create("http://localhost:50222/api/Chat/token");
            request.Method = "Get";
            String encoded = Convert.ToBase64String(System.Text.Encoding.GetEncoding("ISO-8859-1").GetBytes(clientLogin + ":" + password));
            request.Headers.Add("Authorization", "Basic " + encoded);

            using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
            {
                Stream dataStream = response.GetResponseStream();
                StreamReader reader = new StreamReader(dataStream);
                String result = reader.ReadToEnd();
                token = JsonConvert.DeserializeObject<TokenContainer>(result).Token;
                reader.Close();
                dataStream.Close();
            }

            Connect().Wait();
        }

        public static async Task Connect()
        {
            using (ws = new WebSocket("ws://localhost:50222/api/Chat/socket", onMessage: OnMessage, onError: OnError))
            {
                bool result = await ws.Connect();

                if (result)
                {
                    Timer timer = new Timer();
                    timer.AutoReset = true;
                    timer.Interval = 1000 * 60;
                    timer.Elapsed += Timer_Elapsed;
                    timer.Start();

                    Console.Clear();
                    Console.WriteLine("WebSocket Chat CLient");
                    Console.WriteLine();
                    Console.WriteLine("[1] Send Register Message");
                    Console.WriteLine("[2] Send Chat");
                    Console.WriteLine("[3] Send Historical Message Request");
                    Console.WriteLine("[4] Send Historical Last Message List Request");
                    Console.WriteLine("[5] Send Group Message");
                    Console.WriteLine("[6] Send Group Historical Message Request");
                    Console.WriteLine("[7] Send Historical Last Group Message List Request");
                    Console.WriteLine("[8] Send Rig Connection Request");
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
                                await SendRegisterMessage();
                                break;
                            case '2':
                                await SendMessage();
                                break;
                            case '3':
                                await SendHistoricalMessageRequest();
                                break;
                            case '4':
                                await SendHistoricalLastMessageRequest();
                                break;
                            case '5':
                                await SendGroupMessage();
                                break;
                            case '6':
                                await SendGroupHistoricalMessageRequest();
                                break;
                            case '7':
                                await SendGroupHistoricalLastMessageListRequest();
                                break;
                            case '8':
                                await SendRigConnectionRequest();
                                break;
                            case 'x':
                                await ws.Close(CloseStatusCode.Normal);
                                Environment.Exit(0);
                                break;
                            default:
                                Console.WriteLine("Invalid choice. Try again.");
                                break;
                        }
                    }
                }
                else
                {
                    Console.WriteLine("Press any key to exit");
                    Console.ReadKey();
                }
            }
        }

        private static Task OnError(WebSocketSharp.ErrorEventArgs arg)
        {
            Console.WriteLine($"{arg.Exception} {arg.Message}");

            return Task.CompletedTask;
        }

        private static void Timer_Elapsed(object sender, ElapsedEventArgs e)
        {
            Console.WriteLine("Sending Keep Alive Message");
            var keepAlive = new MsgHeartBeat() { Token = token, Type = MessageTypes.KeepAlive };

            ws.Send(JsonConvert.SerializeObject(keepAlive));
        }

        private static async Task SendRigConnectionRequest()
        {
            Console.WriteLine("Sending Rig Connection Request");
            var msgRequest = new MsgReqRigConnection()
            {
                Token = token,
                Type = MessageTypes.RigConnection,
                ReceiverId = "RIG[1572]"
            };

            await ws.Send(JsonConvert.SerializeObject(msgRequest));
        }

        private static async Task SendGroupHistoricalMessageRequest()
        {
            Console.WriteLine("Sending Group Historical Message Request");
            var msgRequest = new GroupMessageRequestHistoricalData()
            {
                Token = token,
                Type = MessageTypes.HistoricalGroupMessageList,
                SenderGuid = clientGuid,
                SenderId = clientId,
                HourLimit = 24,
                GroupGuid = groupGuid
            };

            await ws.Send(JsonConvert.SerializeObject(msgRequest));
        }

        private static async Task SendGroupMessage()
        {
            Console.WriteLine("Sending Group Message");
            var msg = new GroupMessage()
            {
                Token = token,
                Type = MessageTypes.GroupMessage,
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

            await ws.Send(JsonConvert.SerializeObject(msg));
        }

        private static async Task SendGroupHistoricalLastMessageListRequest()
        {
            Console.WriteLine("Sending Group Historical Last Message Request");
            var msgRequest = new GroupMessageRequestLastMessageList()
            {
                Token = token,
                Type = MessageTypes.LastGroupMessageList,
                SenderGuid = clientGuid,
                SenderId = clientId,
                GroupGuids = new List<Guid>() { new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81"), new Guid("AB1FDF6C-362B-421E-88A3-7DB9C48C1C81") }
            };

            await ws.Send(JsonConvert.SerializeObject(msgRequest));
        }

        private static async Task SendHistoricalLastMessageRequest()
        {
            Console.WriteLine("Sending Historical Last Message Request");
            var msgRequest = new MsgRequestLastMessageList()
            {
                Token = token,
                Type = MessageTypes.LastMessageList,
                SenderGuid = clientGuid,
                SenderId = clientId
            };

            await ws.Send(JsonConvert.SerializeObject(msgRequest));
        }

        private static async Task SendHistoricalMessageRequest()
        {
            if (ws.ReadyState == WebSocketState.Open)
            {
                Console.WriteLine("Sending Historical Message Request");
                var msgRequest = new MsgRequestHistoricalData()
                {
                    Token = token,
                    Type = MessageTypes.HistoricalMessageList,
                    SenderGuid = clientGuid,
                    SenderId = clientId,
                    ReceiverId = receiverId,
                    HourLimit = 24
                };

                await ws.Send(JsonConvert.SerializeObject(msgRequest));
            }
        }

        private static async Task SendMessage()
        {
            if (ws.ReadyState == WebSocketState.Open)
            {
                Console.WriteLine("Sending Chat Message");
                var msgMessage = new MsgMessage()
                {
                    Token = token,
                    Type = MessageTypes.Message,
                    SenderGuid = clientGuid,
                    SenderId = clientId,
                    ReceiverGuid = receiverGuid,
                    ReceiverId = receiverId,
                    MessageGuid = Guid.NewGuid(),
                    MessageText = "Hello World",
                    TimeStamp = DateTime.UtcNow.Ticks,
                    Status = MessageStatus.Online
                };

                await ws.Send(JsonConvert.SerializeObject(msgMessage));
            }
        }

        private static async Task SendRegisterMessage()
        {
            if (ws.ReadyState == WebSocketState.Open)
            {
                Console.WriteLine("Sending Register Message");
                var msgRegister = new MsgRegister()
                {
                    Token = token,
                    Type = MessageTypes.Register,
                    SenderGuid = clientGuid,
                    SenderId = clientId
                };

                await ws.Send(JsonConvert.SerializeObject(msgRegister));
            }
        }

        public static async Task OnMessage(MessageEventArgs message)
        {
            if (message.Opcode == Opcode.Text)
            {
                using (message.Text)
                {
                    try
                    {
                        var result = await message.Text.ReadToEndAsync();

                        JObject jObject = JObject.Parse(result);
                        JToken typeToken = jObject["type"];
                        string type = typeToken.ToObject<string>();

                        if (type.Equals("AddressBook"))
                        {
                            var msgAddressBook = JsonConvert.DeserializeObject<MsgAddressBook>(result, new ContactInfoConverter());

                            HandleAddressBook(msgAddressBook);
                        }
                        else if (type.Equals("Acknowledge"))
                        {
                            var msgAcknowledge = jObject.ToObject<MsgAcknowledge>();
                            HandleMsgAcknowledge(msgAcknowledge);
                        }
                        else if (type.Equals("Message"))
                        {
                            var msgMessage = jObject.ToObject<MsgMessage>();
                            HandleMsgMessage(msgMessage);
                        }
                        else if (type.Equals("GroupMessage"))
                        {
                            var grpMessage = jObject.ToObject<GroupMessage>();
                            HandleGroupMessage(grpMessage);
                        }
                        else
                        {
                            Console.WriteLine(result);
                        }
                    }
                    catch (Exception ex)
                    {
                        Trace.WriteLine("Client Exception " + ex);
                    }
                }
            }
        }

        private static void HandleGroupMessage(GroupMessage grpMessage)
        {
            if (grpMessage == null) throw new ArgumentNullException(nameof(grpMessage));
            Trace.WriteLine("Client Received " + grpMessage);
        }

        private static void HandleMsgMessage(MsgMessage msgMessage)
        {
            if (msgMessage == null) throw new ArgumentNullException(nameof(msgMessage));
            Trace.WriteLine("Client Received " + msgMessage);

            var msgAcknowledge = new MsgAcknowledge()
            {
                Token = token,
                Type = MessageTypes.Acknowledge,
                SenderId = msgMessage.ReceiverId,
                ReceiverId = msgMessage.SenderId,
                MessageGuid = msgMessage.MessageGuid,
                Successful = true
            };

            ws.Send(JsonConvert.SerializeObject(msgAcknowledge));
        }

        private static void HandleMsgAcknowledge(MsgAcknowledge msgAcknowledge)
        {
            if (msgAcknowledge == null) throw new ArgumentNullException(nameof(msgAcknowledge));
            Trace.WriteLine("Client Received " + msgAcknowledge);
        }

        private static void HandleAddressBook(MsgAddressBook msgAddressBook)
        {
            if (msgAddressBook == null) throw new ArgumentNullException(nameof(msgAddressBook));
            Trace.WriteLine("Client Received " + msgAddressBook);
        }
    }

    /*
     * By default JSonSerialized .Net objects don't have type informations so while deserializing subclasses, it is deserialized as base class.
     * One way to solve this is to include the type information by using TypeNameHandling.All /Auto while serializing/deserializing. But it includes
     * type name for all classes that bloats the data and also requires the same class to be used both in serialization and deserialization.
     * The other way is to to implement a Custom JsonConverter for the particular base class in deserialization. The converter can create a specific version
     * of subclass object from the JSon string based on some information in the overriden ReadJSon method. Link
     * https://stackoverflow.com/a/17501107/1841089
     * or better https://stackoverflow.com/a/8031283/1841089
     * 
     * In our case, based on the presence of machineList property, the converter is creating RigInfo or UVClientInfo class
     */
    public class ContactInfoConverter : JsonConverter
    {
        public override bool CanWrite => false;

        public override bool CanConvert(Type objectType)
        {
            return typeof(IContactInfo).IsAssignableFrom(objectType);
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            JObject jObject = JObject.Load(reader);

            if (jObject.TryGetValue("machineList", out JToken machineList))
            {
                RigInfo rigInfo = new RigInfo();
                serializer.Populate(jObject.CreateReader(), rigInfo);
                return rigInfo;
            }
            else
            {
                UVClientInfo clientInfo = new UVClientInfo();
                serializer.Populate(jObject.CreateReader(), clientInfo);
                return clientInfo;
            }
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            throw new NotImplementedException();
        }
    }
}
