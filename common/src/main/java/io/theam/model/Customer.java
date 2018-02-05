package io.theam.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_id")
	private long id;

	@Column(name = "first_name")
	@NotNull
	private String firstName;

	@Column(name = "last_name")
	@NotNull
	private String lastName;

	@Column(name = "ndi")
	@NotNull
	private String ndi;

	@Column(name = "imageId")
	private String imageId;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<Purchase> purchases = new HashSet<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getNdi() {
		return ndi;
	}

	public void setNdi(String ndi) {
		this.ndi = ndi;
	}

	public String getImageId() { return imageId; }

	public void setImageId(String imageId) { this.imageId = imageId; }

	public Set<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(Set<Purchase> purchases) {
		this.purchases = purchases;
	}
}
