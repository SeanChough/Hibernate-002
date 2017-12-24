import java.sql.Blob;
import java.util.Date;


public class Students {

	private int sid;
	private String name;
	private String gender;
	private Date birthday;
	//组件属性，Address类的对象
	private Address address;
	//二进制数据类型
	private Blob picture;

	public Students() {
	}

	//方便生成对象
	public Students(int sid, String name, String gender, Date birthday,
			Address address) {
//		super();
		this.sid = sid;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.address = address;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	
	public Blob getPicture() {
		return picture;
	}

	public void setPicture(Blob picture) {
		this.picture = picture;
	}

	// 方便测试
	@Override
	public String toString() {
		return "Students [sid=" + sid + ", name=" + name + ", gender=" + gender
				+ ", birthday=" + birthday + ", address=" + address
				+ ", picture=" + picture + "]";
	}
	

}
