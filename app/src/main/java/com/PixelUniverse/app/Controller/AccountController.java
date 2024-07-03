package com.PixelUniverse.app.Controller;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Entity.Role;
import com.PixelUniverse.app.Repository.AccountRepository;
import com.PixelUniverse.app.Request.Account.AccountChart;
import com.PixelUniverse.app.Request.Account.AccountSaveObject;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import com.PixelUniverse.app.Response.Authentication.RegisterResponse;
import com.PixelUniverse.app.Service.AccountService;
import com.PixelUniverse.app.Service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final AuthenticationService authenticationService;
    @GetMapping("/list")
    public ResponseEntity<?> list(){
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(value = "image", required = false)MultipartFile image,@RequestParam("formJson") String formJson){
        AccountSaveObject accountSaveObject;
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(image);
        if (image == null || image.isEmpty()) {
            image = null; // đảm bảo image là null nếu không có file được tải lên
        }
        try {
            accountSaveObject = objectMapper.readValue(formJson, AccountSaveObject.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new RegisterResponse("Form not invalid"));
        }
        return accountService.saveAccount(accountSaveObject,image);
    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delAccount(@PathVariable Long id){
        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()){
            return ResponseEntity.badRequest().body("Not Exists");
        }
        Account acc = account.get();
        acc.setDeleted(true);
        accountRepository.save(acc);
        return ResponseEntity.ok().body(account.get().getEmail()+" is deleted");
    }
    @PostMapping("/add")
    public ResponseEntity<?> addAccount(@RequestParam("image")MultipartFile image,@RequestParam("formJson") String formJson){
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest registerRequest;
        try {
            registerRequest = objectMapper.readValue(formJson, RegisterRequest.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Form gui khong phu hop");
        }

        return authenticationService.AddAccount(registerRequest,image);
    }

    @GetMapping("/chart")
    public ResponseEntity<?> chartAcount(){
        int currentYear = LocalDate.now().getYear();
        List<Account> accountList = accountRepository.findAllByCreateAtYear(currentYear);
        List<AccountChart> result = new ArrayList<>();
        int[][] arr = new int[12][3];
        for (Account acc : accountList){
            int monthValue = acc.getCreateAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
            Optional<Role> role = acc.getRoleSet().stream().findFirst();
            if (role.isEmpty()){
                return ResponseEntity.badRequest().body(new RegisterResponse("get role failed"));
            }
            int index =(int) role.get().getId().longValue();
            System.out.println(index);
            arr[monthValue-1][index]++;
        }

        List<AccountChart> accountCharts = new ArrayList<>();
        for (int i=1;i<13;i++){
            AccountChart accountChart = new AccountChart();
            accountChart.setMonth(i);
            accountChart.setAdmin(arr[i-1][0]);
            accountChart.setEmployee(arr[i-1][1]);
            accountChart.setClient(arr[i-1][2]);
            accountCharts.add(accountChart);
        }
        return ResponseEntity.ok().body(accountCharts);
    }



}
