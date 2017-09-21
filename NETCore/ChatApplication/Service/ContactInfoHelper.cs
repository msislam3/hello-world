namespace Service
{
    public class ContactInfoHelper
    {
        public static string CreateRigContactID(int rigID)
        {
            return "Rig[" + rigID + "]";
        }

        public static string CreateMachinePart(string machine)
        {
            return "Machine[" + machine.ToUpper() + "]";
        }

        public static string CreateClientContactID(string clientLogin)
        {
            return "Client[" + clientLogin.ToUpper() + "]";
        }

        public static ContactType GetContactType(string contactID)
        {
            if (contactID.Contains("Machine["))
                return ContactType.Machine;
            else if (contactID.Contains("Rig["))
                return ContactType.Rig;
            else if (contactID.Contains("Client["))
                return ContactType.UVClient;

            return ContactType.Unknown;
        }
    }
}
