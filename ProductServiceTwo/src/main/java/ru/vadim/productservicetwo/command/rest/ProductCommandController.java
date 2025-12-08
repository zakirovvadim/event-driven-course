package ru.vadim.productservicetwo.command.rest;

import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vadim.productservicetwo.command.CreateProductCommand;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    @Autowired
    private CommandGateway commandGateway;

    // READ: получить все продукты
//    @GetMapping
//    public String getAllProducts() {
//        return "GET all products";
//    }

    // READ: получить продукт по id
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id) {
        return "GET product with id = " + id;
    }

    // CREATE: создать новый продукт
    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {
        CreateProductCommand createProductCommand = new CreateProductCommand(
                UUID.randomUUID().toString(),
                createProductRestModel.getTitle(),
                createProductRestModel.getPrice(),
                createProductRestModel.getQuantity()
        );

        String returnValue = commandGateway.sendAndWait(createProductCommand);
        return returnValue;
    }

//    // UPDATE: обновить продукт по id
//    @PutMapping("/{id}")
//    public String updateProduct(@PathVariable Long id) {
//        return "PUT update product with id = " + id;
//    }
//
//    // DELETE: удалить продукт по id
//    @DeleteMapping("/{id}")
//    public String deleteProduct(@PathVariable Long id) {
//        return "DELETE product with id = " + id;
//    }
}
