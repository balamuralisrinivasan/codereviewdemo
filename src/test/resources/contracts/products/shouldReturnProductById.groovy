package contracts.products

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return a product by id"
    
    request {
        method GET()
        url value(consumer(regex('/api/products/[0-9]+')), producer('/api/products/${productContractBase.getFirstProductId()}'))
    }
    
    response {
        status 200
        headers {
            contentType applicationJson()
        }
        body([
            id: value(producer(productContractBase.getFirstProductId())),
            name: "Test Product 1",
            description: "Test Description for product 1",
            price: 99.99,
            quantity: 10,
            category: "ELECTRONICS"
        ])
    }
} 