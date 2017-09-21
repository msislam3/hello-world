using Microsoft.Web.WebSockets;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Service;
using System;
using System.Collections.Generic;
using System.Diagnostics;

namespace ChatApplication.Models
{
    public class ChatWebSocketHandler : WebSocketHandler
    {
        private static WebSocketCollection clients = new WebSocketCollection();
        private static List<Guid> sessions = new List<Guid>();

        private List<IContactInfo> contacts = new List<IContactInfo>();
        private List<MsgMessage> historicalLastMessages = new List<MsgMessage>();
        private List<GroupMessage> historicalGroupLastMessages = new List<GroupMessage>();
        private List<MsgMessage> historicalMessages = new List<MsgMessage>();
        private List<GroupMessage> historicalGroupMessage = new List<GroupMessage>();
        private Random rand = new Random();
        private string token;

        public ChatWebSocketHandler(string token)
        {
            this.token = token;

            contacts.Add(new RigInfo() { RigID = 1572, RigName = "Akita-91", MachineList = new List<string>() { "RIG MANAGER", "CLIENT10" } });
            contacts.Add(new UVClientInfo() { ClientLogin = "chimo", DisplayName = "chimo", Status = ContactStatus.On });

            historicalLastMessages.Add(new MsgMessage()
            {
                Type = MessageTypes.Message,
                SenderId = "CLIENT[CHIMO]",
                ReceiverId = "CLIENT[MDTADMIN]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-1).Ticks,
                Status = MessageStatus.HistoricalLast
            });

            historicalLastMessages.Add(new MsgMessage()
            {
                Type = MessageTypes.Message,
                SenderId = "CLIENT[MDTADMINTST]",
                ReceiverId = "CLIENT[MDTADMIN]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-2).Ticks,
                Status = MessageStatus.HistoricalLast
            });

            historicalGroupLastMessages.Add(new GroupMessage()
            {
                Type = MessageTypes.GroupMessage,
                SenderId = "CLIENT[MDTADMIN]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-2).Ticks,
                Status = MessageStatus.HistoricalLast,
                GroupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81"),
                ImportantMessage = true,
                GroupMembers = new List<string>() { "CLIENT[CHIMO]", "CLIENT[MDTADMIN]", "CLIENT[CHIMOTST]" }
            });

            historicalGroupLastMessages.Add(new GroupMessage()
            {
                Type = MessageTypes.GroupMessage,
                SenderId = "CLIENT[MDTADMIN]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-3).Ticks,
                Status = MessageStatus.HistoricalLast,
                GroupGuid = new Guid("AB1FDF6C-362B-421E-88A3-7DB9C48C1C81"),
                ImportantMessage = true,
                GroupMembers = new List<string>() { "CLIENT[CHIMO]", "CLIENT[MDTADMIN]" }
            });

            historicalMessages.Add(new MsgMessage()
            {
                Type = MessageTypes.Message,
                SenderId = "CLIENT[CHIMO]",
                ReceiverId = "CLIENT[MDTADMIN]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-1).Ticks,
                Status = MessageStatus.HistoricalNormal
            });

            historicalMessages.Add(new MsgMessage()
            {
                Type = MessageTypes.Message,
                SenderId = "CLIENT[CHIMO]",
                ReceiverId = "CLIENT[MDTADMIN]",
                MessageText = "Hello2",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-2).Ticks,
                Status = MessageStatus.HistoricalNormal
            });

            historicalGroupMessage.Add(new GroupMessage()
            {
                Type = MessageTypes.GroupMessage,
                SenderId = "CLIENT[CHIMO]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-1).Ticks,
                Status = MessageStatus.HistoricalNormal,
                GroupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81"),
                ImportantMessage = true,
                GroupMembers = new List<string>() { "CLIENT[CHIMO]", "CLIENT[MDTADMIN]", "CLIENT[CHIMOTST]" }
            });

            historicalGroupMessage.Add(new GroupMessage()
            {
                Type = MessageTypes.GroupMessage,
                SenderId = "CLIENT[CHIMO]",
                MessageText = "Hello2",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-2).Ticks,
                Status = MessageStatus.HistoricalNormal,
                GroupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81"),
                ImportantMessage = true,
                GroupMembers = new List<string>() { "CLIENT[CHIMO]", "CLIENT[MDTADMIN]", "CLIENT[CHIMOTST]" }
            });

        }

        public override void OnOpen()
        {
            //Send("Hello " + user + " Welcome from " + WebSocketContext.UserHostAddress);
        }

        public override void OnMessage(string message)
        {
            try
            {
                JObject jObject = JObject.Parse(message);

                string token = jObject["token"].ToObject<string>();

                if (token != this.token)
                {
                    Send(JsonConvert.SerializeObject(new MsgUnauthorized() { Type = MessageTypes.Unauthorized }));
                    return;
                }

                JToken typeToken = jObject["type"];
                //JToken valueToken = jObject["value"];

                var type = typeToken.ToObject<string>();

                if (type == MessageTypes.Register)
                {
                    var msgRegister = jObject.ToObject<MsgRegister>();
                    HandleMsgRegister(msgRegister);
                }
                else if (type == MessageTypes.Message)
                {
                    //MsgMessage msgMessage = valueToken.ToObject<MsgMessage>();
                    var msgMessage = jObject.ToObject<MsgMessage>();
                    HandleMsgMessage(msgMessage);
                }
                else if (type == MessageTypes.GroupMessage)
                {
                    var grpMessage = jObject.ToObject<GroupMessage>();
                    HandleGroupMessage(grpMessage);
                }
                else if (type == MessageTypes.LastMessageList)
                {
                    var msgRequest = jObject.ToObject<MsgRequestLastMessageList>();
                    HandleMsgRequestLastMessageList(msgRequest);
                }
                else if (type == MessageTypes.LastGroupMessageList)
                {
                    var msgRequest = jObject.ToObject<GroupMessageRequestLastMessageList>();
                    HandleMsgRequestLastMessageList(msgRequest);
                }
                else if (type == MessageTypes.HistoricalMessageList)
                {
                    var msgRequest = jObject.ToObject<MsgRequestHistoricalData>();
                    HandleMsgRequestHistoricalData(msgRequest);
                }
                else if (type == MessageTypes.HistoricalGroupMessageList)
                {
                    var msgRequest = jObject.ToObject<GroupMessageRequestHistoricalData>();
                    HandleMsgRequestHistoricalData(msgRequest);
                }
                else if (type == MessageTypes.RigConnection)
                {
                    var msgRigConnection = jObject.ToObject<MsgReqRigConnection>();
                    HandleMsgReqRigConnection(msgRigConnection);
                }
                else if (type == MessageTypes.KeepAlive)
                {
                    Trace.WriteLine("Received Heart Beat Message");
                }

                //var msgMessage = JsonConvert.DeserializeObject<MsgMessage>(message);
            }
            catch (Exception ex)
            {
                Trace.WriteLine("Exception on OnMessage " + ex);
            }
        }

        private void HandleMsgReqRigConnection(MsgReqRigConnection msgRigConnection)
        {
            if (msgRigConnection == null) throw new ArgumentNullException(nameof(msgRigConnection));
            Trace.WriteLine("Received " + msgRigConnection);
        }

        private void HandleMsgRequestHistoricalData(GroupMessageRequestHistoricalData msgRequest)
        {
            if (msgRequest == null) throw new ArgumentNullException(nameof(msgRequest));
            Trace.WriteLine("Received " + msgRequest);

            var success = rand.Next(2);

            if (success == 1)
            {
                foreach (var histMsg in historicalGroupMessage)
                {
                    histMsg.ReceiverGuid = msgRequest.SenderGuid;
                    Send(JsonConvert.SerializeObject(histMsg));
                }

                var lastMsg = new GroupMessage()
                {
                    Type = MessageTypes.GroupMessage,
                    SenderId = msgRequest.ReceiverId,
                    ReceiverGuid = msgRequest.SenderGuid,
                    Status = MessageStatus.Psuedo,
                    MessageText = $"ABOVE IS THE HISTORICAL RECORDS FOUND IN THE LAST {msgRequest.HourLimit / 24} DAYS.",
                    GroupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81")
                };
                Send(JsonConvert.SerializeObject(lastMsg));
            }
            else
            {
                var lastMsg = new GroupMessage()
                {
                    Type = MessageTypes.GroupMessage,
                    SenderId = msgRequest.ReceiverId,
                    ReceiverGuid = msgRequest.SenderGuid,
                    Status = MessageStatus.Psuedo,
                    MessageText = $"NO HISTORICAL RECORDS FOUND IN THE LAST {msgRequest.HourLimit / 24} DAYS.",
                    GroupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81")
                };
                Send(JsonConvert.SerializeObject(lastMsg));
            }
        }

        private void HandleMsgRequestHistoricalData(MsgRequestHistoricalData msgRequest)
        {
            if (msgRequest == null) throw new ArgumentNullException(nameof(msgRequest));
            Trace.WriteLine("Received " + msgRequest);

            var success = rand.Next(2);

            if (success == 1)
            {
                foreach (var histMsg in historicalMessages)
                {
                    histMsg.ReceiverGuid = msgRequest.SenderGuid;
                    Send(JsonConvert.SerializeObject(histMsg));
                }

                var lastMsg = new MsgMessage()
                {
                    Type = MessageTypes.Message,
                    SenderId = msgRequest.ReceiverId,
                    ReceiverGuid = msgRequest.SenderGuid,
                    Status = MessageStatus.Psuedo,
                    MessageText = $"ABOVE IS THE HISTORICAL RECORDS FOUND IN THE LAST {msgRequest.HourLimit / 24} DAYS."
                };
                Send(JsonConvert.SerializeObject(lastMsg));
            }
            else
            {
                var lastMsg = new MsgMessage()
                {
                    Type = MessageTypes.Message,
                    SenderId = msgRequest.ReceiverId,
                    ReceiverGuid = msgRequest.SenderGuid,
                    Status = MessageStatus.Psuedo,
                    MessageText = $"NO HISTORICAL RECORDS FOUND IN THE LAST {msgRequest.HourLimit / 24} DAYS."
                };
                Send(JsonConvert.SerializeObject(lastMsg));
            }
        }

        private void HandleMsgRequestLastMessageList(MsgRequestLastMessageList msgRequest)
        {
            if (msgRequest == null) throw new ArgumentNullException(nameof(msgRequest));
            Trace.WriteLine("Received " + msgRequest);

            foreach (var lastMsg in historicalLastMessages)
            {
                lastMsg.ReceiverGuid = msgRequest.SenderGuid;
                Send(JsonConvert.SerializeObject(lastMsg));
            }

            var grpMessageRequest = msgRequest as GroupMessageRequestLastMessageList;
            if (grpMessageRequest != null)
            {
                foreach (var lastMsg in historicalGroupLastMessages)
                {
                    lastMsg.ReceiverGuid = msgRequest.SenderGuid;
                    Send(JsonConvert.SerializeObject(lastMsg));
                }
            }
        }

        private void HandleGroupMessage(GroupMessage grpMessage)
        {
            if (grpMessage == null) throw new ArgumentNullException(nameof(grpMessage));
            Trace.WriteLine("Received " + grpMessage);

            foreach (var grpMember in grpMessage.GroupMembers)
            {
                if (grpMember.Equals(grpMessage.SenderId)) continue;

                int success = rand.Next(2);
                if (success == 1)
                {
                    var msgAcknowledge = new MsgAcknowledge()
                    {
                        Type = MessageTypes.Acknowledge,
                        SenderId = grpMember,
                        ReceiverId = grpMessage.SenderId,
                        MessageGuid = grpMessage.MessageGuid,
                        Successful = true
                    };

                    Send(JsonConvert.SerializeObject(msgAcknowledge));
                }
                else
                {
                    var msgAcknowledge = new MsgAcknowledge()
                    {
                        Type = MessageTypes.Acknowledge,
                        SenderId = grpMember,
                        ReceiverId = grpMessage.SenderId,
                        MessageGuid = grpMessage.MessageGuid,
                        Successful = false,
                        Reason = "Receiver Offline"
                    };

                    Send(JsonConvert.SerializeObject(msgAcknowledge));
                }
            }
        }

        private void HandleMsgMessage(MsgMessage msgMessage)
        {
            if (msgMessage == null) throw new ArgumentNullException(nameof(msgMessage));
            Trace.WriteLine("Received " + msgMessage);

            int success = rand.Next(2);
            if (success == 1)
            {
                var msgAcknowledge = new MsgAcknowledge()
                {
                    Type = MessageTypes.Acknowledge,
                    SenderId = msgMessage.ReceiverId,
                    ReceiverId = msgMessage.SenderId,
                    MessageGuid = msgMessage.MessageGuid,
                    Successful = true
                };

                Send(JsonConvert.SerializeObject(msgAcknowledge));

                msgMessage = new MsgMessage()
                {
                    Type = MessageTypes.Message,
                    SenderGuid = msgMessage.ReceiverGuid,
                    SenderId = msgMessage.ReceiverId,
                    ReceiverGuid = msgMessage.SenderGuid,
                    ReceiverId = msgMessage.SenderId,
                    MessageGuid = Guid.NewGuid(),
                    MessageText = "Thank you for the message",
                    TimeStamp = DateTime.Now.Ticks,
                    Status = MessageStatus.Online
                };

                Send(JsonConvert.SerializeObject(msgMessage));
            }
            else
            {
                var msgAcknowledge = new MsgAcknowledge()
                {
                    Type = MessageTypes.Acknowledge,
                    SenderId = msgMessage.ReceiverId,
                    ReceiverId = msgMessage.SenderId,
                    MessageGuid = msgMessage.MessageGuid,
                    Successful = false,
                    Reason = "Receiver Offline"
                };

                Send(JsonConvert.SerializeObject(msgAcknowledge));
            }
        }

        private void HandleMsgRegister(MsgRegister msgRegister)
        {
            if (msgRegister == null) throw new ArgumentNullException(nameof(msgRegister));
            Trace.WriteLine("Received " + msgRegister);

            if (sessions.Contains(msgRegister.SenderGuid))
            {
                Trace.WriteLine("Received Old Session " + msgRegister);
                return;
            }
            else
            {
                sessions.Add(msgRegister.SenderGuid);
                Trace.WriteLine("Received New Session " + msgRegister);
            }

            var msgAddressBook = new MsgAddressBook()
            {
                Type = MessageTypes.AddressBook,
                ContactList = contacts,
                ReceiverGuid = msgRegister.SenderGuid,
                ReceiverId = msgRegister.SenderId
            };

            Send(JsonConvert.SerializeObject(msgAddressBook));
        }

        public override void OnClose()
        {
            base.OnClose();
        }
        public override void OnError()
        {
            base.OnError();
        }
    }
}