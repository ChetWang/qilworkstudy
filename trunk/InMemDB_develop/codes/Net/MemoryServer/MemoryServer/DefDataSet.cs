using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Xml;
using System.Xml.Serialization;

namespace MemoryServer
{
    public class DefDataSet
    {
        static DefDataSet()
        {
            string s = Path.Combine(Path.GetDirectoryName(typeof(DefDataSet).Assembly.Location), "SocketConfig.config");
            if (File.Exists(s))
            {
                try
                {
                    FileStream fs = new FileStream(s, FileMode.Open);
                    _config = (ConfigData)(XmlTransfer.DeSerialize(fs, typeof(ConfigData)));
                    fs.Close();
                    return;
                }
                catch (Exception e)
                {
                    throw new Exception("配置文件已破坏，无法生存配置。系统将以默认配置启动！\n"+ e.Message);
                }

            }
            _config = new ConfigData();
        }
        static ConfigData _config = null;

        /// <summary>
        /// 接收时限
        /// </summary>
        public static int RevTime
        {
            get
            {
                if (_config == null)
                    _config = new ConfigData();
                return _config.RecvTime;
            }
        }
        /// <summary>
        /// 心跳时间
        /// </summary>
        public static int HeadTime
        {
            get
            {
                if (_config == null)
                    _config = new ConfigData();
                return _config.HeadTime;
            }
        }
        /// <summary>
        /// 接受空间
        /// </summary>
        public static int RevDatagramSize
        {
            get
            {
                if (_config == null)
                    _config = new ConfigData();
                return _config.BufferSize;
            }
        }

        public static void SaveConfigFile()
        {
            string s = Path.Combine(Path.GetDirectoryName(typeof(DefDataSet).Assembly.Location), "SocketConfig.config");
            FileStream fs = new FileStream(s, FileMode.Create);
            XmlTransfer.Serialize(fs, _config, typeof(ConfigData));
            fs.Close();
        }
        /// <summary>
        /// 设置接收时限
        /// </summary>
        /// <param name="RevTime">接收时限，单位为秒</param>
        public static void SetRecTime(int RevTime)
        {
            if (_config == null)
                _config = new ConfigData();
            _config.RecvTime = RevTime * 1000;
        }
        /// <summary>
        /// 设置心跳时间
        /// </summary>
        /// <param name="HeadTime">心跳时间，单位为秒</param>
        public static void SetHeadTime(int HeadTime)
        {
            if (_config == null)
                _config = new ConfigData();
            _config.HeadTime = HeadTime * 1000;
        }
        /// <summary>
        /// 设置接收数据最大长度，默认为64k
        /// </summary>
        /// <param name="RevDataSize">长度，单位为k</param>
        public static void SetRevDataSize(int RevDataSize)//k
        {
            if (_config == null)
                _config = new ConfigData();
            _config.BufferSize = RevDataSize * 1024;
        }
    }
    [Serializable, XmlRoot(ElementName = "ConfigData")]
    class ConfigData
    {
        public ConfigData()
        { }
        // BayType="" ID="" Name="" JGPsrID=""
        [XmlAttribute]
        public int BufferSize = 64 * 1024;//64k
        [XmlAttribute]
        public int RecvTime = 5000;//5s
        [XmlAttribute]
        public int HeadTime = 20000;//20s
    }
}
