using Microsoft.AspNet.SignalR;
using Newtonsoft.Json;
using Service;
using System;
using System.Collections.Generic;
using System.Diagnostics;

namespace SignalRChat
{
    public class ChatHub : Hub
    {
        private static List<Guid> sessions = new List<Guid>();
        private List<IContactInfo> contacts = new List<IContactInfo>();
        private Random rand = new Random();
        private List<MsgMessage> historicalLastMessages = new List<MsgMessage>();
        private List<MsgMessage> historicalMessages = new List<MsgMessage>();
        private List<GroupMessage> historicalGroupMessage = new List<GroupMessage>();

        public ChatHub()
        {
            contacts.Add(new RigInfo() { RigID = 1572, RigName = "Akita-91", MachineList = new List<string>() { "RIG MANAGER", "CLIENT10" } });
            contacts.Add(new UVClientInfo() { ClientLogin = "chimo", DisplayName = "chimo", Status = ContactStatus.On });

            historicalLastMessages.Add(new MsgMessage()
            {
                Type = "Message",
                SenderId = "CLIENT[CHIMO]",
                ReceiverId = "CLIENT[MDTADMIN]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-1).Ticks,
                Status = MessageStatus.HistoricalLast
            });

            historicalLastMessages.Add(new MsgMessage()
            {
                Type = "Message",
                SenderId = "CLIENT[MDTADMINTST]",
                ReceiverId = "CLIENT[MDTADMIN]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-2).Ticks,
                Status = MessageStatus.HistoricalLast
            });

            historicalMessages.Add(new MsgMessage()
            {
                Type = "Message",
                SenderId = "CLIENT[CHIMO]",
                ReceiverId = "CLIENT[MDTADMIN]",
                MessageText = "Hello1",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-1).Ticks,
                Status = MessageStatus.HistoricalNormal
            });

            historicalMessages.Add(new MsgMessage()
            {
                Type = "Message",
                SenderId = "CLIENT[CHIMO]",
                ReceiverId = "CLIENT[MDTADMIN]",
                MessageText = "Hello2",
                MessageGuid = Guid.NewGuid(),
                TimeStamp = DateTime.UtcNow.AddMonths(-2).Ticks,
                Status = MessageStatus.HistoricalNormal
            });

            historicalGroupMessage.Add(new GroupMessage()
            {
                Type = "Message",
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
                Type = "Message",
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

        public void Send(string name, string message)
        {
            Trace.WriteLine($"Send: {name} {message}");

            // Call the broadcastMessage method to update clients.
            Clients.All.broadcastMessage(name, message);
        }

        public void SendMsgRegister(MsgRegister msgRegister)
        {
            Trace.WriteLine("Received " + msgRegister);

            if (sessions.Contains(msgRegister.SenderGuid))
            {
                Trace.WriteLine("Received Old Session " + msgRegister);
                //return;
            }
            else
            {
                sessions.Add(msgRegister.SenderGuid);
                Trace.WriteLine("Received New Session " + msgRegister);
            }

            MsgAddressBook msgAddressBook = new MsgAddressBook()
            {
                Type = "AddressBook",
                ContactList = contacts,
                ReceiverGuid = msgRegister.SenderGuid,
                ReceiverId = msgRegister.SenderId
            };

            Clients.Caller.broadcastMessage(msgRegister.SenderId, "Received Register");

            //This message is not going
            //Clients.Caller.sendAddressbook(msgAddressBook);
            Clients.Caller.sendAddressbook(JsonConvert.SerializeObject(msgAddressBook));
        }

        public void SendMsgMessage(MsgMessage msgMessage)
        {
            Trace.WriteLine("Received " + msgMessage);

            int success = rand.Next(2);
            if (success == 1)
            {
                MsgAcknowledge msgAcknowledge = new MsgAcknowledge()
                {
                    Type = "Acknowledge",
                    SenderId = msgMessage.ReceiverId,
                    ReceiverId = msgMessage.SenderId,
                    MessageGuid = msgMessage.MessageGuid,
                    Successful = true
                };

                Clients.Caller.sendAckMessage(msgAcknowledge);

                msgMessage = new MsgMessage()
                {
                    Type = "Message",
                    SenderGuid = msgMessage.ReceiverGuid,
                    SenderId = msgMessage.ReceiverId,
                    ReceiverGuid = msgMessage.SenderGuid,
                    ReceiverId = msgMessage.SenderId,
                    MessageGuid = Guid.NewGuid(),
                    MessageText = "Thank you for the message",
                    TimeStamp = DateTime.Now.Ticks,
                    Status = MessageStatus.Online
                };

                Clients.Caller.sendMsgMessage(msgMessage);
            }
            else
            {
                MsgAcknowledge msgAcknowledge = new MsgAcknowledge()
                {
                    Type = "Acknowledge",
                    SenderId = msgMessage.ReceiverId,
                    ReceiverId = msgMessage.SenderId,
                    MessageGuid = msgMessage.MessageGuid,
                    Successful = false,
                    Reason = "Receiver Offline"
                };

                Clients.Caller.sendAckMessage(msgAcknowledge);
            }
        }

        public void SendMsgRequestHistoricalData(MsgRequestHistoricalData msgRequest)
        {
            Trace.WriteLine("Received " + msgRequest);

            var success = rand.Next(2);

            if (success == 1)
            {
                foreach (var histMsg in historicalMessages)
                {
                    histMsg.ReceiverGuid = msgRequest.SenderGuid;
                    Clients.Caller.sendMsgMessage(histMsg);
                }

                var lastMsg = new MsgMessage() { Type = "Message", ReceiverId = "CLIENT[MDTADMIN]", Status = MessageStatus.Psuedo, MessageText = $"ABOVE IS THE HISTORICAL RECORDS FOUND IN THE LAST {msgRequest.HourLimit / 24} DAYS." };
                Clients.Caller.sendMsgMessage(lastMsg);
            }
            else
            {
                var lastMsg = new MsgMessage() { Type = "Message", ReceiverId = "CLIENT[MDTADMIN]", Status = MessageStatus.Psuedo, MessageText = $"NO HISTORICAL RECORDS FOUND IN THE LAST {msgRequest.HourLimit / 24} DAYS." };
                Clients.Caller.sendMsgMessage(lastMsg);
            }
        }

        public void SendGroupMsgRequestHistoricalData(GroupMessageRequestHistoricalData msgRequest)
        {
            Trace.WriteLine("Received " + msgRequest);

            var success = rand.Next(2);

            if (success == 1)
            {
                foreach (var histMsg in historicalGroupMessage)
                {
                    histMsg.ReceiverGuid = msgRequest.SenderGuid;
                    Clients.Caller.sendGroupMessage(histMsg);
                }

                var lastMsg = new GroupMessage()
                {
                    Type = "Message",
                    Status = MessageStatus.Psuedo,
                    MessageText = $"ABOVE IS THE HISTORICAL RECORDS FOUND IN THE LAST {msgRequest.HourLimit / 24} DAYS.",
                    GroupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81")
                };
                Clients.Caller.sendGroupMessage(lastMsg);
            }
            else
            {
                var lastMsg = new GroupMessage()
                {
                    Type = "Message",
                    ReceiverId = "CLIENT[MDTADMIN]",
                    Status = MessageStatus.Psuedo,
                    MessageText = $"NO HISTORICAL RECORDS FOUND IN THE LAST {msgRequest.HourLimit / 24} DAYS.",
                    GroupGuid = new Guid("AE1FDF6C-362B-421E-88A3-7DB9C48C1C81")
                };
                Clients.Caller.sendGroupMessage(lastMsg);
            }
        }

        public void SendMsgRequestLastMessageList(MsgRequestLastMessageList msgRequest)
        {
            Trace.WriteLine("Received " + msgRequest);

            foreach (var lastMsg in historicalLastMessages)
            {
                lastMsg.ReceiverGuid = msgRequest.SenderGuid;
                Clients.Caller.sendMsgMessage(lastMsg);
            }
        }

        public void SendGroupMessage(GroupMessage grpMessage)
        {
            Trace.WriteLine("Received " + grpMessage);

            foreach (var grpMember in grpMessage.GroupMembers)
            {
                if (grpMember.Equals(grpMessage.SenderId)) continue;

                int success = rand.Next(2);
                if (success == 1)
                {
                    MsgAcknowledge msgAcknowledge = new MsgAcknowledge()
                    {
                        Type = "Acknowledge",
                        SenderId = grpMember,
                        ReceiverId = grpMessage.SenderId,
                        MessageGuid = grpMessage.MessageGuid,
                        Successful = true
                    };

                    Clients.Caller.sendAckMessage(msgAcknowledge);
                }
                else
                {
                    MsgAcknowledge msgAcknowledge = new MsgAcknowledge()
                    {
                        Type = "Acknowledge",
                        SenderId = grpMember,
                        ReceiverId = grpMessage.SenderId,
                        MessageGuid = grpMessage.MessageGuid,
                        Successful = false,
                        Reason = "Receiver Offline"
                    };

                    Clients.Caller.sendAckMessage(msgAcknowledge);
                }
            }
        }
    }
}