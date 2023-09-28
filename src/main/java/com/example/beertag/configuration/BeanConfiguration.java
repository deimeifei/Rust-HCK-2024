//package com.example.beertag.configuration;
//
//import com.example.beertag.repositories.BeerMapRepositoryImpl;
//import com.example.beertag.repositories.BeerRepository;
//import com.example.beertag.services.BeerService;
//import com.example.beertag.services.BeerServiceImpl;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class BeanConfiguration {
//
//    @Bean
//    public BeerService beerService(){
//        return new BeerServiceImpl(beerRepository());
//    }
//
//    @Bean
//    public BeerRepository beerRepository(){
//        return new BeerMapRepositoryImpl();
//    }
//
//    //Не се ползва понеже използваме @Service, @Repository and @Autowire!!!
//}
