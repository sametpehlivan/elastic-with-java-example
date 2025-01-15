package com.sametp.els;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.sametp.els.dtos.ProductDto;


import java.io.IOException;
import java.util.List;


public class Repository {
    private final ElasticsearchClient client;
    private final String INDEX_NAME;
    public Repository(String indexName) {
        this.client = ESConfig.getEsRestClient();
        this.INDEX_NAME = indexName;
    }
    public void printClientInfo() throws IOException {
        System.out.println(client.info());
    }
    public void createIndex(){

    }
    public ProductDto createProduct(String productName,Double price) throws IOException {
        ProductDto productDto = new ProductDto(null,productName,price);
        IndexResponse response =  client.index(builder ->
                builder.index(INDEX_NAME)
                        .document(productDto)
        );
        return new ProductDto(response.id(),productName,price);
    }
    public ProductDto getById(String id) throws IOException {
        ProductDto response =  client.get(g -> g.index(INDEX_NAME).id(id),ProductDto.class).source();
        response.setId(id);
        return response;
    }
    public List<ProductDto> getByProductName(String productName) throws IOException {
        SearchResponse<ProductDto> response = client.search(s ->
            s.index(INDEX_NAME)
                    .query(q ->
                        q.match(t->
                                t.field("product_name")
                                        .query(productName)
                        )
                    ),
            ProductDto.class
        );
        return response.hits().hits().stream().map(Hit::source).toList();
    }
}
