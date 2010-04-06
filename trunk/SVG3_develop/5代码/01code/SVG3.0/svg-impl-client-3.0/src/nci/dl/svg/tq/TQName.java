package nci.dl.svg.tq;

public class TQName {
	private String name;
	private String uuid;

	public TQName() {
	}

	public TQName(String name, String uuid) {
		this.name = name;
		this.uuid = uuid;
	}

	public String toString() {
		if (name == null || !name.trim().equals(""))
			return uuid;
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
