package com.nci.domino.beans.desyer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.message.MessageHandler;
import com.nci.domino.utils.BeanParamUtils;
import com.nci.domino.utils.CopyableUtils;
import com.nci.domino.utils.XmlUtils;

/**
 * 流程活动，包括所有活动的属性
 * 
 * @author Qil.Wong
 * 
 */
public class WofoActivityBean extends WofoBaseBean implements MessageHandler{

	// 活动类型代码（代码表wofoc_activity_type）
	public static final String ACTIVITY_TYPE_BEGIN = "begin";
	public static final String ACTIVITY_TYPE_END = "end";
	public static final String ACTIVITY_TYPE_AUTO = "auto";
	public static final String ACTIVITY_TYPE_HUMAN = "human";
	public static final String ACTIVITY_TYPE_JOIN = "join";
	public static final String ACTIVITY_TYPE_SPLIT = "split";
	public static final String ACTIVITY_TYPE_SUBFLOW = "subflow";
	public static final String ACTIVITY_TYPE_NOTIFY = "notify";
	public static final String ACTIVITY_TYPE_WAIT = "wait";

	// 活动定义状态（代码表wofoc_activity_status）
	public static final String ACTIVITY_STATUS_ONLINE = "ON";
	public static final String ACTIVITY_STATUS_OUTLINE = "OUT";
	public static final String ACTIVITY_STATUS_DELETE = "DEL"; // 删除

	// 分支活动条件类型（代码表wofoc_split_mode）
	public static final String JOIN_MODE_AND = "AND";
	public static final String JOIN_MODE_OR = "OR";
	public static final String JOIN_MODE_CONDITION = "CONDITION";

	// 聚合活动条件类型（代码表wofoc_join_mode）
	public static final String SPLIT_MODE_AND = "AND";
	public static final String SPLIT_MODE_OR = "OR";
	public static final String SPLIT_MODE_CONDITION = "CONDITION";

	// 模块类型
	public static final String APP_TYPE_JFW = "JFW"; // 功能模型
	public static final String APP_TYPE_URL = "URL"; // URL

	// 完成类型（代码表wofoc_complete_type）
	public static final String COMPLETE_TYPE_ALL = "ALL"; // 全部完成
	public static final String COMPLETE_TYPE_PERCENT = "PERCENT"; // 完成率
	public static final String COMPLETE_TYPE_COUNT = "COUNT"; // 完成数

	/**
	 * 参与者类型：同时参与
	 */
	public static final String PARTICIPANT_TYPE_SAMETIME = "SAMETIME";
	/**
	 * 参与者类型：顺序参与
	 */
	public static final String PARTICIPANT_TYPE_SEQUENCE = "SEQUENCE";

	private String activityId;
	private String activityCode;
	private String activityName;
	private String processMasterId;
	private String processId;
	private String processCode;
	private String packageId;
	private String activityType;
	private String activityExtends;
	private int activityVersion;
	private String description;
	private String activityStatus;
	private String activityIcon;
	private String effectUnitId;
	private String effectDepartmentId;
	private String subProcessId; // 子流程ID
	private String multiParticipant;
	private String multiInstance; // 多活动实例
	private String participateType;
	private String completeType;
	private String completeCond;
	private int limitTime;
	private int alarmTime;
	private String joinMode;
	private String joinConditionId;
	private String splitMode;
	private String splitConditionId;
	private String canUntread = CAN;
	private String canUntreadTo = CAN;
	private String canReplevy = CAN;
	private String canReplevyTo = CAN;
	private String canInterrupt = CAN;
	private String expireLoopRemind = CAN_NOT;
	private String appPerformType;
	private String appPerformName;
	private String appPerformUrl;
	private String appUntreadType;
	private String appUntreadName;
	private String appUntreadUrl;
	private String appViewType;
	private String appViewName;
	private String appViewUrl;
	private int displayOrder;
	private double posX;
	private double posY;
	private double width;
	private double height;
	private String creatorUnitId;
	private String creatorId;
	private Date createDate;
	private Date updateDate;

	private List participantScopes = new ArrayList(); // 参与者范围
	private List arguments = new ArrayList(); // 参数
	private WofoWorkitemRuleBean workitemRule; // 工作单规则
	private List events = new ArrayList(); // 事件(存放事件监听)
	private List messages = new ArrayList(); // 消息

	private Map pluginProps = new HashMap(); // 其它业务数据存放，可以作为第三方接入时数据存放地

	// 数据必须是可序列化对象

	public WofoActivityBean() {

	}

	public WofoActivityBean(String activityId) {
		this.activityId = activityId;
	}

	/**
	 * 浅克隆一个WofoActivityBean
	 * 
	 * @param bean
	 *            被克隆的WofoActivityBean
	 * @return 克隆出的WofoActivityBean
	 */
	public WofoActivityBean cloneActivity() {
		WofoActivityBean newBean = new WofoActivityBean(this.toMap());
		if (participantScopes != null) {
			List ps = new ArrayList();
			for (int i = 0; i < participantScopes.size(); i++) {
				WofoParticipantScopeBean scopeBean = (WofoParticipantScopeBean) participantScopes
						.get(i);
				WofoParticipantScopeBean newScope = scopeBean.cloneScopeBean();
				ps.add(newScope);
			}
			newBean.participantScopes = ps;
		}
		if (arguments != null) {
			List ags = new ArrayList();
			for (int i = 0; i < arguments.size(); i++) {
				WofoArgumentsBean ag = (WofoArgumentsBean) arguments.get(i);
				WofoArgumentsBean newAg = ag.cloneArguments();
				ags.add(newAg);
			}
			newBean.arguments = ags;
		}
		// FIXME 事件、消息拷贝
		// System.err.println("事件、消息的clone未实现 " + this.getClass());
		// if (events != null) {
		// List evts = new ArrayList();
		// for (int i = 0; i < events.size(); i++) {
		// WofoBean ev = (WofoArgumentsBean) events.get(i);
		// WofoArgumentsBean newEv = ev.cloneArguments();
		// evts.add(newEv);
		// }
		// }
		return newBean;
	}

	public WofoActivityBean(Map map) {
		this.activityId = (String) map.get("activity_id");
		this.populate(map);
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getProcessMasterId() {
		return processMasterId;
	}

	public void setProcessMasterId(String processMasterId) {
		this.processMasterId = processMasterId;
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

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityExtends() {
		return activityExtends;
	}

	public void setActivityExtends(String activityExtends) {
		this.activityExtends = activityExtends;
	}

	public int getActivityVersion() {
		return activityVersion;
	}

	public void setActivityVersion(int activityVersion) {
		this.activityVersion = activityVersion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getActivityIcon() {
		return activityIcon;
	}

	public void setActivityIcon(String activityIcon) {
		this.activityIcon = activityIcon;
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

	public String getSubProcessId() {
        return subProcessId;
    }

    public void setSubProcessId(String subProcessId) {
        this.subProcessId = subProcessId;
    }

    public String getMultiParticipant() {
		return multiParticipant;
	}

	public void setMultiParticipant(String multiParticipant) {
		this.multiParticipant = multiParticipant;
	}

	public String getMultiInstance() {
        return multiInstance;
    }

    public void setMultiInstance(String multiInstance) {
        this.multiInstance = multiInstance;
    }

    public String getParticipateType() {
		return participateType;
	}

	public void setParticipateType(String participateType) {
		this.participateType = participateType;
	}

	public String getCompleteType() {
		return completeType;
	}

	public void setCompleteType(String completeType) {
		this.completeType = completeType;
	}

	public String getCompleteCond() {
		return completeCond;
	}

	public void setCompleteCond(String completeCond) {
		this.completeCond = completeCond;
	}

	public int getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}

	public int getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(int alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getJoinMode() {
		return joinMode;
	}

	public void setJoinMode(String joinMode) {
		this.joinMode = joinMode;
	}

	/**
	 * 获取聚合条件
	 * 
	 * @return
	 */
	public String getJoinConditionId() {
		return joinConditionId;
	}

	/**
	 * 设置聚合条件
	 * 
	 * @param joinConditionId
	 */
	public void setJoinConditionId(String joinConditionId) {
		this.joinConditionId = joinConditionId;
	}

	public String getSplitMode() {
		return splitMode;
	}

	public void setSplitMode(String splitMode) {
		this.splitMode = splitMode;
	}

	/**
	 * 获取分支条件
	 * 
	 * @return
	 */
	public String getSplitConditionId() {
		return splitConditionId;
	}

	/**
	 * 分支条件
	 * 
	 * @param splitConditionId
	 */
	public void setSplitConditionId(String splitConditionId) {
		this.splitConditionId = splitConditionId;
	}

	public String getCanUntread() {
		return canUntread;
	}

	public void setCanUntread(String canUntread) {
		this.canUntread = canUntread;
	}

	public String getCanUntreadTo() {
		return canUntreadTo;
	}

	public void setCanUntreadTo(String canUntreadTo) {
		this.canUntreadTo = canUntreadTo;
	}

	public String getCanReplevy() {
		return canReplevy;
	}

	public void setCanReplevy(String canReplevy) {
		this.canReplevy = canReplevy;
	}

	public String getCanReplevyTo() {
		return canReplevyTo;
	}

	public void setCanReplevyTo(String canReplevyTo) {
		this.canReplevyTo = canReplevyTo;
	}

	public String getCanInterrupt() {
		return canInterrupt;
	}

	public void setCanInterrupt(String canInterrupt) {
		this.canInterrupt = canInterrupt;
	}

	public String getExpireLoopRemind() {
		return expireLoopRemind;
	}

	public void setExpireLoopRemind(String expireLoopRemind) {
		this.expireLoopRemind = expireLoopRemind;
	}

	public String getAppPerformType() {
		return appPerformType;
	}

	public void setAppPerformType(String appPerformType) {
		this.appPerformType = appPerformType;
	}

	public String getAppPerformName() {
		return appPerformName;
	}

	public void setAppPerformName(String appPerformName) {
		this.appPerformName = appPerformName;
	}

	public String getAppPerformUrl() {
		return appPerformUrl;
	}

	public void setAppPerformUrl(String appPerformUrl) {
		this.appPerformUrl = appPerformUrl;
	}

	public String getAppUntreadType() {
		return appUntreadType;
	}

	public void setAppUntreadType(String appUntreadType) {
		this.appUntreadType = appUntreadType;
	}

	public String getAppUntreadName() {
		return appUntreadName;
	}

	public void setAppUntreadName(String appUntreadName) {
		this.appUntreadName = appUntreadName;
	}

	public String getAppUntreadUrl() {
		return appUntreadUrl;
	}

	public void setAppUntreadUrl(String appUntreadUrl) {
		this.appUntreadUrl = appUntreadUrl;
	}

	public String getAppViewType() {
		return appViewType;
	}

	public void setAppViewType(String appViewType) {
		this.appViewType = appViewType;
	}

	public String getAppViewName() {
		return appViewName;
	}

	public void setAppViewName(String appViewName) {
		this.appViewName = appViewName;
	}

	public String getAppViewUrl() {
		return appViewUrl;
	}

	public void setAppViewUrl(String appViewUrl) {
		this.appViewUrl = appViewUrl;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
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
		if (list.contains("activity_code")) {
			this.activityCode = (String) map.get("activity_code");
		}
		if (list.contains("activity_name")) {
			this.activityName = (String) map.get("activity_name");
		}
		if (list.contains("package_id")) {
			this.packageId = (String) map.get("package_id");
		}
		if (list.contains("process_master_id")) {
			this.processMasterId = (String) map.get("process_master_id");
		}
		if (list.contains("process_id")) {
			this.processId = (String) map.get("process_id");
		}
		if (list.contains("process_code")) {
			this.processCode = (String) map.get("process_code");
		}
		if (list.contains("activity_type")) {
			this.activityType = (String) map.get("activity_type");
		}
		if (list.contains("activity_extends")) {
			this.activityExtends = (String) map.get("activity_extends");
		}
		if (list.contains("activity_version")) {
			this.activityVersion = BeanParamUtils.getIntValue((BigDecimal) map.get("activity_version"));
		}
		if (list.contains("description")) {
			this.description = (String) map.get("description");
		}
		if (list.contains("activity_status")) {
			this.activityStatus = (String) map.get("activity_status");
		}
		if (list.contains("activity_icon")) {
			this.activityIcon = (String) map.get("activity_icon");
		}
		if (list.contains("effect_unit_id")) {
			this.effectUnitId = (String) map.get("effect_unit_id");
		}
		if (list.contains("effect_department_id")) {
			this.effectDepartmentId = (String) map.get("effect_department_id");
		}
        if (list.contains("sub_process_id")) {
            this.subProcessId = (String) map.get("sub_process_id");
        }
		if (list.contains("multi_participant")) {
			this.multiParticipant = (String) map.get("multi_participant");
		}
        if (list.contains("multi_instance")) {
            this.multiInstance = (String) map.get("multi_instance");
        }
		if (list.contains("participate_type")) {
			this.participateType = (String) map.get("participate_type");
		}
		if (list.contains("complete_type")) {
			this.completeType = (String) map.get("complete_type");
		}
		if (list.contains("complete_cond")) {
			this.completeCond = (String) map.get("complete_cond");
		}
		if (list.contains("limit_time")) {
			this.limitTime = BeanParamUtils.getIntValue((BigDecimal) map
					.get("limit_time"));
		}
		if (list.contains("alarm_time")) {
			this.alarmTime = BeanParamUtils.getIntValue((BigDecimal) map
					.get("alarm_time"));
		}
		if (list.contains("join_mode")) {
			this.joinMode = (String) map.get("join_mode");
		}
		if (list.contains("join_condition_id")) {
			this.joinConditionId = (String) map.get("join_condition_id");
		}
		if (list.contains("split_mode")) {
			this.splitMode = (String) map.get("split_mode");
		}
		if (list.contains("split_condition_id")) {
			this.splitConditionId = (String) map.get("split_condition_id");
		}
		if (list.contains("can_untread")) {
			this.canUntread = (String) map.get("can_untread");
		}
		if (list.contains("can_untread_to")) {
			this.canUntreadTo = (String) map.get("can_untread_to");
		}
		if (list.contains("can_replevy")) {
			this.canReplevy = (String) map.get("can_replevy");
		}
		if (list.contains("can_replevy_to")) {
			this.canReplevyTo = (String) map.get("can_replevy_to");
		}
		if (list.contains("can_interrupt")) {
			this.canInterrupt = (String) map.get("can_interrupt");
		}
		if (list.contains("expire_loop_remind")) {
			this.expireLoopRemind = (String) map.get("expire_loop_remind");
		}
		if (list.contains("app_perform_type")) {
			this.appPerformType = (String) map.get("app_perform_type");
		}
		if (list.contains("app_perform_name")) {
			this.appPerformName = (String) map.get("app_perform_name");
		}
		if (list.contains("app_perform_url")) {
			this.appPerformUrl = (String) map.get("app_perform_url");
		}
		if (list.contains("app_untread_type")) {
			this.appUntreadType = (String) map.get("app_untread_type");
		}
		if (list.contains("app_untread_name")) {
			this.appUntreadName = (String) map.get("app_untread_name");
		}
		if (list.contains("app_untread_url")) {
			this.appUntreadUrl = (String) map.get("app_untread_url");
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
		if (list.contains("display_order")) {
			this.displayOrder = BeanParamUtils.getIntValue((BigDecimal) map
					.get("display_order"));
		}
		if (list.contains("pos_x")) {
			this.posX = BeanParamUtils.getIntValue((BigDecimal) map
					.get("pos_x"));
		}
		if (list.contains("pos_y")) {
			this.posY = BeanParamUtils.getIntValue((BigDecimal) map
					.get("pos_y"));
		}
		if (list.contains("width")) {
			this.width = BeanParamUtils.getDoubleValue((BigDecimal) map
					.get("width"));
		}
		if (list.contains("height")) {
			this.height = BeanParamUtils.getDoubleValue((BigDecimal) map
					.get("height"));
		}
		if (list.contains("creator_unit_id")) {
			this.creatorUnitId = (String) map.get("creator_unit_id");
		}
		if (list.contains("creator_id")) {
			this.creatorId = (String) map.get("creator_id");
		}
		if (list.contains("create_date")) {
			this.createDate = BeanParamUtils.getDateValue((Date) map
					.get("create_date"));
		}
		if (list.contains("update_date")) {
			this.updateDate = BeanParamUtils.getDateValue((Date) map
					.get("update_date"));
		}
	}

	public Map toMap() {
		Map map = new HashMap();
		map.put("activity_id", activityId);
		map.put("activity_code", activityCode);
		map.put("activity_name", activityName);
		map.put("package_id", packageId);
		map.put("process_master_id", processMasterId);
		map.put("process_id", processId);
		map.put("process_code", processCode);
		map.put("activity_type", activityType);
		map.put("activity_extends", activityExtends);
		map.put("activity_version", BeanParamUtils.parseBigDecimal(activityVersion));
		map.put("description", description);
		map.put("activity_status", activityStatus);
		map.put("activity_icon", activityIcon);
		map.put("effect_unit_id", effectUnitId);
		map.put("effect_department_id", effectDepartmentId);
		map.put("sub_process_id", subProcessId);
		map.put("multi_participant", multiParticipant);
		map.put("multi_instance", multiInstance);
		map.put("participate_type", participateType);
		map.put("complete_type", completeType);
		map.put("complete_cond", completeCond);
		map.put("limit_time", BeanParamUtils.parseBigDecimal(limitTime));
		map.put("alarm_time", BeanParamUtils.parseBigDecimal(alarmTime));
		map.put("join_mode", joinMode);
		map.put("join_condition_id", joinConditionId);
		map.put("split_mode", splitMode);
		map.put("split_condition_id", splitConditionId);
		map.put("can_untread", canUntread);
		map.put("can_untread_to", canUntreadTo);
		map.put("can_replevy", canReplevy);
		map.put("can_replevy_to", canReplevyTo);
		map.put("can_interrupt", canInterrupt);
		map.put("expire_loop_remind", expireLoopRemind);
		map.put("app_perform_type", appPerformType);
		map.put("app_perform_name", appPerformName);
		map.put("app_perform_url", appPerformUrl);
		map.put("app_untread_type", appUntreadType);
		map.put("app_untread_name", appUntreadName);
		map.put("app_untread_url", appUntreadUrl);
		map.put("app_view_type", appViewType);
		map.put("app_view_name", appViewName);
		map.put("app_view_url", appViewUrl);
		map.put("display_order", BeanParamUtils.parseBigDecimal(displayOrder));
		map.put("pos_x", BeanParamUtils.parseBigDecimal(posX));
		map.put("pos_y", BeanParamUtils.parseBigDecimal(posY));
		map.put("width", BeanParamUtils.parseBigDecimal(width));
		map.put("height", BeanParamUtils.parseBigDecimal(height));
		map.put("creator_unit_id", creatorUnitId);
		map.put("creator_id", creatorId);
		map.put("create_date", createDate);
		map.put("update_date", updateDate);
		return map;
	}

	public String toString() {
		return activityName;
	}

	public List getParticipantScopes() {
		return participantScopes;
	}

	public void setParticipantScopes(List participantScopes) {
		this.participantScopes = participantScopes;
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

	public Map getPluginProps() {
		return pluginProps;
	}

	public void setPluginProps(Map pluginProps) {
		this.pluginProps = pluginProps;
	}

	public String getID() {
		return activityId;
	}

	public void setID(String id) {
		this.activityId = id;
	}

	public Object copy(Object[] keys) {
		Object o = null;
		try {
			String sourceXml = CopyableUtils.toString(this);
			System.out.println(sourceXml);
			String activityId = (String) keys[0];
			Document doc = XmlUtils.fromXML(sourceXml);
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("string");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i).getFirstChild();
				if (node.getNodeValue() != null
						&& node.getNodeValue().equals(this.activityId)) {
					node.setNodeValue(activityId);
				}
			}
			String targetXml = XmlUtils.toXML(doc);
			System.out.println(targetXml);
			o = CopyableUtils.toObject(targetXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	public void stateChanged(int preiousState, int newState) {
		if (newState == DELETED || newState == NATURAL) {
			if (participantScopes != null) {
				for (int i = 0; i < participantScopes.size(); i++) {
					((WofoBaseBean) participantScopes.get(i))
							.setState(newState);
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
	
	public void saveByCompareToOriginalBean(WofoBaseBean currentBean,
			WofoBaseBean originalBean) {
	}

	public static void main(String[] args) {
		WofoActivityBean bean = new WofoActivityBean();
		bean.setActivityId("E7B67B30-5D53-5E73-5DF0-21932A7F4E1F");
		bean.setActivityCode("AAAA");
		bean.setActivityName("你好！");
		WofoParticipantScopeBean scope = new WofoParticipantScopeBean();
		scope.setParticipantId("1234");
		scope.setActivityId("E7B67B30-5D53-5E73-5DF0-21932A7F4E1F");
		bean.getParticipantScopes().add(scope);
		WofoActivityBean newBean = (WofoActivityBean) bean
				.copy(new Object[] { "ttttt" });
		System.out.println(newBean.getActivityId());
	}

}
