package com.vinsguru.playground.sec03.service;

import com.vinsguru.playground.sec03.dto.CustomerDto;
import com.vinsguru.playground.sec03.mapper.EntityDtoMapper;
import com.vinsguru.playground.sec03.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllCustomers() {
        return  this.customerRepository.findAll()
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> getCustomerById(Integer id) {
        return this.customerRepository.findById(id)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> mono) {
        return mono.map(EntityDtoMapper::toEntity)
                .flatMap(this.customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> updatingCustomer(Integer id, Mono<CustomerDto> mono) {
        return this.customerRepository.findById(id)// buscamos al cliente por id
                .flatMap(entity -> mono)//si existe, lo sustituimos por la nueva entidad
                .map(EntityDtoMapper::toEntity)//convertimos el dto a entidad pero le faltara el id
                .doOnNext(c -> c.setId(id))//asi que le asignamos el id que nos llega por parametro
                .flatMap(this.customerRepository::save)//guardamos la entidad actualizada
                .map(EntityDtoMapper::toDto);//convertimos la entidad a dto
    }

    public Mono<Void> deleteCustomerById(Integer id) {
        return this.customerRepository.deleteById(id);
    }


}

