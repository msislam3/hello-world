package transferobjects;

import java.time.LocalDate;
import java.util.Date;

public class Employee {
	private int number;
	private String firstName;
	private String lastName;
	private Gender gender;
	//https://stackoverflow.com/a/31238011/1841089
	private LocalDate birthDate;
	private LocalDate hireDate;
	
	public Employee() {}
	
	public Employee(int number, String firstName, String lastName, Gender gender, LocalDate birthDate, LocalDate hireDate) {
		setNumber(number);
		setFirstName(firstName);
		setLastName(lastName);
		setGender(gender);
		setBirthDate(birthDate);
		setHireDate(hireDate);
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

}
