package nci.gps;

import nci.gps.message.StaticUnPackageMessage;
import nci.gps.util.MsgLogger;

/**
 * 根据控制码中的功能码，采取对应的操作
 * 
 * @author Qil.Wong
 * 
 */
public class FunctionCodeCommunicator {
	TransactorServer server;

	public FunctionCodeCommunicator(TransactorServer server) {
		this.server = server;
	}

	public void parse(byte functionCode, byte[] oneFrame) {
		switch (functionCode) {
		case 0x00:// 中继
			System.out.println("中继");
			break;
		case 0x01:// 读当前信息
			System.out.println("读当前信息");
			break;
		case 0x02:// 读任务数据
			System.out.println("读任务数据");
			break;
		case 0x04:// 读编程日志
			System.out.println("读编程日志");
			break;
		case 0x08:// 写对象参数
			System.out.println("写对象参数");
			break;
		case 0x09:// 异常告警
			System.out.println("异常告警");
			break;
		case 0x0A:// 告警确认
			System.out.println("告警确认");
			break;
		case 0x21:// 登录
			System.out.println("登录");
			break;
		case 0x22:// 登录退出
			System.out.println("登录退出");
			break;
		case 0x24:// 心跳检测
			System.out.println("心跳检测");
			break;
		case 0x28:// 请求发送短信
			System.out.println("请求发送短信");
			break;
		case 0x29:// 收到短信上报
			System.out.println("收到短信上报");
			break;
		}
		// 有异常发生
		if (StaticUnPackageMessage.getIsErrorCode(oneFrame)) {
			byte[] data = StaticUnPackageMessage.getData(oneFrame);
			parseError(data[0]);
		}

	}

	private void parseError(byte errorCode) {
		switch (errorCode) {
		case 0x00: { // 正常
			MsgLogger.log(MsgLogger.INFO, "正常！");
			break;
		}
		case 0x01: {// 中继命令没有返回！
			MsgLogger.log(MsgLogger.INFO, "中继命令没有返回！");
			break;
		}
		case 0x02: {// 设置内容非法
			MsgLogger.log(MsgLogger.INFO, "设置内容非法！");
			break;
		}
		case 0x03: {// 密码权限不足
			MsgLogger.log(MsgLogger.INFO, "密码权限不足！");
			break;
		}
		case 0x04: {// 无此项数据
			MsgLogger.log(MsgLogger.INFO, "无此项数据！");
			break;
		}
		case 0x11: {// 目标地址不存在
			MsgLogger.log(MsgLogger.INFO, "目标地址不存在！");
			break;
		}
		case 0x12: {// 发送失败
			MsgLogger.log(MsgLogger.INFO, "发送失败！");
			break;
		}
		case 0x13: {// 短消息帧太长
			MsgLogger.log(MsgLogger.INFO, "短消息帧太长！");
			break;
		}
		default:{
			MsgLogger.log(MsgLogger.INFO, "无法识别的错误码！"+errorCode);
			break;
		}
		}
	}

}
