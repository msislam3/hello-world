using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace SignalRChatClient
{
    public class Message
    {
        [JsonProperty("type")]
        public string Type { get; set; }
        [JsonProperty("value")]
        public MessageBase Value { get; set; }
    }

    public class MessageBase
    {
        /*SenderGuid and ReceiverGuid are used to identify a specific client instance. One user can have multiple chat 
         * clients running each identified by a specific Guid. When a chat client sends a message it sets the SenderGuid to its own 
         * Guid. It also sets the ReceiverGuid to the specific instance of the receiver. If ReceiverGuid is absent, the message is sent 
         * to all client instance of the receiver*/

        [JsonProperty("type")]
        public string Type { get; set; }
        [JsonProperty("senderGuid")]
        public Guid SenderGuid { get; set; }
        [JsonProperty("senderId")]
        public string SenderId { get; set; }
        [JsonProperty("receiverGuid")]
        public Guid ReceiverGuid { get; set; }
        [JsonProperty("receiverId")]
        public string ReceiverId { get; set; }
    }

    public enum MessageStatus
    {
        Online,
        Offline,
        HistoricalNormal,
        HistoricalLast,
        Psuedo
    }

    public class MsgMessage : MessageBase
    {
        [JsonProperty("messageStatus")]
        public MessageStatus Status { get; set; }
        [JsonProperty("messageGuid")]
        public Guid MessageGuid { get; set; }
        [JsonProperty("messageText")]
        public string MessageText { get; set; }
        [JsonProperty("timeStamp")]
        public long TimeStamp { get; set; }
        public override string ToString()
        {
            return $"{Type} SenderID {SenderId} ReceiverID {ReceiverId} MsgGuid:  {MessageGuid}  Status: {Status} Text: {MessageText} Time: {new DateTime(TimeStamp).ToShortTimeString()}";
        }
    }

    public class GroupMessage : MsgMessage
    {
        [JsonProperty("groupGuid")]
        public Guid GroupGuid { get; set; }
        [JsonProperty("groupMembers")]
        public List<string> GroupMembers { get; set; }
        [JsonProperty("importantMessage")]
        public bool ImportantMessage { get; set; }

        public override string ToString()
        {
            var builder = new StringBuilder();

            builder.AppendFormat("GroupGUID={0}, Important Message={1}, ", GroupGuid, ImportantMessage);

            if (GroupMembers != null && GroupMembers.Count > 0)
            {
                builder.AppendFormat(" Members={0},", string.Join(", ", GroupMembers.ToArray()));
            }

            return $"{base.ToString()} Members: {builder.ToString()}";
        }
    }

    public class MsgAcknowledge : MessageBase
    {
        [JsonProperty("messageGuid")]
        public Guid MessageGuid { get; set; }
        [JsonProperty("successful")]
        public bool Successful { get; set; }
        [JsonProperty("reason")]
        public string Reason { get; set; }

        public override string ToString()
        {
            return $"{Type} MessageGuid: {MessageGuid} Success: {Successful} Reason: {Reason}";
        }
    }

    public class MsgAddressBook : MessageBase
    {
        [JsonProperty("contacts")]
        public List<BaseContactInfo> ContactList { get; set; }

        public override string ToString()
        {
            return $"{Type}";
        }
    }

    public class BaseContactInfo
    {
        [JsonProperty("displayName")]
        string DisplayName { get; set; }
        [JsonProperty("contactId")]
        string ContactId { get; set; }
        [JsonProperty("status")]
        ContactStatus Status { get; set; }
    }

    public enum ContactStatus
    {
        On,
        Off
    }

    public class RigInfo : BaseContactInfo
    {
        public RigInfo()
        {
            MachineList = new List<string>();
        }

        public string DisplayName { get; set; }

        public string ContactId { get; set; }

        public ContactStatus Status
        {
            get
            {
                if (MachineList.Count == 0)
                {
                    return ContactStatus.Off;
                }
                else
                {
                    return ContactStatus.On;
                }
            }
            set
            {
                throw new NotSupportedException();
            }
        }

        [JsonProperty("machineList")]
        public List<string> MachineList { get; set; }
    }

    public class UVClientInfo : BaseContactInfo
    {
        public string DisplayName { get; set; }

        public string ContactId { get; set; }

        public ContactStatus Status { get; set; }
    }

    public class MsgRegister : MessageBase
    {
        public override string ToString()
        {
            return $"{Type} ID {SenderId} GUID {SenderGuid}";
        }
    }

    public class MsgRequestLastMessageList : MessageBase
    {
        public override string ToString()
        {
            return $"Type: {Type}";
        }
    }

    public class MsgRequestHistoricalData : MessageBase
    {
        [JsonProperty("hourLimit")]
        public int HourLimit { get; set; }

        public override string ToString()
        {
            return $"Type: {Type}";
        }
    }

    public class GroupMessageRequestHistoricalData : MsgRequestHistoricalData
    {
        [JsonProperty("groupGuid")]
        public Guid GroupGuid { get; set; }
    }

    public class MsgReqRigConnection : MessageBase
    {
        public override string ToString()
        {
            return $"Type: {Type} ReceiverID: {ReceiverId}";
        }
    }
}
