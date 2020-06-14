package pojos.login;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CriarFuncionarioObj {

    public String name;
    public String salary;
	public String age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}




}

