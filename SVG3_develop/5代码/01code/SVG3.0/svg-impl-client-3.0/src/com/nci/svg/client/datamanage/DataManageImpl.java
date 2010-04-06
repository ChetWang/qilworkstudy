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
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-11
 * @���ܣ����ݹ���ʵ����
 * 
 */
public class DataManageImpl extends DataManageAdapter {

	/**
	 * ϵͳ��������
	 */
	private HashMap<String, String> mapSysSet = new HashMap<String, String>();
	/**
	 * �����
	 */
	private HashMap<String, HashMap<String, CodeInfoBean>> mapCodes = new HashMap<String, HashMap<String, CodeInfoBean>>();
	/**
	 * ͼԪ��
	 */
	private HashMap<String, HashMap<String, NCIEquipSymbolBean>> mapGraphUnit = new HashMap<String, HashMap<String, NCIEquipSymbolBean>>();
	// /**
	// * ģ���
	// */
	// private HashMap<String, HashMap<String, NCIEquipSymbolBean>> mapTemplate
	// = new HashMap<String, HashMap<String, NCIEquipSymbolBean>>();

	/**
	 * �����洢��
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
			// ��ȡϵͳ��������
			String value = mapSysSet.get(upperType);
			if (value == null) {
				// ��������ڣ�������
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
			// ��ȡϵͳ��������
			Object value = mapGlobal.get(upperType);
			if (value == null) {
				// ��������ڣ�������
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

			// ��ȡϵͳ��������
			String value = mapSysSet.get(upperType);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("�޶�Ӧ�������ļ�");
				result.setReturnType("String");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("String");
				result.setReturnObj(value);
			}
			break;
		}
		case KIND_CODES: {
			// ��ȡ��������
			result = getCodeData(upperType, obj);
			break;
		}
		case KIND_SYMBOL: {
			// ��ȡͼԪ����
			result = getSymbolData(kind, upperType, obj);
			break;
		}
			// case KIND_TEMPLATE: {
			// // ��ȡģ������
			// result = getSymbolData(kind, upperType, obj);
			// break;
			// }
		case KIND_INDUNORM: {
			// ��ȡҵ��淶����
			Object value = mapGlobal.get(KINDTYPE_INDUNORM);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("�޶�Ӧ�������ļ�");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("Object");
				result.setReturnObj(value);
			}
			break;
		}
		case KIND_MODEL: {
			// ��ȡҵ��ģ������
			Object value = mapGlobal.get(KINDTYPE_MODEL);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("�޶�Ӧ�������ļ�");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("Object");
				result.setReturnObj(value);
			}
			break;
		}
		case KIND_MODELRELAINDUNORM: {
			// ��ȡҵ��淶��ģ�͹�������
			Object value = mapGlobal.get(KINDTYPE_MODELRELAINDUNORM);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("�޶�Ӧ�������ļ�");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("Object");
				result.setReturnObj(value);
			}
			break;
		}
		case KIND_GLOBAL: {

			// ��ȡϵͳ��������
			Object value = mapGlobal.get(upperType);
			if (value == null) {
				result.setReturnFlag(OPER_ERROR);
				result.setErrorText("�޶�Ӧ�������ļ�");
			} else {
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("Object");
				result.setReturnObj(value);
			}
			break;
		}
		default: {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("Ŀǰ��֧�ָ��������͵����ݻ�ȡ����");
		}
		}
		return result;
	}

	@Override
	public int loadLocal(int kind, String type) {
		int ret = OPER_ERROR;
		switch (kind) {
		case KIND_SYSSET: {
			// ��ȡϵͳ��������
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
			// Զ�̻�ȡ�����
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
				editor.getLogger().log(this, LoggerAdapter.ERROR, "�޷���ȡ�������Ϣ!");
			}
			break;
		}
		case KIND_SYMBOL: {
			// Զ�̻�ȡͼԪ��ģ��

			break;
		}
		case KIND_INDUNORM: {
			// Զ�̻�ȡ��ҵ��ϵͳ֧�ֵ�����ҵ��淶�嵥
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
						"�޷���ȡҵ��淶�嵥��Ϣ!");
			}
			break;
		}
		case KIND_MODEL: {
			// Զ�̻�ȡ��ҵ��ϵͳ֧�ֵ�����ģ���嵥
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
						.log(this, LoggerAdapter.ERROR, "�޷���ȡģ���嵥��Ϣ!");
			}
			break;
		}
		case KIND_MODELRELAINDUNORM: {
			// Զ�̻�ȡ��ҵ��ϵͳ֧�ֵ�����ģ�͹���ҵ��淶�嵥
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
						.log(this, LoggerAdapter.ERROR, "�޷���ȡģ���嵥��Ϣ!");
			}
			break;
		}
			// case KIND_TEMPLATE: {
			// // Զ�̻�ȡģ��
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
			// ��ȡϵͳ��������
			String value = mapSysSet.get(type);
			if (value != null) {
				// ������ڣ���ɾ��
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
					// ������ɾ��
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
					// ������ɾ��
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
			// // ������ɾ��
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
	 * ���ݴ������������Ϣ��ͳһ��conf.p�ļ��л�ȡϵͳ����
	 * 
	 * @param f����
	 * @return����ȡ��ǣ��ɹ�����OPER_SUCCESS,ʧ�ܷ���OPER_ERROR
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
			// ��ȡϵͳ���ö������е�����������
			Field[] fields = SysSetDefines.class.getDeclaredFields();
			int len = fields.length;
			String fieldName = null;
			String sysValue = null;
			for (int i = 0; i < len; i++) {
				// ��ȡÿ�������������
				fieldName = fields[i].getName();
				try {
					// ��ȡԭ���ķ��ʿ���Ȩ��
					boolean accessFlag = fields[i].isAccessible();
					// �޸ķ��ʿ���Ȩ��
					fields[i].setAccessible(true);
					// ��ȡÿ���������ֵ
					Object o = fields[i].get(SysSetDefines.class);
					if (o == null || !(o instanceof String))
						continue;
					// �����������ֵ��ȡ�����ļ��е�ֵ
					sysValue = p.getProperty((String) o);
					if (sysValue == null)
						continue;
					mapSysSet.put(((String) o).toUpperCase(), sysValue);
					// �ָ����ʿ���Ȩ��
					fields[i].setAccessible(accessFlag);
				} catch (Exception ex) {
					continue;
				}
			}
		} catch (IOException ex) {
			editor.getLogger().log(this, LoggerAdapter.ERROR,
					"�޷��ҵ������ļ�:" + sysSetFileName);
			return OPER_ERROR;
		}
		return OPER_SUCCESS;
	}

	/**
	 * ���л��洢������
	 * 
	 * @param obj�����洢����Ϣ
	 * @param fileName���洢���ļ���
	 * @return:�洢������ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
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
			editor.getLogger().log(this, LoggerAdapter.ERROR, "���л��洢-�ļ��޷��ҵ�");
			return OPER_ERROR;
		} catch (IOException e) {
			editor.getLogger().log(this, LoggerAdapter.ERROR, "���л��洢-д���ļ�����");
			return OPER_ERROR;
		}

		return OPER_SUCCESS;
	}

	/**
	 * ���ض�ȡ���л�����
	 * 
	 * @param fileName�������ļ���
	 * @return����ȡ������ɹ��򷵻����л����ݣ�ʧ�ܷ���null
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
			editor.getLogger().log(this, LoggerAdapter.ERROR, "���л���ȡ-�ļ��޷��ҵ�");
		} catch (IOException e) {
			obj = null;
			editor.getLogger().log(this, LoggerAdapter.ERROR, "���л���ȡ-��ȡ�ļ�����");
		} catch (ClassNotFoundException e) {
			obj = null;
			editor.getLogger().log(this, LoggerAdapter.ERROR, "���л���ȡ-���޷��ҵ�");
		}
		return obj;

	}

	@Override
	public int saveLocal(int kind) {
		int ret = OPER_ERROR;
		switch (kind) {
		case KIND_CODES: {
			// ��������л�
			ret = writeDataToLocal(mapCodes, Constants.NCI_SVG_LOCAL_CODES);
			break;
		}
		case KIND_SYMBOL: {
			// ͼԪ���л�
			// ret = writeDataToLocal(mapGraphUnit,
			// Constants.NCI_SVG_LOCAL_GRAPHUNIT);
			break;
		}
			// case KIND_TEMPLATE: {
			// // ģ�����л�
			// ret = writeDataToLocal(mapTemplate,
			// Constants.NCI_SVG_LOCAL_TEMPLATE);
			// break;
			// }
		case KIND_GLOBAL: {
			// �����������л�
			ret = writeDataToLocal(mapGlobal, Constants.NCI_SVG_LOCAL_GLOBAL);
			break;
		}
		default: {
			// ���������������л�
		}
		}
		return ret;
	}

	/**
	 * ���ݴ���Ĵ������ͺʹ��������ȡ��������
	 * 
	 * @param type:�������ͣ�type����Ϊ��
	 * @param obj����������
	 *            objΪ�գ���ѯ�ô������͵����д������ݣ�����HashMap<String,CodeInfoBean>
	 *            objΪString����ʱ��objΪ����ֵ����ѯ�ô��������·��ϸô���ֵ�Ĵ�������,����CodeInfoBean
	 *            objΪCodeInfoBean����ʱ��
	 *            ��valueֵ��Ϊ��ʱ��ѯ�ô��������·��ϸô���ֵ�Ĵ������ݣ�����CodeInfoBean
	 *            ��nameֵ��Ϊ��ʱ��ѯ�ô��������·��ϸô������Ƶĵ�һ���������ݣ�����CodeInfoBean
	 *            ��parentValueֵ��Ϊ��ʱ��ѯ�ô��������·��ϸø�����ֵ�����д������ݣ�����HashMap<String,CodeInfoBean>
	 * @return���������ݼ���
	 */
	protected ResultBean getCodeData(String type, Object obj) {
		ResultBean result = new ResultBean();
		if (type == null || type.length() == 0) {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("�������Ͳ���Ϊ��");
			editor.getLogger().log(this, LoggerAdapter.ERROR, "�������Ͳ���Ϊ��");
			return result;
		}
		HashMap<String, CodeInfoBean> mapCode = mapCodes.get(type);
		if (mapCode == null) {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("�����ڸô������͵Ĵ�����Ϣ");
			editor.getLogger().log(this, LoggerAdapter.ERROR,
					type + " �����ڸô������͵Ĵ�����Ϣ");
		} else {
			if (obj == null) {
				// ��ȡ�ô������͵����д�����Ϣ
				result.setReturnFlag(OPER_SUCCESS);
				result.setReturnType("HashMap");
				result.setReturnObj(mapCode);
			} else if (obj instanceof String) {
				// �����Ϊ����ֵ�������ȡ������Ϣ
				CodeInfoBean bean = mapCode.get(obj);
				if (bean == null) {
					result.setReturnFlag(OPER_ERROR);
					result.setErrorText("�޴˴���ֵ");
					editor.getLogger().log(this, LoggerAdapter.ERROR,
							type + ":" + obj + ":" + "�޴˴���ֵ");
				} else {
					// ��ȡ�ô������͵ĸô���ֵ��Ӧ�Ĵ�����Ϣ
					result.setReturnFlag(OPER_SUCCESS);
					result.setReturnType("CodeInfoBean");
					result.setReturnObj(bean);
				}
			} else if (obj instanceof CodeInfoBean) {
				CodeInfoBean bean = (CodeInfoBean) obj;
				CodeInfoBean codeBean = null;
				if (bean.getValue() != null && bean.getValue().length() == 0) {
					// �����Ϊ����ֵ�������ȡ������Ϣ
					codeBean = mapCode.get(bean.getValue());
					if (codeBean == null) {
						result.setReturnFlag(OPER_ERROR);
						result.setErrorText("�޴˴���ֵ");
						editor.getLogger().log(this, LoggerAdapter.ERROR,
								type + ":" + bean.getValue() + ":" + "�޴˴���ֵ");
					} else {
						// ��ȡ�ô������͵ĸô���ֵ��Ӧ�Ĵ�����Ϣ
						result.setReturnFlag(OPER_SUCCESS);
						result.setReturnType("CodeInfoBean");
						result.setReturnObj(codeBean);
					}
				} else if (bean.getName() != null
						&& bean.getName().length() == 0) {
					// �����Ϊ����ֵ�������ȡ������Ϣ
					Iterator<CodeInfoBean> iterator = mapCode.values()
							.iterator();
					boolean isFind = false;
					while (iterator.hasNext()) {
						codeBean = iterator.next();
						if (codeBean.getName().equals(bean.getName())) {
							// ��ȡ�ô������͵ĸô���ֵ��Ӧ�Ĵ�����Ϣ
							result.setReturnFlag(OPER_SUCCESS);
							result.setReturnType("CodeInfoBean");
							result.setReturnObj(codeBean);
							isFind = true;
						}
					}
					if (!isFind) {
						result.setReturnFlag(OPER_ERROR);
						result.setErrorText("�޴˴���ֵ");
						editor.getLogger().log(this, LoggerAdapter.ERROR,
								type + ":" + bean.getName() + ":" + "�޴˴���ֵ");
					}
				} else if (bean.getParentValue() != null
						&& bean.getParentValue().length() == 0) {
					// ��ȡ���ϸ���������ĳһ����ֵ��Ӧ�Ĵ��뼯��
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
					// ��ȡ�ô������͵ĸø�����ֵ��Ӧ�����д�����Ϣ
					result.setReturnFlag(OPER_SUCCESS);
					result.setReturnType("HashMap");
					result.setReturnObj(map);
				}
			}
		}
		return result;
	}

	/**
	 * ���ݴ���Ĵ������ͺʹ��������ȡ��������
	 * 
	 * @param kind:�������࣬ͼԪ��ģ��
	 * @param type��ͼԪ���ͣ�����Ϊ��
	 * @param obj�������
	 *            ��objΪ��ʱ�������ͼԪ����������ͼԪ,����HashMap<String, NCIEquipSymbolBean>
	 *            ��objΪNCIEquipSymbolBeanʱ
	 *            ��name��Ϊ��ʱ���򷵻ظ�ͼԪ�����·��ϸ�name��ͼԪNCIEquipSymbolBean���������򷵻ش���
	 * @return����ȡ�����
	 */
	protected ResultBean getSymbolData(int kind, String type, Object obj) {
		ResultBean result = new ResultBean();

		HashMap<String, NCIEquipSymbolBean> mapSymbol = null;
		if (type == null) {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("ͼԪ���Ͳ���Ϊ��");
			editor.getLogger().log(this, LoggerAdapter.ERROR, "ͼԪ���Ͳ���Ϊ��");
			return result;
		}
		// �������������ȡͼԪ����ӳ��
		if (kind == KIND_SYMBOL) {
			mapSymbol = mapGraphUnit.get(type);
		}
		// else {
		// mapSymbol = mapTemplate.get(type);
		// }
		if (mapSymbol == null) {
			result.setReturnFlag(OPER_ERROR);
			result.setErrorText("��ͼԪ������ͼԪ");
			editor.getLogger().log(this, LoggerAdapter.ERROR, "��ͼԪ������ͼԪ");
			return result;
		}

		// ����obj����������
		if (obj == null) {
			// ��ȡ�����͵�����ͼԪ
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
						result.setErrorText("�޷��������ͼԪ");
						editor.getLogger().log(this, LoggerAdapter.ERROR,
								"�޷��������ͼԪ");
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
			result.setErrorText("�޷�ʶ������");
			editor.getLogger().log(this, LoggerAdapter.ERROR, "�޷�ʶ������");
			return result;
		}
		return result;
	}

	/**
	 * ����ϵͳ�����ļ�·��
	 * 
	 * @param sysSetFileName
	 */
	public void setSysSetFileName(String sysSetFileName) {
		this.sysSetFileName = sysSetFileName;
	}
}
