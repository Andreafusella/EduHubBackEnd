package com.andrea.service;

import com.andrea.dao.AccountDao;
import com.andrea.dto.NewAccountDto;
import com.andrea.exception.EmailExistException;
import com.andrea.model.Account;
import com.andrea.model.AccountWithEmail;

import java.util.List;

public class AccountService {
    private AccountDao accountDao = new AccountDao();

    public AccountWithEmail addAccount(NewAccountDto account) throws EmailExistException {
        return accountDao.addAccount(account);
    }

    public List<AccountWithEmail> getAllAccountWithEmail() {
        return accountDao.getAllAccountWithEmail();
    }

    public List<AccountWithEmail> getAllAccountWithEmailWithParam(String role) {
        return accountDao.getAllAccountWithEmailWithParam(role);
    }

    public String removeAccount(int id_account) {
        return accountDao.removeAccount(id_account);
    }

    public AccountWithEmail getStudent(int id_account) {
        return accountDao.getStudent(id_account);
    }

    public List<AccountWithEmail> getStudentsByCourse(int id_account) {
        return accountDao.getStudentsByCourse(id_account);
    }

    public List<AccountWithEmail> getStudentsNotInCourse(int id_account) {
        return accountDao.getStudentsNotInCourse(id_account);
    }
}
