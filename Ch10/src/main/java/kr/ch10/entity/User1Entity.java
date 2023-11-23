package kr.ch10.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="USER1")
public class User1Entity {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String hp;
	private int age;
	
}
