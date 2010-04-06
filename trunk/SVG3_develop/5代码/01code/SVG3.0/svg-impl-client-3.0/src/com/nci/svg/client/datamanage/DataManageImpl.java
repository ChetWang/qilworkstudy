package com.nci.svg.client.datamanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.IndunormBean;
import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.logger.LoggerAdapter;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-12-11
 * @功能：数据管理实现类
 * 
 */
public class DataManageImpl extends DataManageAdapter {

	/**
	 * 系统参数配置
	 */
	private HashMap<String, String> mapSysSet = new HashMap<String, String>();
	/**
	 * 代码表
	 */
	private HashMap<String, HashMap<String, CodeInfoBean>> mapCodes = new HashMap<String, HashMap<String, CodeInfoBean>>();
	/**
	 * 图元表
	 */
	private HashMap<String, HashMap<String, NCIEquipSymbolBean>> mapGraphUnit = new HashMap<String, HashMap<String, NCIEquipSymbolBean>>();
	// /**
	// * 模板表
	// */
	// private HashMap<String, HashMap<String, NCIEquipSymbolBean>> mapTemplate
	// = new HashMap<String, HashMap<String, NCIEquipSymbolBean>>();

	/**
	 * 公共存储表
	 */
	private HashMap<String, Object> mapGlobal = new HashMap<String, Object>();
	private String sysSetFileName = "../conf/conf.p";

	public DataManageImpl(EditorAdapter editor) {
		super(editor);
	}

	@Override
	public int addData(int kind, String type, Object obj) {
		int ret = OPER_ERROR;
		String upperType = type.toUpperCase();
		if (type == null || type.length() == 0 || obj == null)
			return ret;
		switch (kind) {
		case KIND_SYSSET: {
			// 获取系统配置数据
			String value = mapSysSet.get(upperType);
			if (value == null) {
				// 如果不存在，则新增
				mapSysSet.put(upperType, (String) obj);
				ret = OPER_SUCCESS;
			}
			break;
		}
		case KIND_CODES: {
			HashMap<String, CodeInfoBean> mapCode = mapCodes.get(upperType);
			if (obj instanceof CodeInfoBean) {

				CodeInfoBean bean = (CodeInfoBean) obj;
				if (mapCode == null) {
					mapCode = new HashMap<String, CodeInfoBean>();

					mapCode.put(bean.getValue(), bean);
					mapCodes.put(upperType, mapCode);
					ret = OPER_SUCCESS;
				} else {
					if (mapCode.get(bean.getValue()) == null) {
						mapCode.put(bean.getValue(), bean);
						ret = OPER_SUCCESS;
					}
				}
			}

			break;
		}
		case KIND_SYMBOL: {
			HashMap<String, NCIEquipSymbolBean> map = mapGraphUnit
					.get(upperType);
			if (obj instanceof NCIEquipSymbolBean) {

				NCIEquipSymbolBean bean = (NCIEquipSymbolBean) obj;
				if (map == null) {
					map = new HashMap<String, NCIEquipSymbolBean>();

					map.put(bean.getName(), bean);
					mapGraphUnit.put(upperType, map);
					ret = OPER_SUCCESS;
				} else {
					if (map.get(bean.getName()) == null) {
						map.put(bean.getName(), bean);
						ret = OPER_SUCCESS;
					}
				}
			}
			break;
		}
		case KIND_GLOBAL: {
			// 获取系统配置数据
			Object value = mapGlobal.get(upperType);
			if (value == null) {
				// 如果不存在，则新增
				mapGlobal.put(upperType, obj);
				ret = OPER_SUCCESS;
			}
			break;
		}
			// case KIND_TEMPLATE: {
			// HashMap<String, NCIEquipSymbolBean> map =
			// mapTemplate.get(upperType);
			// if (obj instanceof NCIEquipSymbolBean) {
			//
			// NCIEquipSymbolBean bean = (NCIEquipSymbolBean) obj;
			// if (map == null) {
			// map = new HashMap<String, NCIEquipSymbolBean>();
			//
			// map.put(bean.getName(), bean);
			// mapTemplate.put(upperType, map);
			// ret = OPER_SUCCESS;
			// } else {
			// if (map.get(bean.getName()) == null) {
			// map.put(bean.getName(), bean);
			// ret = OPER_SUCCESS;
			// }
			// }
			// }
			// break;
			// }
		}
		return ret;
	}

	@Override
	public ResultBean getData(int kind, String type, Object obj) {
		ResultBean result = new ResultBean();
		String upperType = null;
		if (type != null)
			upperType = type.toUpperCase();
		switch (kind) {
		case KIND_SYSSET: {

			// 获取系统配置数据
			String value = mapSysSet.get(upperType);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("无对应的设置文件");
				result.setReturnType("String");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("String");
				result.setReturnObj(value);
			}
			break;
		}
		case KIND_CODES: {
			// 获取代码数据
			result = getCodeData(upperType, obj);
			break;
		}
		case KIND_SYMBOL: {
			// 获取图元数据
			result = getSymbolData(kind, upperType, obj);
			break;
		}
			// case KIND_TEMPLATE: {
			// // 获取模板数据
			// result = getSymbolData(kind, upperType, obj);
			// break;
			// }
		case KIND_INDUNORM: {
			// 获取业务规范数据
			Object value = mapGlobal.get(KINDTYPE_INDUNORM);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("无对应的设置文件");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("Object");
				result.setReturnObj(value);
			}
			break;
		}
		case KIND_MODEL: {
			// 获取业务模型数据
			Object value = mapGlobal.get(KINDTYPE_MODEL);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("无对应的设置文件");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("Object");
				result.setReturnObj(value);
			}
			break;
		}
		case KIND_MODELRELAINDUNORM: {
			// 获取业务规范与模型关联数据
			Object value = mapGlobal.get(KINDTYPE_MODELRELAINDUNORM);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("无对应的设置文件");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("Object");
				result.setReturnObj(value);
			}
			break;
		}
		case KIND_GLOBAL: {

			// 获取系统配置数据
			Object value = mapGlobal.get(upperType);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("无对应的设置文件");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("Object");
				result.setReturnObj(value);
			}
			break;
		}
		default: {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("目前不支持该数据类型的数据获取请求");
		}
		}
		return result;
	}

	@Override
	public int loadLocal(int kind, String type) {
		int ret = OPER_ERROR;
		switch (kind) {
		case KIND_SYSSET: {
			// 获取系统配置数据
			ret = loadSysSet();
			break;
		}
		case KIND_CODES: {
			Object obj = readDataFromLocal(Constants.NCI_SVG_LOCAL_CODES);
			if (obj != null && obj instanceof HashMap) {
				try {
					HashMap<String, HashMap<String, CodeInfoBean>> map = (HashMap<String, HashMap<String, CodeInfoBean>>) obj;
					mapCodes.putAll(map);
					ret = OPER_SUCCESS;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			break;
		}
		case KIND_GLOBAL: {
			Object obj = readDataFromLocal(Constants.NCI_SVG_LOCAL_GLOBAL);
			if (obj != null && obj instanceof HashMap) {
				try {
					HashMap<String, Object> map = (HashMap<String, Object>) obj;
					mapGlobal.putAll(map);
					ret = OPER_SUCCESS;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			break;
		}
		case KIND_SYMBOL: {
			// Object obj =
			// readDataFromLocal(Constants.NCI_SVG_LOCAL_GRAPHUNIT);
			// if (obj != null && obj instanceof HashMap) {
			// try {
			// HashMap<String, HashMap<String, NCIEquipSymbolBean>> map =
			// (HashMap<String, HashMap<String, NCIEquipSymbolBean>>) obj;
			// mapGraphUnit.putAll(map);
			// ret = OPER_SUCCESS;
			// } catch (Exception ex) {
			//
			// }
			// }
			break;
		}
			// case KIND_TEMPLATE: {
			// Object obj = readDataFromLocal(Constants.NCI_SVG_LOCAL_TEMPLATE);
			// if (obj != null && obj instanceof HashMap) {
			// try {
			// HashMap<String, HashMap<String, NCIEquipSymbolBean>> map =
			// (HashMap<String, HashMap<String, NCIEquipSymbolBean>>) obj;
			// mapTemplate.putAll(map);
			// ret = OPER_SUCCESS;
			// } catch (Exception ex) {
			//
			// }
			// }
			// break;
			// }
		default: {

		}
		}
		return ret;
	}

	@Override
	public int loadRemote(int kind, String type) {

		int ret = OPER_ERROR;
		switch (kind) {
		case KIND_CODES: {
			// 远程获取代码表
			ResultBean rb = editor.getCommunicator().communicate(
					new CommunicationBean(ActionNames.GET_SVG_CODES, null));
			if (rb != null) {
				HashMap<String, HashMap<String, CodeInfoBean>> codes = (HashMap<String, HashMap<String, CodeInfoBean>>) rb
						.getReturnObj();
				if (codes != null) {
					mapCodes.putAll(codes);
				}
				ret = OPER_SUCCESS;
			} else {
				editor.getLogger().log(this, LoggerAdapter.ERROR, "无法获取代码表信息!");
			}
			break;
		}
		case KIND_SYMBOL: {
			// 远程获取图元、模板

			break;
		}
		case KIND_INDUNORM: {
			// 远程获取本业务系统支持的所有业务规范清单
			ResultBean rb = editor.getCommunicator().communicate(
					new CommunicationBean(ActionNames.GET_BUSINESS_INDUNORM,
							null));
			if (rb != null) {
				HashMap<String, IndunormBean> indunorms = (HashMap<String, IndunormBean>) rb
						.getReturnObj();
				if (indunorms != null) {
					mapGlobal.put(KINDTYPE_INDUNORM, indunorms);
				}
				ret = OPER_SUCCESS;
			} else {
				editor.getLogger().log(this, LoggerAdapter.ERROR,
						"无法获取业务规范清单信息!");
			}
			break;
		}
		case KIND_MODEL: {
			// 远程获取本业务系统支持的所有模型清单
			ResultBean rb = editor.getCommunicator()
					.communicate(
							new CommunicationBean(
									ActionNames.GET_BUSINESS_MODEL, null));
			if (rb != null) {
				HashMap<String, ModelBean> models = (HashMap<String, ModelBean>) rb
						.getReturnObj();
				if (models != null) {
					mapGlobal.put(KINDTYPE_MODEL, models);
				}
				ret = OPER_SUCCESS;
			} else {
				editor.getLogger()
						.log(this, LoggerAdapter.ERROR, "无法获取模型清单信息!");
			}
			break;
		}
		case KIND_MODELRELAINDUNORM: {
			// 远程获取本业务系统支持的所有模型关联业务规范清单
			ResultBean rb = editor.getCommunicator().communicate(
					new CommunicationBean(
							ActionNames.GET_BUSINESS_MOELRELAINDUNORM, null));
			if (rb != null) {
				HashMap<String, ArrayList<ModelRelaIndunormBean>> relas = (HashMap<String, ArrayList<ModelRelaIndunormBean>>) rb
						.getReturnObj();
				if (relas != null) {
					mapGlobal.put(KINDTYPE_MODELRELAINDUNORM, relas);
				}
				ret = OPER_SUCCESS;
			} else {
				editor.getLogger()
						.log(this, LoggerAdapter.ERROR, "无法获取模型清单信息!");
			}
			break;
		}
			// case KIND_TEMPLATE: {
			// // 远程获取模板
			// break;
			// }
		}
		return ret;
	}

	@Override
	public int removeData(int kind, String type, Object obj) {
		int ret = OPER_ERROR;
		if (type == null || type.length() == 0)
			return ret;
		switch (kind) {
		case KIND_SYSSET: {
			// 获取系统配置数据
			String value = mapSysSet.get(type);
			if (value != null) {
				// 如果存在，则删除
				mapSysSet.remove(type);
				ret = OPER_SUCCESS;
			}
			break;
		}
		case KIND_CODES: {
			HashMap<String, CodeInfoBean> mapCode = mapCodes.get(type);
			if (obj == null) {
				if (mapCode != null) {
					mapCode.remove(type);
					ret = OPER_SUCCESS;
				}
			} else if (obj instanceof CodeInfoBean) {

				CodeInfoBean bean = (CodeInfoBean) obj;
				if (mapCode != null) {
					// 存在则删除
					if (mapCode.get(bean.getValue()) != null) {
						mapCode.remove(bean.getValue());
						ret = OPER_SUCCESS;
					}
				}
			}
			break;
		}
		case KIND_SYMBOL: {
			HashMap<String, NCIEquipSymbolBean> map = mapGraphUnit.get(type);
			if (obj == null) {
				if (map != null) {
					mapGraphUnit.remove(type);
					ret = OPER_SUCCESS;
				}
			} else if (obj instanceof NCIEquipSymbolBean) {

				NCIEquipSymbolBean bean = (NCIEquipSymbolBean) obj;
				if (map != null) {
					// 存在则删除
					if (map.get(bean.getName()) != null) {
						map.remove(bean.getName());
						ret = OPER_SUCCESS;
					}
				}
			}
			break;
		}
			// case KIND_TEMPLATE: {
			// HashMap<String, NCIEquipSymbolBean> map = mapTemplate.get(type);
			// if(obj == null)
			// {
			// if(map != null)
			// {
			// mapGraphUnit.remove(type);
			// ret = OPER_SUCCESS;
			// }
			// }
			// else if (obj instanceof NCIEquipSymbolBean) {
			//
			// NCIEquipSymbolBean bean = (NCIEquipSymbolBean) obj;
			// if (map != null) {
			// // 存在则删除
			// if (map.get(bean.getName()) != null) {
			// map.remove(bean.getName());
			// ret = OPER_SUCCESS;
			// }
			// }
			// }
			// break;
			// }
		}
		return ret;
	}

	@Override
	public int setData(int kind, String type, Object obj) {
		int ret = OPER_ERROR;
		if (type == null || type.length() == 0 || obj == null)
			return ret;
		String upperType = type.toUpperCase();
		switch (kind) {
		case KIND_SYSSET: {
			mapSysSet.put(upperType, (String) obj);
			ret = OPER_SUCCESS;
			break;
		}
		case KIND_CODES: {
			HashMap<String, CodeInfoBean> mapCode = mapCodes.get(upperType);
			if (obj instanceof CodeInfoBean) {

				CodeInfoBean bean = (CodeInfoBean) obj;
				if (mapCode == null) {
					mapCode = new HashMap<String, CodeInfoBean>();
					mapCodes.put(upperType, mapCode);
				}
				mapCode.put(bean.getValue().toUpperCase(), bean);
				ret = OPER_SUCCESS;
			}

			break;
		}
		case KIND_SYMBOL: {
			HashMap<String, NCIEquipSymbolBean> map = mapGraphUnit
					.get(upperType);
			if (obj instanceof NCIEquipSymbolBean) {

				NCIEquipSymbolBean bean = (NCIEquipSymbolBean) obj;
				if (map == null) {
					map = new HashMap<String, NCIEquipSymbolBean>();
					mapGraphUnit.put(upperType, map);
				}
				map.put(bean.getName(), bean);
				ret = OPER_SUCCESS;
			}
			break;
		}
		case KIND_GLOBAL: {
			mapGlobal.put(upperType, obj);
			ret = OPER_SUCCESS;
			break;
		}
			// case KIND_TEMPLATE: {
			// HashMap<String, NCIEquipSymbolBean> map =
			// mapTemplate.get(upperType);
			// if (obj instanceof NCIEquipSymbolBean) {
			//
			// NCIEquipSymbolBean bean = (NCIEquipSymbolBean) obj;
			// if (map == null) {
			// map = new HashMap<String, NCIEquipSymbolBean>();
			// mapTemplate.put(upperType, map);
			// }
			// map.put(bean.getName(), bean);
			// ret = OPER_SUCCESS;
			// }
			// break;
			// }

		}
		return ret;
	}

	/**
	 * 根据传入的类属性信息，统一在conf.p文件中获取系统配置
	 * 
	 * @param f：类
	 * @return：读取标记，成功返回OPER_SUCCESS,失败返回OPER_ERROR
	 */
	private int loadSysSet() {
		Properties p = new Properties();
		try {
			File f = null;
			if (editor.isApplet()) {
				f = new File(sysSetFileName);				
			}else{
				f = new File("conf/conf.p");		
			}
			p.load(new FileInputStream(f));
			// 获取系统设置定义类中的所有属性域
			Field[] fields = SysSetDefines.class.getDeclaredFields();
			int len = fields.length;
			String fieldName = null;
			String sysValue = null;
			for (int i = 0; i < len; i++) {
				// 获取每个属性域的名称
				fieldName = fields[i].getName();
				try {
					// 获取原来的访问控制权限
					boolean accessFlag = fields[i].isAccessible();
					// 修改访问控制权限
					fields[i].setAccessible(true);
					// 获取每个属性域的值
					Object o = fields[i].get(SysSetDefines.class);
					if (o == null || !(o instanceof String))
						continue;
					// 根据属性域的值获取配置文件中的值
					sysValue = p.getProperty((String) o);
					if (sysValue == null)
						continue;
					mapSysSet.put(((String) o).toUpperCase(), sysValue);
					// 恢复访问控制权限
					fields[i].setAccessible(accessFlag);
				} catch (Exception ex) {
					continue;
				}
			}
		} catch (IOException ex) {
			editor.getLogger().log(this, LoggerAdapter.ERROR,
					"无法找到配置文件:" + sysSetFileName);
			return OPER_ERROR;
		}
		return OPER_SUCCESS;
	}

	/**
	 * 序列化存储至本地
	 * 
	 * @param obj：待存储的信息
	 * @param fileName：存储的文件名
	 * @return:存储结果，成功返回OPER_SUCCESS，失败返回OPER_ERROR
	 */
	protected int writeDataToLocal(Object obj, String fileName) {
		ObjectOutputStream out;
		try {
			File file = new File(fileName);
			file.createNewFile();
			out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(obj);
			out.close();
		} catch (FileNotFoundException e) {
			editor.getLogger().log(this, LoggerAdapter.ERROR, "序列化存储-文件无法找到");
			return OPER_ERROR;
		} catch (IOException e) {
			editor.getLogger().log(this, LoggerAdapter.ERROR, "序列化存储-写入文件有误");
			return OPER_ERROR;
		}

		return OPER_SUCCESS;
	}

	/**
	 * 本地读取序列化数据
	 * 
	 * @param fileName：本地文件名
	 * @return：读取结果，成功则返回序列化数据，失败返回null
	 */
	protected Object readDataFromLocal(String fileName) {
		ObjectInputStream in;
		Object obj = null;
		try {
			in = new ObjectInputStream(new FileInputStream(fileName));
			obj = in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			obj = null;
			editor.getLogger().log(this, LoggerAdapter.ERROR, "序列化读取-文件无法找到");
		} catch (IOException e) {
			obj = null;
			editor.getLogger().log(this, LoggerAdapter.ERROR, "序列化读取-读取文件有误");
		} catch (ClassNotFoundException e) {
			obj = null;
			editor.getLogger().log(this, LoggerAdapter.ERROR, "序列化读取-类无法找到");
		}
		return obj;

	}

	@Override
	public int saveLocal(int kind) {
		int ret = OPER_ERROR;
		switch (kind) {
		case KIND_CODES: {
			// 代码表序列化
			ret = writeDataToLocal(mapCodes, Constants.NCI_SVG_LOCAL_CODES);
			break;
		}
		case KIND_SYMBOL: {
			// 图元序列化
			// ret = writeDataToLocal(mapGraphUnit,
			// Constants.NCI_SVG_LOCAL_GRAPHUNIT);
			break;
		}
			// case KIND_TEMPLATE: {
			// // 模板序列化
			// ret = writeDataToLocal(mapTemplate,
			// Constants.NCI_SVG_LOCAL_TEMPLATE);
			// break;
			// }
		case KIND_GLOBAL: {
			// 公共数据序列化
			ret = writeDataToLocal(mapGlobal, Constants.NCI_SVG_LOCAL_GLOBAL);
			break;
		}
		default: {
			// 其他类型无需序列化
		}
		}
		return ret;
	}

	/**
	 * 根据传入的代码类型和代码请求获取代码数据
	 * 
	 * @param type:代码类型，type不能为空
	 * @param obj：代码请求
	 *            obj为空，查询该代码类型的所有代码数据，返回HashMap<String,CodeInfoBean>
	 *            obj为String类型时，obj为代码值，查询该代码类型下符合该代码值的代码数据,返回CodeInfoBean
	 *            obj为CodeInfoBean类型时，
	 *            当value值不为空时查询该代码类型下符合该代码值的代码数据，返回CodeInfoBean
	 *            当name值不为空时查询该代码类型下符合该代码名称的第一个代码数据，返回CodeInfoBean
	 *            当parentValue值不为空时查询该代码类型下符合该父代码值的所有代码数据，返回HashMap<String,CodeInfoBean>
	 * @return：代码数据集合
	 */
	protected ResultBean getCodeData(String type, Object obj) {
		ResultBean result = new ResultBean();
		if (type == null || type.length() == 0) {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("代码类型不能为空");
			editor.getLogger().log(this, LoggerAdapter.ERROR, "代码类型不能为空");
			return result;
		}
		HashMap<String, CodeInfoBean> mapCode = mapCodes.get(type);
		if (mapCode == null) {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("无属于该代码类型的代码信息");
			editor.getLogger().log(this, LoggerAdapter.ERROR,
					type + " 无属于该代码类型的代码信息");
		} else {
			if (obj == null) {
				// 获取该代码类型的所有代码信息
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("HashMap");
				result.setReturnObj(mapCode);
			} else if (obj instanceof String) {
				// 传入的为代码值，请求获取代码信息
				CodeInfoBean bean = mapCode.get(obj);
				if (bean == null) {
					result.setReturnFlag(OPER_ERROR);
					result.setErrorText("无此代码值");
					editor.getLogger().log(this, LoggerAdapter.ERROR,
							type + ":" + obj + ":" + "无此代码值");
				} else {
					// 获取该代码类型的该代码值对应的代码信息
					result.setReturnFlag(OPER_SUCCESS);
					result.setReturnType("CodeInfoBean");
					result.setReturnObj(bean);
				}
			} else if (obj instanceof CodeInfoBean) {
				CodeInfoBean bean = (CodeInfoBean) obj;
				CodeInfoBean codeBean = null;
				if (bean.getValue() != null && bean.getValue().length() == 0) {
					// 传入的为代码值，请求获取代码信息
					codeBean = mapCode.get(bean.getValue());
					if (codeBean == null) {
						result.setReturnFlag(OPER_ERROR);
						result.setErrorText("无此代码值");
						editor.getLogger().log(this, LoggerAdapter.ERROR,
								type + ":" + bean.getValue() + ":" + "无此代码值");
					} else {
						// 获取该代码类型的该代码值对应的代码信息
						result.setReturnFlag(OPER_SUCCESS);
						result.setReturnType("CodeInfoBean");
						result.setReturnObj(codeBean);
					}
				} else if (bean.getName() != null
						&& bean.getName().length() == 0) {
					// 传入的为代码值，请求获取代码信息
					Iterator<CodeInfoBean> iterator = mapCode.values()
							.iterator();
					boolean isFind = false;
					while (iterator.hasNext()) {
						codeBean = iterator.next();
						if (codeBean.getName().equals(bean.getName())) {
							// 获取该代码类型的该代码值对应的代码信息
							result.setReturnFlag(OPER_SUCCESS);
							result.setReturnType("CodeInfoBean");
							result.setReturnObj(codeBean);
							isFind = true;
						}
					}
					if (!isFind) {
						result.setReturnFlag(OPER_ERROR);
						result.setErrorText("无此代码值");
						editor.getLogger().log(this, LoggerAdapter.ERROR,
								type + ":" + bean.getName() + ":" + "无此代码值");
					}
				} else if (bean.getParentValue() != null
						&& bean.getParentValue().length() == 0) {
					// 获取符合父代码类型某一代码值对应的代码集合
					Iterator<CodeInfoBean> iterator = mapCode.values()
							.iterator();
					HashMap<String, CodeInfoBean> map = new HashMap<String, CodeInfoBean>();
					while (iterator.hasNext()) {
						codeBean = iterator.next();
						if (codeBean.getParentValue().equals(
								bean.getParentValue())) {
							map.put(codeBean.getValue(), codeBean);
						}
					}
					// 获取该代码类型的该父代码值对应的所有代码信息
					result.setReturnFlag(OPER_SUCCESS);
					result.setReturnType("HashMap");
					result.setReturnObj(map);
				}
			}
		}
		return result;
	}

	/**
	 * 根据传入的代码类型和代码请求获取代码数据
	 * 
	 * @param kind:数据种类，图元或模板
	 * @param type：图元类型，不能为空
	 * @param obj：请求包
	 *            当obj为空时，请求该图元类型下所有图元,返回HashMap<String, NCIEquipSymbolBean>
	 *            当obj为NCIEquipSymbolBean时
	 *            当name不为空时，则返回该图元类型下符合该name的图元NCIEquipSymbolBean，不存在则返回错误
	 * @return：获取结果包
	 */
	protected ResultBean getSymbolData(int kind, String type, Object obj) {
		ResultBean result = new ResultBean();

		HashMap<String, NCIEquipSymbolBean> mapSymbol = null;
		if (type == null) {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("图元类型不能为空");
			editor.getLogger().log(this, LoggerAdapter.ERROR, "图元类型不能为空");
			return result;
		}
		// 根据数据种类获取图元类型映射
		if (kind == KIND_SYMBOL) {
			mapSymbol = mapGraphUnit.get(type);
		}
		// else {
		// mapSymbol = mapTemplate.get(type);
		// }
		if (mapSymbol == null) {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("该图元类型无图元");
			editor.getLogger().log(this, LoggerAdapter.ERROR, "该图元类型无图元");
			return result;
		}

		// 根据obj处理各种情况
		if (obj == null) {
			// 获取该类型的所有图元
			result.setReturnFlag(OPER_SUCCESS);
			result.setReturnType("HashMap");
			result.setReturnObj(mapSymbol);
		} else {
			if (obj instanceof NCIEquipSymbolBean) {
				NCIEquipSymbolBean bean = (NCIEquipSymbolBean) obj;
				if (bean.getName() != null && bean.getName().length() != 0) {
					NCIEquipSymbolBean nciBean = mapSymbol.get(bean.getName());
					if (nciBean == null) {
						result.setReturnFlag(OPER_ERROR);
						result.setErrorText("无符合请求的图元");
						editor.getLogger().log(this, LoggerAdapter.ERROR,
								"无符合请求的图元");
						return result;
					} else {
						result.setReturnFlag(OPER_SUCCESS);
						result.setReturnType("NCIEquipSymbolBean");
						result.setReturnObj(nciBean);
						return result;
					}
				}
			}
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("无法识别请求");
			editor.getLogger().log(this, LoggerAdapter.ERROR, "无法识别请求");
			return result;
		}
		return result;
	}

	/**
	 * 设置系统设置文件路径
	 * 
	 * @param sysSetFileName
	 */
	public void setSysSetFileName(String sysSetFileName) {
		this.sysSetFileName = sysSetFileName;
	}
}
