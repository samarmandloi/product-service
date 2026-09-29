package com.pm.productservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("PRODUCT")
@Getter
@Setter
public class Product extends Checkoutable {
}