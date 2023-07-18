package com.mspr.payetonkawasrvc.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "listing_image")
@Table(name = "listing_image")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingImageBean {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @Column(columnDefinition="TEXT")
   private String imageUrl;
   private Long stockId;
   private Long dealerId;
}
