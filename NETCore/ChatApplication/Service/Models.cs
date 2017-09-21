using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace Service
{
    public class TokenContainer
    {
        public string Token { get; set; }
    }

    public enum ContactType
    {
        Rig = 1,
        Machine = 2,
        UVClient = 3,
        Unknown = -1
    }

    public enum MessageStatus
    {
        //Receiving client On-line
        Online,
        //Receiving client off-line
        Offline,
        //Historical message
        HistoricalNormal,
        //Last message of a conversation
        HistoricalLast,
        //End marker of a historical conversation stream
        Psuedo
    }

    public class MessageTypes
    {
        public static string Register = "Register";
        public static string AddressBook = "AddressBook";
        public static string Message = "Message";
        public static string GroupMessage = "GroupMessage";
        public static string Acknowledge = "Acknowledge";
        public static string LastMessageList = "LastMessageList";
        public static string LastGroupMessageList = "LastGroupMessageList";
        public static string HistoricalMessageList = "HistoricalMessageList";
        public static string HistoricalGroupMessageList = "HistoricalGroupMessageList";
        public static string RigConnection = "RigConnction";
        public static string KeepAlive = "KeepALive";
        public static string Unauthorized = "Unauthorized";
    }

    public class Base
    {
        /// <summary>
        /// The authorization token. Clients will need to get the authorization token before starting the websocket and need to
        /// include the token in each messages
        /// </summary>
        [JsonProperty("token")]
        public string Token { get; set; }

        /// <summary>
        /// Websocket does not support transferring subclasses directly. So need to send type information in the message which will be
        /// used to deserialize the subclass of MessageBase properly
        /// </summary>
        [JsonProperty("type")]
        public string Type { get; set; }
    }

    public class MessageBase : Base
    {
        /// <summary>
        /// SenderGuid and ReceiverGuid are used to identify a specific client instance.One user can have multiple chat
        /// clients running each identified by a specific Guid.When a chat client sends a message it sets the SenderGuid to its own
        /// Guid. It also sets the ReceiverGuid to the specific instance of the receiver. If ReceiverGuid is absent, the message is sent
        /// to all client instance of the receiver
        /// </summary>
        [JsonProperty("senderGuid")]
        public Guid SenderGuid { get; set; }

        /// <summary>
        /// Contact ID of the Sender
        /// </summary>
        [JsonProperty("senderId")]
        public string SenderId { get; set; }

        /// <summary>
        /// Client guid for the receiver
        /// </summary>
        [JsonProperty("receiverGuid")]
        public Guid ReceiverGuid { get; set; }

        /// <summary>
        /// Contact ID of the Receiver
        /// </summary>
        [JsonProperty("receiverId")]
        public string ReceiverId { get; set; }
    }

    /// <summary>
    /// Class used to transfer chat messages
    /// </summary>
    public class MsgMessage : MessageBase
    {
        /// <summary>
        /// Status of the message
        /// </summary>
        [JsonProperty("messageStatus")]
        public MessageStatus Status { get; set; }

        /// <summary>
        /// Unique Id for each message
        /// </summary>
        [JsonProperty("messageGuid")]
        public Guid MessageGuid { get; set; }

        /// <summary>
        /// Message content
        /// </summary>
        [JsonProperty("messageText")]
        public string MessageText { get; set; }

        /// <summary>
        /// Message Time-stamp as UTC ticks
        /// </summary>
        [JsonProperty("timeStamp")]
        public long TimeStamp { get; set; }

        public override string ToString()
        {
            return $"{Type} SenderID {SenderId} ReceiverID {ReceiverId} MsgGuid:  {MessageGuid}  Status: {Status} Text: {MessageText} Time: {new DateTime(TimeStamp).ToShortTimeString()}";
        }
    }

    /// <summary>
    /// Class used to transfer group chat messages
    /// </summary>
    public class GroupMessage : MsgMessage
    {
        /// <summary>
        /// Unique Id for each group
        /// </summary>
        [JsonProperty("groupGuid")]
        public Guid GroupGuid { get; set; }

        /// <summary>
        /// List of Contact Ids who are members of the group
        /// </summary>
        [JsonProperty("groupMembers")]
        public List<string> GroupMembers { get; set; }

        /// <summary>
        /// If true, the message will shown a RigSense group member immediately when the message is received.
        /// Else RigSense client will only show a message icon in the screen
        /// </summary>
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

    /// <summary>
    /// Class used to send acknowledge message
    /// </summary>
    public class MsgAcknowledge : MessageBase
    {
        /// <summary>
        /// The guid of the message, for which the acknowledge is sent 
        /// </summary>
        [JsonProperty("messageGuid")]
        public Guid MessageGuid { get; set; }

        /// <summary>
        /// True of message successfully reached the receiver, else false
        /// </summary>
        [JsonProperty("successful")]
        public bool Successful { get; set; }

        /// <summary>
        /// Reason for delivery failure
        /// </summary>
        [JsonProperty("reason")]
        public string Reason { get; set; }

        public override string ToString()
        {
            return $"{Type} MessageGuid: {MessageGuid} Success: {Successful} Reason: {Reason}";
        }
    }

    /// <summary>
    /// Class to transfer address book
    /// </summary>
    public class MsgAddressBook : MessageBase
    {
        /// <summary>
        /// List of ContactInfo who are in the client's address book
        /// </summary>
        [JsonProperty("contacts")]
        public List<IContactInfo> ContactList { get; set; }

        public override string ToString()
        {
            StringBuilder builder = new StringBuilder();
            ContactList.ForEach(ci => builder.AppendFormat(" {0} ", ci.ToString()));

            return $"{Type} Members {builder.ToString()}";
        }
    }

    /*
     * MemberSerialization is set to opt in as we don't want to serialize all the properties as they are derived from other properties
     */
    /// <summary>
    /// Interface for all contact types
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public interface IContactInfo
    {
        /// <summary>
        /// Display friendly name of the contact
        /// </summary>
        string DisplayName { get; }

        /// <summary>
        /// Contact Id of the contact
        /// </summary>
        string ContactId { get; }

        /// <summary>
        /// Denotes if the contact is available to receive message or not
        /// </summary>
        ContactStatus Status { get; }
    }

    public enum ContactStatus
    {
        On,
        Off
    }

    /// <summary>
    /// Class that represent a RigSense AppSVR
    /// </summary>
    public class RigInfo : IContactInfo
    {
        public RigInfo()
        {
            MachineList = new List<string>();
        }

        /// <summary>
        /// Id of the rig
        /// </summary>
        [JsonProperty("rigId")]
        public int RigID { get; set; }

        /// <summary>
        /// Name of the rig
        /// </summary>
        [JsonProperty("rigName")]
        public string RigName { get; set; }

        /// <summary>
        /// Denotes if the Rig can accept files as message attachment 
        /// </summary>
        [JsonProperty("acceptFile")]
        public bool AcceptFile { get; set; }

        /// <summary>
        /// Name of the machines that are present in the AppSVR, these names are used to create Contact Id of a machine 
        /// </summary>
        [JsonProperty("machineList")]
        public List<string> MachineList { get; set; }

        public string DisplayName
        {
            get { return RigName; }
        }

        public string ContactId
        {
            get { return ContactInfoHelper.CreateRigContactID(RigID); }
        }

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

        /// <summary>
        /// Returns the display name of a particular machine on an AppSVR
        /// </summary>
        /// <param name="machine">The name of the machine</param>
        /// <returns></returns>
        public string GetMachineDisplayName(string machine)
        {
            if (MachineList.Contains(machine))
                return RigName + ":" + machine;
            else
                return RigName + ":" + machine + "(offline)";
        }

        /// <summary>
        /// Returns the contact id of a particular machine. 
        /// 
        /// When sending a message to a particular machine of an AppSvr, this method is used to get the contact id of the receiver. 
        /// </summary>
        /// <param name="machine">Name of the machine</param>
        /// <returns></returns>
        public string GetMachineID(string machine)
        {
            return ContactId + ContactInfoHelper.CreateMachinePart(machine);
        }

        public override string ToString()
        {
            return $"RigInfo: Id {ContactId} Status {Status}";
        }
    }

    /// <summary>
    /// Class that represents a UV Client
    /// </summary>
    public class UVClientInfo : IContactInfo
    {

        [JsonProperty("displayName")]
        public string DisplayName { get; set; }

        /// <summary>
        /// Login name of the client
        /// </summary>
        [JsonProperty("clientLogin")]
        public string ClientLogin { get; set; }

        [JsonProperty("status")]
        public ContactStatus Status { get; set; }


        public string ContactId
        {
            get { return ContactInfoHelper.CreateClientContactID(ClientLogin); }
        }

        public override string ToString()
        {
            return $"UVClientInfo: Id {ContactId} Status {Status}";
        }
    }

    /// <summary>
    /// Class used to register a client in the messenger server
    /// </summary>
    public class MsgRegister : MessageBase
    {
        public override string ToString()
        {
            return $"{Type} ID {SenderId} GUID {SenderGuid}";
        }
    }

    /// <summary>
    /// Class used to request the last messages of all historical conversation of the client
    /// </summary>
    public class MsgRequestLastMessageList : MessageBase
    {
        public override string ToString()
        {
            return $"Type: {Type}";
        }
    }

    /// <summary>
    /// Class used to request historical messages of a particular conversation of the client
    /// 
    /// When the historical messages are sent to the client all the messages are sent with Status = HistoricalNormal and a dummy
    /// end message is sent with Status = Pseudo to mark the end of the stream. If there are no messages available for the conversation
    /// server will just send one dummy message with Status = Pseudo
    /// </summary>
    public class MsgRequestHistoricalData : MessageBase
    {
        /// <summary>
        /// Number of hours to get historical message for
        /// </summary>
        [JsonProperty("hourLimit")]
        public int HourLimit { get; set; }

        public override string ToString()
        {
            return $"Type: {Type}";
        }
    }

    /// <summary>
    /// Class used to request historical conversation messages for a particular group
    /// 
    /// When the historical messages are sent to the client all the messages are sent with Status = HistoricalNormal and a dummy
    /// end message is sent with Status = Pseudo to mark the end of the stream. If there are no messages available for the conversation
    /// server will just send one dummy message with Status = Pseudo
    /// </summary>
    public class GroupMessageRequestHistoricalData : MsgRequestHistoricalData
    {
        /// <summary>
        /// Group guid for the particular group 
        /// </summary>
        [JsonProperty("groupGuid")]
        public Guid GroupGuid { get; set; }

        public override string ToString()
        {
            return $"{base.ToString()} GroupGuid {GroupGuid}";
        }
    }

    /// <summary>
    /// Class to request last messages for multiple group conversation
    /// </summary>
    public class GroupMessageRequestLastMessageList : MsgRequestLastMessageList
    {
        /// <summary>
        /// List of group guids to get last message for
        /// </summary>
        [JsonProperty("groupGuids")]
        public List<Guid> GroupGuids { get; set; }

        public override string ToString()
        {
            StringBuilder builder = new StringBuilder();
            foreach (var guid in GroupGuids)
            {
                builder.AppendFormat(" {0} ", guid);
            }
            return $"{base.ToString()} GroupGuids {builder.ToString()}";
        }
    }

    /// <summary>
    /// Class used to request a rig connection
    /// </summary>
    public class MsgReqRigConnection : MessageBase
    {
        public override string ToString()
        {
            return $"Type: {Type} ReceiverID: {ReceiverId}";
        }
    }

    /// <summary>
    /// Heart beat class transfered to keep the connection open
    /// </summary>
    public class MsgHeartBeat : MessageBase
    { }

    public class MsgUnauthorized : Base
    { }
}
