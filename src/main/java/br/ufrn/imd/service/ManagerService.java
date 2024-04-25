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

    @Autowired
    private ManagerRepository managerRepository;

    public Manager saveManager(Manager manager) {
        validateManager(manager);
        return managerRepository.save(manager);
    }

    public Optional<Manager> getManagerById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Manager ID cannot be null or empty.");
        }
        return managerRepository.findById(id);
    }

    public void deleteManager(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Manager ID cannot be null or empty for deletion.");
        }
        managerRepository.deleteById(id);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    private void validateManager(Manager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null.");
        }
        if (!StringUtils.hasText(manager.getName())) {
            throw new IllegalArgumentException("Manager name cannot be empty.");
        }
        if (manager.getEmail() != null && !manager.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}
