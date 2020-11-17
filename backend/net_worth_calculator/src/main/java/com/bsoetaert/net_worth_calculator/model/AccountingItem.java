package com.bsoetaert.net_worth_calculator.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "category",
        "values"
})
public class AccountingItem {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("category")
    private Integer category;
    @JsonProperty("values")
    private List<AccountingValue> values = null;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("category")
    public Integer getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(Integer category) {
        this.category = category;
    }

    @JsonProperty("values")
    public List<AccountingValue> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<AccountingValue> values) {
        this.values = values;
    }

    public AccountingValue getValue(String name) {
        for (AccountingValue acctValue : values) {
            if (acctValue.getName() != null && acctValue.getName().equals(name)) {
                return acctValue;
            }
        }
        return null;
    }
}