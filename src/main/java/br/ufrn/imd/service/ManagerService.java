package br.ufrn.imd.service;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public Manager saveManager(Manager manager) {
        validateManager(manager);
        return managerRepository.save(manager);
    }

    public Optional<Manager> getManagerById(String id) {
        validateId(id);
        return managerRepository.findById(id);
    }

    public void deleteManager(String id) {
        validateId(id);
        managerRepository.deleteById(id);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }
    

    private void validateId(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Manager ID cannot be null or empty.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
    }
}
