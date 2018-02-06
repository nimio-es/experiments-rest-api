package io.theam.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchases")
public class Purchase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "purchase_id")
	private long id;

	@Column(name = "purchase_date")
	@JsonFormat(pattern = "YYYY-MM-dd")
	private Date date;

	@ManyToOne
	@JoinColumn (name="customer_id")
	private Customer customer;

	private long amount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public long getAmmount() {
		return amount;
	}

	public void setAmmount(long ammount) {
		this.amount = ammount;
	}
}