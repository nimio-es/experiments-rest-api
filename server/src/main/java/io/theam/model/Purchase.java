package io.theam.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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

	@ManyToOne
	@JoinColumn (name="product_id")
	private Product product;

	@Column(name = "num_items")
	private int numOfItems;

	@Column(name = "item_price")
	private double priceOfItem;


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

	public Product getProduct() { return product; }

	public void setProduct(Product product) { this.product = product; }

	public int getNumOfItems() { return numOfItems; }

	public void setNumOfItems(int numOfItems) { this.numOfItems = numOfItems; }

	public double getPriceOfItem() { return priceOfItem; }

	public void setPriceOfItem(double priceOfItem) { this.priceOfItem = priceOfItem; }
}