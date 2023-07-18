package com.mspr.payetonkawasrvc.web;

import com.mspr.payetonkawasrvc.core.ProductBean;
import com.mspr.payetonkawasrvc.core.UserBean;
import com.mspr.payetonkawasrvc.service.ProductService;
import com.mspr.payetonkawasrvc.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/configurations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigurationController {
   private ProductService productService;
   private UserService userService;

   public ConfigurationController(ProductService productService, UserService userService) {
      this.productService = productService;
      this.userService = userService;
   }

   @GetMapping("")
   @ResponseStatus(HttpStatus.OK)
   public GetConfigurationResponse getAllProduct(){
      List<ProductBean> productBeans = productService.getAllProduct();

      List<ListingProductDto> productDtos = productBeans.stream()
              .map(productBean -> {
                 ListingProductDto listingProductDto = new ListingProductDto();
                 listingProductDto.setId(productBean.getId());
                 listingProductDto.setName(productBean.getName());

                 return listingProductDto;
              })
              .collect(Collectors.toList());
      GetConfigurationResponse getConfigurationResponse = new GetConfigurationResponse();
      getConfigurationResponse.setProducts(productDtos);
      getConfigurationResponse.setError(false);
      return getConfigurationResponse;
   }

   @GetMapping("/profile")
   @ResponseStatus(HttpStatus.OK)
   public GetProfileResponse getProfile(@CurrentSecurityContext(expression="authentication?.name")
                                           String username){
      UserBean userBean = userService.findByEmail(username);
      return GetProfileResponse.builder()
              .firstname(userBean.getFirstname())
              .lastname(userBean.getLastname())
              .email(userBean.getEmail())
              .build();
   }
}