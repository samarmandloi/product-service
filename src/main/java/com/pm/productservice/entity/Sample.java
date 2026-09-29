package com.pm.productservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("SAMPLE")
@Getter
@Setter
public class Sample extends Checkoutable {
}