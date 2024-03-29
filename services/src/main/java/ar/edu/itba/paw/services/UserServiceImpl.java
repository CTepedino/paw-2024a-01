package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(final UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findById(long id){
        return userDao.findById(id);
    }

    @Override
    public User create(String username){
        return userDao.create(username);
    }
}
