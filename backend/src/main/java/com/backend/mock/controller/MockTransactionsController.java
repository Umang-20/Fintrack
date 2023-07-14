package com.backend.mock.controller;

import com.backend.dto.AccountDTO;
import com.backend.dto.ExpenseDTO;
import com.backend.dto.IncomeDTO;
import com.backend.mock.dto.MockExpenseDTO;
import com.backend.mock.dto.MockIncomeDTO;
import com.backend.service.AccountService;
import com.backend.service.ExpenseService;
import com.backend.service.IncomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import static com.backend.util.getUserEmail;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/transactions")
public class MockTransactionsController {
    private final AccountService accountService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @GetMapping("/expenses")
    public ResponseEntity getExpenses() {
        generateExpenses().forEach(mockExpense -> {
            ExpenseDTO expense = new ExpenseDTO();
            expense.setAccountNumber(mockExpense.getAccountNumber());
            expense.setExpenseAmount(mockExpense.getExpenseAmount());
            expense.setExpenseDescription(mockExpense.getExpenseDescription());
            expense.setDate(mockExpense.getDate());
            expenseService.saveExpense(expense);
        });
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/incomes")
    public ResponseEntity getIncomes() {
        generateIncomes().stream().forEach(mockIncome -> {
            IncomeDTO income = new IncomeDTO();
            income.setAccountNumber(mockIncome.getAccountNumber());
            income.setAmount(mockIncome.getAmount());
            income.setDescription(mockIncome.getDescription());
            income.setDate(mockIncome.getDate());
            incomeService.addIncome(income);
        });
        return ResponseEntity.status(200).body(null);
    }

    private ArrayList<MockIncomeDTO> generateIncomes() {
        ArrayList<MockIncomeDTO> generatedIncomes = new ArrayList<>();
        Random random = new Random();
        int newIncomesSize = random.nextInt(3);
        ArrayList<String> descriptionList = getDescriptionList("src/main/java/com/backend/mock/incomeDescriptionList.txt");
        int descriptionListSize = descriptionList.size();
        ArrayList<AccountDTO> accountList = (ArrayList<AccountDTO>) accountService.getAccounts().stream().filter(account -> account.getAccountType().equals("Debit"));
        int accountListSize = accountList.size();

        for (int i = 0; i < newIncomesSize; i++) {
            MockIncomeDTO income = new MockIncomeDTO();
            AccountDTO account = accountList.get(random.nextInt(accountListSize));
            String description = descriptionList.get(random.nextInt(descriptionListSize));
            double amount = account.getBalance() * random.nextDouble(1) / 50;
            Date date = new Date(System.currentTimeMillis() - 3600 * random.nextInt(1, 24) * random.nextInt(7));

            income.setAccountNumber(account.getAccountNumber());
            income.setAmount(amount);
            income.setDescription(description);
            income.setDate(date);
            generatedIncomes.add(income);
        }

        return generatedIncomes;
    }

    private ArrayList<MockExpenseDTO> generateExpenses() {
        ArrayList<MockExpenseDTO> generatedExpenses = new ArrayList<>();
        Random random = new Random();
        int newExpensesSize = random.nextInt(5);
        ArrayList<String> descriptionList = getDescriptionList("src/main/java/com/backend/mock/expenseDescriptionList.txt");
        int descriptionListSize = descriptionList.size();
        ArrayList<AccountDTO> accountList = accountService.getAccounts();
        int accountListSize = accountList.size();

        if (newExpensesSize == 0) {
            return generatedExpenses;
        }

        for (int i = 0; i < newExpensesSize; i++) {
            MockExpenseDTO expense = new MockExpenseDTO();
            AccountDTO account = accountList.get(random.nextInt(accountListSize));
            String description = descriptionList.get(random.nextInt(descriptionListSize));
            double amount = account.getBalance() * random.nextDouble(1) / 50;
            Date date = new Date(System.currentTimeMillis() - 3600 * random.nextInt(1, 24) * random.nextInt(7));

            expense.setAccountNumber(account.getAccountNumber());
            expense.setExpenseAmount(amount);
            expense.setExpenseDescription(description);
            expense.setDate(date);
            generatedExpenses.add(expense);
        }

        return generatedExpenses;
    }

    private ArrayList<String> getDescriptionList(String path) {
        ArrayList<String> descriptionList = new ArrayList<>();
        try {
            File file = new File(path);
            Scanner listReader = new Scanner(file);
            while (listReader.hasNextLine()) {
                descriptionList.add(listReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("failed to load description list");
        }
        return descriptionList;
    }
}
