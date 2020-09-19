package com.udacity.project4.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_order")
public class UserOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	@Column
	private Long id;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonProperty
	@Column
    private List<Item> items;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false, referencedColumnName = "id")
	@JsonProperty
    private User user;
	
	@JsonProperty
	@Column
	private BigDecimal total;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public static UserOrder createFromCart(Cart cart) {
		UserOrder order = new UserOrder();
		order.setItems(cart.getItems().stream().collect(Collectors.toList()));
		order.setTotal(cart.getTotal());
		order.setUser(cart.getUser());
		return order;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof UserOrder)) return false;

		UserOrder userOrder = (UserOrder) o;

		return new EqualsBuilder()
				.append(getId(), userOrder.getId())
				.append(getItems(), userOrder.getItems())
				.append(getUser(), userOrder.getUser())
				.append(getTotal(), userOrder.getTotal())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(getId())
				.append(getItems())
				.append(getUser())
				.append(getTotal())
				.toHashCode();
	}

	@Override
	public String toString() {
		return "UserOrder{" +
				"id=" + id +
				", items=" + items +
				", user=" + user +
				", total=" + total +
				'}';
	}
}