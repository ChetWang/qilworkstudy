using System;
using System.Collections.Generic;
using System.Text;
using System.Xml.Serialization;
using System.Xml;
using System.IO;

namespace MemoryServer
{
    public class XmlTransfer
    {
        public static void Serialize(Stream stream, object o, Type t)
        {
            XmlSerializer xs = new XmlSerializer(t);
            XmlTextWriter xmlTextWriter = new XmlTextWriter(stream, Encoding.UTF8);

            xs.Serialize(xmlTextWriter, o);
        }
        /// <summary>
        /// 反序列化
        /// </summary>
        /// <param name="s"></param>
        /// <param name="t"></param>
        /// <returns></returns>
        public static object DeSerialize(string s, Type t)
        {

#if DEBUG
            byte[] byteArray = Convert.FromBase64String(s);
            MemoryStream ms = new MemoryStream(byteArray);
            StreamReader sr = new StreamReader(ms);
            string sxml = sr.ReadToEnd();
#endif
            byte[] byteA = Convert.FromBase64String(s);
            MemoryStream stream = new MemoryStream(byteA);
            return DeSerialize(stream, t);
        }
        public static object DeSerialize(Stream ms, Type t)
        {
            XmlSerializer xs = new XmlSerializer(t);
            object p = xs.Deserialize(ms);//XLTModel 
            return p;
        }
    }
}
