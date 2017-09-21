using ChatApplication.Models;
using Microsoft.Web.WebSockets;
using Service;
using System;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;
using System.Web.Http;

namespace ChatApplication.Controllers
{
    public class ChatController : ApiController
    {
        private static readonly string token = "dummyToken";

        //http://localhost:50222/api/Chat/Token
        [ActionName("Token")]
        [HttpGet]
        public HttpResponseMessage GetToken()
        {
            Encoding encoding = Encoding.GetEncoding("iso-8859-1");
            string usernamePassword = encoding.GetString(Convert.FromBase64String((Request.Headers.Authorization.Parameter)));
            string userName = usernamePassword.Split(':')[0];
            string password = usernamePassword.Split(':')[1];

            if (userName == "mdtadmin" && password == "password")
            {
                return Request.CreateResponse(HttpStatusCode.OK, new TokenContainer() { Token = token });
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.Unauthorized);
            }

        }

        //ws://localhost:50222/api/Chat/Socket
        [ActionName("Socket")]
        [HttpGet]
        public HttpResponseMessage GetSocket()
        {
            var currentContext = HttpContext.Current;

            if (currentContext.IsWebSocketRequest || currentContext.IsWebSocketRequestUpgrading)
            {
                //currentContext.AcceptWebSocketRequest(ProcessWebsocketSession);
                /*We can use Subclass of WebSocketHandler when we use Microsoft.Web.WebSockets package which is not actively developed any more
                 Currently WebSockets are supported by SignalR package which does not support WebSocketHandler and we have to 
                 use the ProcessWebSocketSession exclusively to handle web socket connection*/
                currentContext.AcceptWebSocketRequest(new ChatWebSocketHandler(token));
                return Request.CreateResponse(HttpStatusCode.SwitchingProtocols);
            }

            return Request.CreateResponse(HttpStatusCode.BadRequest);
        }

        //private Task ProcessWebsocketSession(AspNetWebSocketContext context)
        //{
        //    var handler = new ChatWebSocketHandler();
        //    var processTask = handler.ProcessWebSocketRequestAsync(context);
        //    return processTask;
        //}

        //The 
        //private async Task ProcessWebsocketSession(AspNetWebSocketContext context)
        //{
        //    var ws = context.WebSocket;

        //    new Task(async () =>
        //    {
        //        var inputSegment = new ArraySegment<byte>(new byte[1024]);

        //        while (true)
        //        {
        //            // MUST read if we want the state to get updated...
        //            var result = await ws.ReceiveAsync(inputSegment, CancellationToken.None);

        //            if (ws.State != WebSocketState.Open)
        //            {
        //                break;
        //            }
        //        }
        //    }).Start();

        //    while (true)
        //    {
        //        if (ws.State != WebSocketState.Open)
        //        {
        //            break;
        //        }
        //        else
        //        {
        //            byte[] binaryData = { 0xde, 0xad, 0xbe, 0xef, 0xca, 0xfe };
        //            var segment = new ArraySegment<byte>(binaryData);
        //            await ws.SendAsync(segment, WebSocketMessageType.Binary,
        //                true, CancellationToken.None);
        //        }
        //    }
        //}
    }
}
