package com.nci.domino.beans.desyer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.message.MessageHandler;
import com.nci.domino.utils.BeanParamUtils;

/**
 * 业务流程
 * 
 * @author Qil.Wong
 * 
 */
public class WofoProcessBean extends WofoBaseBean implements MessageHandler{

	public static final String DEPLOY_STATUS_WAIT_DEPLOY = "WAIT_DEPLOY"; // 未发布
	public static final String DEPLOY_STATUS_DEPLOYED = "DEPLOYED"; // 已经发布

	public static final String PROCESS_STATUS_ONLINE = "ON"; // 运行中
	public static final String PROCESS_STATUS_OUTLINE = "OUT"; // 停运
	public static final String PROCESS_STATUS_EXPIRED = "EXPIRED"; // 超期
	public static final String PROCESS_STATUS_DELETE = "DEL"; // 删除

	private String processId;
	private String processCode;
	private String processName;
	private String processMasterId;
	private String processMasterName;
	private String packageId;
	private String description;
	private int displayOrder; // 显示顺序
	private String deployStatus; // 发布状态
	private String processStatus; // 流程状态
	private int processVersion; // 流程版本
	private String effectUnitId;
	private String effectDepartmentId;
	private String appViewType;
	private String appViewName;
	private String appViewUrl;
	private Date activeTime;
	private Date disableTime;
	private String lookAndFeel; // 外观，是图标模式还是图元模式
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	private WofoProcessBean oldVersionProcess; // 旧版本
	private boolean nextVersion = false; // 是否新版本

	private List graphs = new ArrayList(); // 包括活动环节、迁移、泳道和备注
	private List conditions = new ArrayList(); // 流程条件
	private List arguments = new ArrayList(); // 流程参数
    private List events = new ArrayList(); // 流程事件(存放事件监听)
    private List messages = new ArrayList(); // 流程消息
    private WofoWorkitemRuleBean workitemRule; // 工作单规则

	private Map pluginProps = new HashMap(); // 其它业务数据存放，可以作为第三方接入时数据存放地

	// 数据必须是可序列化对象

	public WofoProcessBean() {
	}

	public WofoProcessBean(String processId) {
		this.processId = processId;
	}

	public WofoProcessBean(Map map) {
		this.processId = (String) map.get("process_id");
		this.populate(map);
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessMasterId() {
		return processMasterId;
	}

	public void setProcessMasterId(String processMasterId) {
		this.processMasterId = processMasterId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(String deployStatus) {
		this.deployStatus = deployStatus;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public int getProcessVersion() {
		return processVersion;
	}

	public void setProcessVersion(int processVersion) {
		this.processVersion = processVersion;
	}

	public String getEffectUnitId() {
		return effectUnitId;
	}

	public void setEffectUnitId(String effectUnitId) {
		this.effectUnitId = effectUnitId;
	}

	public String getEffectDepartmentId() {
		return effectDepartmentId;
	}

	public void setEffectDepartmentId(String effectDepartmentId) {
		this.effectDepartmentId = effectDepartmentId;
	}

	public String getAppViewType() {
		return appViewType;
	}

	public void setAppViewType(String appViewType) {
		this.appViewType = appViewType;
	}

	public String getAppViewUrl() {
		return appViewUrl;
	}

	public void setAppViewUrl(String appViewUrl) {
		this.appViewUrl = appViewUrl;
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public Date getDisableTime() {
		return disableTime;
	}

	public void setDisableTime(Date disableTime) {
		this.disableTime = disableTime;
	}

	public String getLookAndFeel() {
		return lookAndFeel;
	}

	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}

	public String getCreatorUnitId() {
		return creatorUnitId;
	}

	public void setCreatorUnitId(String creatorUnitId) {
		this.creatorUnitId = creatorUnitId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = BeanParamUtils.getDateValue(createDate);
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = BeanParamUtils.getDateValue(updateDate);
	}

	public boolean isNextVersion() {
		return nextVersion;
	}

	public void setNextVersion(boolean nextVersion) {
		this.nextVersion = nextVersion;
		if (!nextVersion) {
			oldVersionProcess = null;
		}
	}

	public WofoWorkitemRuleBean getWorkitemRule() {
		return workitemRule;
	}

	public void setWorkitemRule(WofoWorkitemRuleBean workitemRule) {
		this.workitemRule = workitemRule;
	}

	public void populate(Map map) {
		List list = new ArrayList();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			list.add(entry.getKey());
		}
		if (list.contains("process_code")) {
			this.processCode = (String) map.get("process_code");
		}
		if (list.contains("process_name")) {
			this.processName = (String) map.get("process_name");
		}
		if (list.contains("process_master_id")) {
			this.processMasterId = (String) map.get("process_master_id");
		}
		if (list.contains("package_id")) {
			this.packageId = (String) map.get("package_id");
		}
		if (list.contains("description")) {
			this.description = (String) map.get("description");
		}
		if (list.contains("display_order")) {
			this.displayOrder = BeanParamUtils.getIntValue((BigDecimal) map.get("display_order"));
		}
		if (list.contains("deploy_status")) {
			this.deployStatus = (String) map.get("deploy_status");
		}
		if (list.contains("process_status")) {
			this.processStatus = (String) map.get("process_status");
		}
		if (list.contains("process_version")) {
			this.processVersion = BeanParamUtils.getIntValue((BigDecimal) map.get("process_version"));
		}
		if (list.contains("effect_unit_id")) {
			this.effectUnitId = (String) map.get("effect_unit_id");
		}
		if (list.contains("effect_department_id")) {
			this.effectDepartmentId = (String) map.get("effect_department_id");
		}
		if (list.contains("app_view_type")) {
			this.appViewType = (String) map.get("app_view_type");
		}
		if (list.contains("app_view_name")) {
			this.appViewName = (String) map.get("app_view_name");
		}
		if (list.contains("app_view_url")) {
			this.appViewUrl = (String) map.get("app_view_url");
		}
		if (list.contains("active_time")) {
			this.activeTime = (Date) map.get("active_time");
		}
		if (list.contains("disable_time")) {
			this.disableTime = (Date) map.get("disable_time");
		}
		if (list.contains("look_and_feel")) {
			this.lookAndFeel = (String) map.get("look_and_feel");
		}
		if (list.contains("creator_unit_id")) {
			this.creatorUnitId = (String) map.get("creator_unit_id");
		}
		if (list.contains("creator_id")) {
			this.creatorId = (String) map.get("creator_id");
		}
		if (list.contains("create_date")) {
			this.createDate = BeanParamUtils.getDateValue((Date) map.get("create_date"));
		}
		if (list.contains("update_date")) {
			this.updateDate = BeanParamUtils.getDateValue((Date) map.get("update_date"));
		}
	}

	public Map toMap() {
		Map map = new HashMap();
		map.put("process_id", processId);
		map.put("process_code", processCode);
		map.put("process_name", processName);
		map.put("process_master_id", processMasterId);
		map.put("package_id", packageId);
		map.put("description", description);
		map.put("display_order", BeanParamUtils.parseBigDecimal(displayOrder));
		map.put("deploy_status", deployStatus);
		map.put("process_status", processStatus);
		map.put("process_version", BeanParamUtils.parseBigDecimal(processVersion));
		map.put("effect_unit_id", effectUnitId);
		map.put("effect_department_id", effectDepartmentId);
		map.put("app_view_type", appViewType);
		map.put("app_view_name", appViewName);
		map.put("app_view_url", appViewUrl);
		map.put("active_time", activeTime);
		map.put("disable_time", disableTime);
		map.put("look_and_feel", lookAndFeel);
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public String toString() {
		return processName;
	}

	public String getProcessMasterName() {
		return processMasterName;
	}

	public void setProcessMasterName(String processMasterName) {
		this.processMasterName = processMasterName;
	}

	public List getGraphs() {
		return graphs;
	}

	public void setGraphs(List graphs) {
		this.graphs = graphs;
	}

	public List getConditions() {
		return conditions;
	}

	public void setConditions(List conditions) {
		this.conditions = conditions;
	}

	public String getAppViewName() {
		return appViewName;
	}

	public void setAppViewName(String appViewName) {
		this.appViewName = appViewName;
	}

	public Map getPluginProps() {
		return pluginProps;
	}

	public void setPluginProps(Map pluginProps) {
		this.pluginProps = pluginProps;
	}

	public List getArguments() {
		return arguments;
	}

	public void setArguments(List arguments) {
		this.arguments = arguments;
	}

	public List getEvents() {
		return events;
	}

	public void setEvents(List events) {
		this.events = events;
	}

	public List getMessages() {
		return messages;
	}

	public void setMessages(List messages) {
		this.messages = messages;
	}

	public String getID() {
		return processId;
	}

	public void setID(String id) {
		this.processId = id;
	}

	public void stateChanged(int oldState, int newState) {
		if (newState == DELETED || newState == NATURAL) {
			if (graphs != null) {
				for (int i = 0; i < graphs.size(); i++) {
					((WofoBaseBean) graphs.get(i)).setState(newState);
				}
			}
			if (conditions != null) {
				for (int i = 0; i < conditions.size(); i++) {
					((WofoBaseBean) conditions.get(i)).setState(newState);
				}
			}
			if (arguments != null) {
				for (int i = 0; i < arguments.size(); i++) {
					((WofoBaseBean) arguments.get(i)).setState(newState);
				}
			}
			if (workitemRule != null) {
				workitemRule.setState(newState);
			}
			if (events != null) {
				for (int i = 0; i < events.size(); i++) {
					((WofoBaseBean) events.get(i)).setState(newState);
				}
			}
			if (messages != null) {
				for (int i = 0; i < messages.size(); i++) {
					((WofoBaseBean) messages.get(i)).setState(newState);
				}
			}
			// 插件对象也要处理成DELETED
			Iterator pluginIt = pluginProps.values().iterator();
			while (pluginIt.hasNext()) {
				Object o = pluginIt.next();
				if (o != null) {
					if (o instanceof WofoBaseBean) {
						((WofoBaseBean) o).setState(newState);
					} else if (o instanceof List) {
						for (int i = 0; i < ((List) o).size(); i++) {
							if (((List) o).get(i) instanceof WofoBaseBean) {
								((WofoBaseBean) ((List) o).get(i))
										.setState(newState);
							}
						}
					} else if (o instanceof Map) {
						Iterator it = ((Map) o).values().iterator();
						while (it.hasNext()) {
							Object oo = it.next();
							if (oo instanceof WofoBaseBean) {
								((WofoBaseBean) oo).setState(newState);
							}
						}
					}
				}
			}
		}
	}

	public WofoProcessBean getOldVersionProcess() {
		return oldVersionProcess;
	}

	public void setOldVersionProcess(WofoProcessBean oldVersionProcess) {
		this.oldVersionProcess = oldVersionProcess;
	}
}
