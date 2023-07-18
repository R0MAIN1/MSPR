package com.mspr.payetonkawasrvc.web;

import com.mspr.payetonkawasrvc.core.ListingImageBean;
import com.mspr.payetonkawasrvc.core.ProductBean;
import com.mspr.payetonkawasrvc.core.StockBean;
import com.mspr.payetonkawasrvc.service.AddStockRequest;
import com.mspr.payetonkawasrvc.service.ListingImageRequest;
import com.mspr.payetonkawasrvc.service.ListingImageService;
import com.mspr.payetonkawasrvc.service.UpdateStockRequest;
import com.mspr.payetonkawasrvc.service.UserService;
import com.mspr.payetonkawasrvc.service.ProductService;
import com.mspr.payetonkawasrvc.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/dealer", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
   private UserService userService;
   private ProductService productService;
   private StockService stockService;
   private ListingImageService listingImageService;

   public UserController(UserService userService, ProductService productService, StockService stockService, ListingImageService listingImageService) {
      this.userService = userService;
      this.productService = productService;
      this.stockService = stockService;
      this.listingImageService = listingImageService;
   }

   @GetMapping("/products")
   @ResponseStatus(HttpStatus.OK)
   public List<ListingStockDto> getAllStock(@CurrentSecurityContext(expression="authentication?.name")
                                                 String username){

      List<StockBean> stockBeans = stockService.getProductsOfDealer(username);

      return stockBeans.stream()
              .map(stockBean -> buildStockDto(stockBean))
              .collect(Collectors.toList());
   }

   @GetMapping("/products/{productId}")
   @ResponseStatus(HttpStatus.OK)
   public ListingStockDetailsDto getStockDetails(
           @CurrentSecurityContext(expression="authentication?.name")
           String username,
           @PathVariable(value = "productId" ) Long stockId ) {

      return  buildStockDetailsDto(stockService.getStockDetails(stockId,  username));
   }

   public ListingStockDto buildStockDto(StockBean stockBean){
      ProductBean productBean = productService.getProduct(stockBean.getProductId());
      List<ListingImageBean> listingImageBeans = listingImageService.findAllByStockId(stockBean.getId());

      ListingStockDto listingStockDto = new ListingStockDto();
      listingStockDto.setId(stockBean.getId());
      listingStockDto.setNameProduct(productBean.getName());
      listingStockDto.setImage(listingImageBeans.get(0).getImageUrl());
      listingStockDto.setPrice(stockBean.getPrice());
      listingStockDto.setDescription(stockBean.getDescription());
      listingStockDto.setStatuts(stockBean.getStatuts());
      return listingStockDto;
   }
   public ListingStockDetailsDto buildStockDetailsDto(StockBean stockBean){
      ProductBean productBean = productService.getProduct(stockBean.getProductId());
      List<ListingImageBean> listingImageBeans = listingImageService.findAllByStockId(stockBean.getId());

      ListingStockDetailsDto listingStockDto = new ListingStockDetailsDto();
      listingStockDto.setId(stockBean.getId());
      listingStockDto.setNameProduct(productBean.getName());
      listingStockDto.setImages(listingImageBeans.stream().map(ListingImageBean::getImageUrl).collect(Collectors.toList()));
      listingStockDto.setQteDisponible(stockBean.getQteDisponible());
      listingStockDto.setQteRestante(stockBean.getQteRestante());
      listingStockDto.setPrice(stockBean.getPrice());
      listingStockDto.setDescription(stockBean.getDescription());
      listingStockDto.setCreationDate(stockBean.getCreationDate());
      listingStockDto.setLastModificationDate(stockBean.getLastModificationDate());
      listingStockDto.setStatuts(stockBean.getStatuts());
      return listingStockDto;
   }

   @PostMapping("/stock")
   @ResponseStatus(HttpStatus.CREATED)
   public ListingStockDto addStock(
           @CurrentSecurityContext(expression="authentication?.name")
           String username,
           @Validated @RequestBody AddStockRequest addStockRequest){

      StockBean stockBean = stockService.add(addStockRequest, username);
      return ListingStockDto.builder()
              .id(stockBean.getId())
              .build();
   }

   @PutMapping("/stock/{stockId}")
   @ResponseStatus(HttpStatus.ACCEPTED)
   public ListingStockDto updateStock(@CurrentSecurityContext(expression="authentication?.name")
                                         String username,
                                      @Validated @RequestBody UpdateStockRequest updateStockRequest,
                                      @PathVariable(value = "stockId") Long stockId ){

      StockBean stockBean = stockService.update(updateStockRequest, username, stockId);
      return  buildStockDto(stockBean);
   }

   @PostMapping("/product/image")
   @ResponseStatus(HttpStatus.CREATED)
   public ListingImageResponse addImage(@CurrentSecurityContext(expression="authentication?.name")
                                      String username,
                                        @Validated @RequestBody List<ListingImageRequest> listingImageRequest){

      listingImageService.add(listingImageRequest, username);
      ListingImageResponse listingImageResponse = new ListingImageResponse();
      listingImageResponse.setSuccess(true);

      return listingImageResponse ;
   }
}

