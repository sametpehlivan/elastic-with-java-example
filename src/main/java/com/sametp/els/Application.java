package com.sametp.els;


import com.sametp.els.dtos.ProductDto;

public class Application {
    public static void main(String[] args) throws Exception {
        Repository repository = new Repository("products");
        repository.printClientInfo();
        //ProductDto createdProduct = repository.createProduct("sametidsafd",33.234);
        //System.out.println("***********     CREATE    **************");
        //System.out.println(createdProduct);
        //System.out.println("*********** GET DOC BY ID **************");
        //System.out.println(repository.getById(createdProduct.getId()));
        //System.out.println("*********** BASIC  SEARCH **************");
        System.out.println(repository.getByProductName("sametidsafd"));
    }
}
