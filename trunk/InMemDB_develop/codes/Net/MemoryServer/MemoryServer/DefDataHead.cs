using System;
using System.Collections.Generic;
using System.Text;

namespace MemoryServer
{
    public class DefDataHead
    {
        public static byte UserLoging = 0x79;
        public static byte UserLogResult = 0x77;
        public static byte UserLogOut = 0x78;
        public static byte Heartbeat = 0x66;
        public static byte UserOperation = 0x71;
        public static byte ErrorHead = 0x72;
    }
}
