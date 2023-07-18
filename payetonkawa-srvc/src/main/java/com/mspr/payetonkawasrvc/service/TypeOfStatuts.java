package com.mspr.payetonkawasrvc.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TypeOfStatuts {
   @JsonProperty("disponible")
   DISPONIBLE,

   @JsonProperty("non_disponible")
   NON_DISPONIBLE

}
