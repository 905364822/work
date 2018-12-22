package cn.xujiaye.shiyanwu;

public class Person {

	String name;
	String tel;
	
	public Person(String name, String tel) {
		super();
		this.name = name;
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "点击姓名为" + name + "\n联系电话为" + tel;
	}
	