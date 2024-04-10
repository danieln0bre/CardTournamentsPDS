package br.ufrn.imd.service;

import br.ufrn.imd.model.Manager;
import br.ufrn.imd.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public Optional<Manager> getManagerById(String id) {
        return managerRepository.findById(id);
    }

    public void deleteManager(String id) {
        managerRepository.deleteById(id);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }
}
